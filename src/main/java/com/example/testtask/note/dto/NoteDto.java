package com.example.testtask.note.dto;

import java.time.Instant;
import java.util.List;

public record NoteDto(String id,
                      String title,
                      Instant createdDate,
                      String text,
                      List<String> tags) {
}
