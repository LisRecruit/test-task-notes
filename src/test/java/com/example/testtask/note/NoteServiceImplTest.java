package com.example.testtask.note;

import com.example.testtask.note.dto.CreateNoteRequest;
import com.example.testtask.note.dto.UpdateNoteRequest;
import com.example.testtask.note.model.Note;
import com.example.testtask.note.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.server.ResponseStatusException;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@Testcontainers
@SpringBootTest
class NoteServiceImplTest {

    @Container
    static final MongoDBContainer mongo = new MongoDBContainer("mongo:7.0.5");

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
    }

    @Autowired
    private NoteRepository repo;

    @Autowired
    private NoteServiceImpl service;

    @BeforeEach
    void setUp() {
        repo.deleteAll();
    }

    @Test
    void shouldCreateNoteSuccessfully() {
        CreateNoteRequest req = new CreateNoteRequest("Title", "Some text", List.of("business"));

        Note saved = service.create(req);

        assertNotNull(saved.getId());
        assertEquals("Title", saved.getTitle());
        assertEquals("Some text", saved.getText());
        assertEquals(List.of(Tag.BUSINESS), saved.getTags());
        assertNotNull(saved.getCreatedDate());
    }

    @Test
    void shouldThrowWhenInvalidTagProvided() {
        CreateNoteRequest req = new CreateNoteRequest("Bad", "text", List.of("wrongtag"));
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.create(req));
        assertEquals("400 BAD_REQUEST \"Invalid tag provided: 'WRONGTAG'. Allowed tags are: BUSINESS, PERSONAL, IMPORTANT.\"",
                ex.getMessage());
    }

    @Test
    void shouldUpdateExistingNote() {
        Note note = service.create(new CreateNoteRequest("Old", "Old text", List.of("personal")));
        UpdateNoteRequest req = new UpdateNoteRequest("New", "Updated text", List.of("important"));

        Note updated = service.update(note.getId(), req);

        assertEquals("New", updated.getTitle());
        assertEquals("Updated text", updated.getText());
        assertEquals(List.of(Tag.IMPORTANT), updated.getTags());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingNote() {
        UpdateNoteRequest req = new UpdateNoteRequest("New", "Updated text", List.of("business"));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.update("fake-id", req));
        assertEquals("Note not found", ex.getReason());
    }

    @Test
    void shouldDeleteNoteSuccessfully() {
        Note note = service.create(new CreateNoteRequest("Delete", "Body", List.of("business")));

        service.delete(note.getId());

        assertFalse(repo.findById(note.getId()).isPresent());
    }

    @Test
    void shouldThrowWhenDeletingNonExistingNote() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.delete("does-not-exist"));
        assertEquals("Note not found", ex.getReason());
    }

    @Test
    void shouldGetStatsForNote() {
        Note note = service.create(new CreateNoteRequest("Stats", "Note is just a note", List.of("personal")));

        Map<String, Integer> stats = service.stats(note.getId());

        assertEquals(Map.of("note", 2, "a", 1, "is", 1, "just", 1), stats);
    }

    @Test
    void shouldListNotesAndFilterByTags() {
        service.create(new CreateNoteRequest("One", "Body", List.of("business")));
        service.create(new CreateNoteRequest("Two", "Body", List.of("personal")));
        service.create(new CreateNoteRequest("Three", "Body", List.of("important")));

        Page<Note> all = service.list(null, PageRequest.of(0, 10));
        assertEquals(3, all.getTotalElements());

        Page<Note> personal = service.list(List.of("personal"), PageRequest.of(0, 10));
        assertEquals(1, personal.getTotalElements());
        assertEquals("Two", personal.getContent().get(0).getTitle());
    }
}