package com.example.infrastructure.ports.rest.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

@Data
@Serdeable
public class JsonPlaceholderToDo {
    private Long id;
    private Long userId;
}