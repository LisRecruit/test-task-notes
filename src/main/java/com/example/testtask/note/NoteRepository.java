package com.example.testtask.note;

import com.example.testtask.note.model.Note;
import com.example.testtask.note.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NoteRepository extends MongoRepository<Note, String> {
    Page<Note> findByTagsIn(List<Tag> tags, Pageable pageable);
}
