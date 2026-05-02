package com.example.applicaion.usecases;

import java.util.List;
import java.util.Optional;

import com.example.domain.models.Task;
import com.example.domain.ports.in.RetrieveTaskUseCase;
import com.example.domain.ports.out.TaskRepositoryPort;

import jakarta.inject.Inject;

public class RetrieveTaskUseCaseImpl implements RetrieveTaskUseCase{

    private final TaskRepositoryPort taskRepositoryPort;

    @Inject
    public RetrieveTaskUseCaseImpl(TaskRepositoryPort taskRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
    }

    @Override
    public Optional<Task> getTaskByIdOptional(Long id) {
        return taskRepositoryPort.findById(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepositoryPort.findAll();
    }
    
}
