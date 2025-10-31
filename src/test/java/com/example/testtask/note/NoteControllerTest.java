package com.example.testtask.note;


import com.example.testtask.note.dto.CreateNoteRequest;
import com.example.testtask.note.dto.UpdateNoteRequest;
import com.example.testtask.note.model.Note;
import com.example.testtask.note.model.Tag;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class NoteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void setMongoUri(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    @AfterEach
    void cleanUp() {
        noteRepository.deleteAll();
    }


    private Note createTestNote(String title, String text, List<Tag> tags, Instant createdDate) {
        return noteRepository.save(Note.builder()
                .title(title)
                .createdDate(createdDate)
                .text(text)
                .tags(tags)
                .build());
    }


    @Test
    void shouldCreateNoteSuccessfully() throws Exception {
        CreateNoteRequest request = new CreateNoteRequest("New Title", "New Text", List.of("BUSINESS", "PERSONAL"));
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.tags", containsInAnyOrder("BUSINESS", "PERSONAL")))
                .andExpect(jsonPath("$.id").exists());

        assertEquals(1, noteRepository.count());
    }

    @Test
    void shouldReturn400WhenTitleIsMissingOnCreate() throws Exception {
        CreateNoteRequest request = new CreateNoteRequest(null, "Text", List.of("BUSINESS"));
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetExistingNoteById() throws Exception {
        Note note = createTestNote("Found Note", "Text", List.of(Tag.IMPORTANT), Instant.now());

        mockMvc.perform(get("/api/notes/{id}", note.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(note.getId()))
                .andExpect(jsonPath("$.title").value("Found Note"))
                .andExpect(jsonPath("$.text").value("Text"))
                .andExpect(jsonPath("$.tags", contains("IMPORTANT")));
    }

    @Test
    void shouldReturn404WhenNoteNotFoundOnGet() throws Exception {
        mockMvc.perform(get("/api/notes/{id}", "nonExistentId"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateExistingNote() throws Exception {
        Note note = createTestNote("Old Title", "Old Text", List.of(Tag.BUSINESS), Instant.now());
        UpdateNoteRequest request = new UpdateNoteRequest("Updated Title", "New Text", List.of("PERSONAL"));
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/notes/{id}", note.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.tags", contains("PERSONAL")));

        Note updatedInDb = noteRepository.findById(note.getId()).orElseThrow();
        assertEquals("Updated Title", updatedInDb.getTitle());
    }

    @Test
    void shouldDeleteNoteSuccessfully() throws Exception {
        Note note = createTestNote("Delete Me", "Text", List.of(), Instant.now());

        mockMvc.perform(delete("/api/notes/{id}", note.getId()))
                .andExpect(status().isNoContent()); // Проверяем статус 204

        assertEquals(0, noteRepository.count());
    }

    @Test
    void shouldListNotesFilteredByTagAndSortedByDate() throws Exception {
        Instant now = Instant.now();
        createTestNote("Note C", "Text", List.of(Tag.BUSINESS, Tag.IMPORTANT), now.minusSeconds(1));
        createTestNote("Note B", "Text", List.of(Tag.PERSONAL), now.minusSeconds(2));
        createTestNote("Note A", "Text", List.of(Tag.BUSINESS), now.minusSeconds(3));

        mockMvc.perform(get("/api/notes")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].title").value("Note C"))
                .andExpect(jsonPath("$.content[1].title").value("Note B"));

        mockMvc.perform(get("/api/notes")
                        .param("tags", "BUSINESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].title").value("Note C"))
                .andExpect(jsonPath("$.content[1].title").value("Note A"));
    }

    @Test
    void shouldReturnWordStatistics() throws Exception {
        Note note = createTestNote("Stats Note", "The word WORD is just a word.", List.of(), Instant.now());

        MvcResult result = mockMvc.perform(get("/api/notes/{id}/stats", note.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();

        String expectedJson = "{\"word\":3,\"a\":1,\"is\":1,\"just\":1,\"the\":1}";

        String expectedStripped = expectedJson.substring(1, expectedJson.length() - 1);
        String actualStripped = jsonResponse.substring(1, jsonResponse.length() - 1);

        assertEquals(expectedStripped, actualStripped, "The order of words in the statistics map must match the expected sorted order.");
    }
}