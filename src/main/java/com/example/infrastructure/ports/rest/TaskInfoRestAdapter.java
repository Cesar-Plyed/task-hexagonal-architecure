package com.example.infrastructure.ports.rest;

import com.example.domain.models.AdditionalTaskInfo;
import com.example.domain.ports.out.TaskInfoRepositoryPort;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class TaskInfoRestAdapter implements TaskInfoRepositoryPort {

    private final TaskInfoClient taskInfoClient;

    @Inject
    public TaskInfoRestAdapter(TaskInfoClient taskInfoClient) {
        this.taskInfoClient = taskInfoClient;
    }

    @Override
    public AdditionalTaskInfo getAdditionalInfo(Long id) {
        var todo = taskInfoClient.getToDo(id);

        if (todo == null) return null;

        var user = taskInfoClient.getUser(todo.getUserId());

        if (user == null) return null;

        return new AdditionalTaskInfo(user.getId(), user.getName(), user.getEmail());
    }
}
