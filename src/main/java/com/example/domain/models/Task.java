package com.example.domain.models;

import java.time.LocalDateTime;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Introspected
@Serdeable
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Nullable
    private Long id;
    private String title;
    private String description;
    private LocalDateTime creationDate;
    private boolean completed;
}
