package com.example.testtask.note.dto;

import java.time.Instant;

public record NoteListItemDto(String id,
                              String title,
                              Instant createdDate) {
}
