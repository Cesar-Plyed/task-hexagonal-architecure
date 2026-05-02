package com.example.applicaion.usecases;

import java.util.Optional;

import com.example.domain.models.Task;
import com.example.domain.ports.in.UpdateTaskUseCase;
import com.example.domain.ports.out.TaskRepositoryPort;

import jakarta.inject.Inject;

public class UpdateTaskUseCaseImpl implements UpdateTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    @Inject
    public UpdateTaskUseCaseImpl(TaskRepositoryPort taskRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
    }

    @Override
    public Optional<Task> updateTaskOptional(Long id, Task updateTaskOptional) {
        return taskRepositoryPort.update(updateTaskOptional);
    }

}
