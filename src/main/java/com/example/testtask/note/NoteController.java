package com.example.testtask.note;

import com.example.testtask.note.dto.CreateNoteRequest;
import com.example.testtask.note.dto.NoteDto;
import com.example.testtask.note.dto.NoteListItemDto;
import com.example.testtask.note.dto.UpdateNoteRequest;
import com.example.testtask.note.model.Note;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
@Validated
public class NoteController {
    private final NoteService service;
    @Autowired
    public NoteController(NoteService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<NoteDto> create(@Valid @RequestBody CreateNoteRequest req) {
        Note note = service.create(req);
        return ResponseEntity.ok(toDto(note));
    }

    @GetMapping
    public ResponseEntity<Page<NoteListItemDto>> list(
            @RequestParam(required = false) List<String> tags,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Note> notes = service.list(tags, pageable);
        Page<NoteListItemDto> dtoPage = notes.map(n -> new NoteListItemDto(n.getId(), n.getTitle(), n.getCreatedDate()));
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDto> get(@PathVariable String id) {
        Note n = service.get(id);
        return ResponseEntity.ok(toDto(n));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDto> update(@PathVariable String id, @Valid @RequestBody UpdateNoteRequest req) {
        Note updated = service.update(id, req);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Integer>> stats(@PathVariable String id) {
        return ResponseEntity.ok(service.stats(id));
    }

    private NoteDto toDto(Note n) {
        List<String> tags = n.getTags() == null ? List.of() :
                n.getTags().stream()
                        .map(Enum::name)
                        .toList();
        return new NoteDto(n.getId(), n.getTitle(), n.getCreatedDate(), n.getText(), tags);
    }
}

