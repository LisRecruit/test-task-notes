package com.example.testtask.note;

import com.example.testtask.note.dto.CreateNoteRequest;
import com.example.testtask.note.dto.UpdateNoteRequest;
import com.example.testtask.note.model.Note;
import com.example.testtask.note.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class NoteServiceImpl implements NoteService {
    private final NoteRepository repo;
    private static final String NOTE_NOT_FOUND = "Note not found";

    @Autowired
    public NoteServiceImpl(NoteRepository repo) { this.repo = repo; }

    @Override
    public Note create(CreateNoteRequest req) {
        List<Tag> tags = convertTags(req.tags());
        Note note = Note.builder()
                .title(req.title())
                .createdDate(Instant.now())
                .text(req.text())
                .tags(tags)
                .build();
        return repo.save(note);
    }

    @Override
    public Note update(String id, UpdateNoteRequest req) {
        Note note = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOTE_NOT_FOUND));
        note.setTitle(req.title());
        note.setText(req.text());
        note.setTags(convertTags(req.tags()));
        return repo.save(note);
    }

    @Override
    public void delete(String id) {
        Note noteToDelete = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOTE_NOT_FOUND));
        repo.delete(noteToDelete);
    }

    @Override
    public Note get(String id) {
        return repo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOTE_NOT_FOUND));
    }

    @Override
    public Page<Note> list(List<String> tagStrings, Pageable pageable) {
        if (tagStrings == null || tagStrings.isEmpty()) {
            return repo.findAll(pageable);
        }
        List<Tag> tags = convertTags(tagStrings);
        return repo.findByTagsIn(tags, pageable);
    }

    @Override
    public Map<String, Integer> stats(String id) {
        Note note = get(id);
        return TextStatisticsUtil.wordFrequencies(note.getText());
    }

    private List<Tag> convertTags(List<String> tags) {
        if (tags == null) return List.of();
        return tags.stream()
                .map(String::toUpperCase)
                .map(s -> {
                    try {
                        return Tag.valueOf(s);
                    } catch (IllegalArgumentException e) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Invalid tag provided: '" + s + "'. Allowed tags are: BUSINESS, PERSONAL, IMPORTANT.");
                    }
                })
                .toList();
    }

}
