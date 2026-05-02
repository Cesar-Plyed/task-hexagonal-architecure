package com.example.domain.ports.in;

import java.util.Optional;

import com.example.domain.models.Task;

public interface UpdateTaskUseCase {
    Optional<Task> updateTaskOptional(Long id, Task updateTaskOptional);
}
