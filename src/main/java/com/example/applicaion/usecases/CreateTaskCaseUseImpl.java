package com.example.applicaion.usecases;

import com.example.domain.models.Task;
import com.example.domain.ports.in.CreateTaskUseCase;
import com.example.domain.ports.out.TaskRepositoryPort;

import jakarta.inject.Inject;

public class CreateTaskCaseUseImpl implements CreateTaskUseCase{

    private final TaskRepositoryPort taskRepositoryPort;

    @Inject
    public CreateTaskCaseUseImpl(TaskRepositoryPort taskRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
    }

    @Override
    public Task createTask(Task task) {
        return taskRepositoryPort.save(task);
    }

}
