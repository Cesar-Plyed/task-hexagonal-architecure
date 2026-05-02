package com.example.domain.ports.in;

import com.example.domain.models.Task;

public interface CreateTaskUseCase {
    Task createTask(Task task);
}
