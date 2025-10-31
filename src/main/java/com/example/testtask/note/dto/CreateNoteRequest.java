package com.example.testtask.note.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateNoteRequest(@NotBlank String title,
                                @NotBlank String text,
                                List<String> tags) {
}
