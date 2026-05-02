package com.example.infrastructure.ports.rest.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

@Data
@Serdeable
public class JsonPlaceholderToDoUser {
    private Long id;
    private String name;
    private String email;
}