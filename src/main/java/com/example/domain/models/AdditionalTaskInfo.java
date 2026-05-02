package com.example.domain.models;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Value;

@Value
@Serdeable
public class AdditionalTaskInfo {
    private final Long userId;
    private final String userName;
    private final String userEmail;
}
