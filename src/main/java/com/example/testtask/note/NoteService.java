package com.example.testtask.note;

import com.example.testtask.note.dto.CreateNoteRequest;
import com.example.testtask.note.dto.UpdateNoteRequest;
import com.example.testtask.note.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface NoteService {
    Note create(CreateNoteRequest req);
    Note update(String id, UpdateNoteRequest req);
    void delete(String id);
    Note get(String id);
    Page<Note> list(List<String> tagStrings, Pageable pageable);
    Map<String, Integer> stats(String id);
}
