package com.example.testtask.note.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notes")
public class Note {
    @Id
    private String id;
    private String title;
    private Instant createdDate;
    private String text;
    private List<Tag> tags;
}
