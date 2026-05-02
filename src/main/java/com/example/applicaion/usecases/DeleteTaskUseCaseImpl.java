package com.example.applicaion.usecases;

import com.example.domain.ports.in.DeleteTaskUseCase;
import com.example.domain.ports.out.TaskRepositoryPort;

import jakarta.inject.Inject;

public class DeleteTaskUseCaseImpl implements DeleteTaskUseCase {
    private final TaskRepositoryPort taskRepositoryPort;

    @Inject
    public DeleteTaskUseCaseImpl(TaskRepositoryPort taskRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
    }


    @Override
    public Boolean deletetask(Long id) {
        return taskRepositoryPort.deleteById(id);
    }
   
}
