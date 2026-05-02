package com.example.domain.ports.in;

import java.util.List;
import java.util.Optional;

import com.example.domain.models.Task;

public interface RetrieveTaskUseCase {
    Optional<Task> getTaskByIdOptional(Long id);
    List<Task> getAllTasks();
}
