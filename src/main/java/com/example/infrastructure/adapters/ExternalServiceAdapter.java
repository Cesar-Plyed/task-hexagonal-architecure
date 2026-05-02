package com.example.infrastructure.adapters;

import com.example.domain.models.AdditionalTaskInfo;
import com.example.domain.ports.out.ExternalServicePort;
import com.example.infrastructure.ports.rest.TaskInfoClient;
import com.example.infrastructure.ports.rest.dto.JsonPlaceholderToDo;
import com.example.infrastructure.ports.rest.dto.JsonPlaceholderToDoUser;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.NoArgsConstructor;

@Singleton
@NoArgsConstructor
public class ExternalServiceAdapter implements ExternalServicePort {

    private TaskInfoClient taskInfoClient;

    @Inject
    public ExternalServiceAdapter(TaskInfoClient taskInfoClient) {
        this.taskInfoClient = taskInfoClient;
    }

    @Override
    public AdditionalTaskInfo getAdditionalTaskInfo(Long taskId) {
        JsonPlaceholderToDo toDo = taskInfoClient.getToDo(taskId);

        if (toDo == null) {
            return null;
        }

        JsonPlaceholderToDoUser user = taskInfoClient.getUser(toDo.getUserId());

        if (user == null) {
            return null;
        }

        return new AdditionalTaskInfo(user.getId(), user.getName(), user.getEmail());
    }
}
