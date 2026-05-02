package com.example.infrastructure.config;

import com.example.applicaion.services.TaskService;
import com.example.applicaion.usecases.CreateTaskCaseUseImpl;
import com.example.applicaion.usecases.DeleteTaskUseCaseImpl;
import com.example.applicaion.usecases.GetAdditionalTaskInfoUseCaseImpl;
import com.example.applicaion.usecases.RetrieveTaskUseCaseImpl;
import com.example.applicaion.usecases.UpdateTaskUseCaseImpl;
import com.example.domain.ports.in.GetAdditionalTaskInfoUseCase;
import com.example.domain.ports.out.ExternalServicePort;
import com.example.domain.ports.out.TaskRepositoryPort;
import com.example.infrastructure.repositoryes.JpaTaskRepositoryAdapter;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

@Factory
public class ApplicationConfig {
    @Bean
    public TaskService taskService(TaskRepositoryPort taskRepositoryPort, GetAdditionalTaskInfoUseCase getAdditionalTaskInfoUseCase) {
        return new TaskService(
                new CreateTaskCaseUseImpl(taskRepositoryPort),
                new DeleteTaskUseCaseImpl(taskRepositoryPort),
                new RetrieveTaskUseCaseImpl(taskRepositoryPort),
                new UpdateTaskUseCaseImpl(taskRepositoryPort),
                getAdditionalTaskInfoUseCase
        );
    }

    @Bean
    public TaskRepositoryPort taskRepositoryPort(JpaTaskRepositoryAdapter jpaTaskRepositoryAdapter) {
        return jpaTaskRepositoryAdapter;
    }

    @Bean
    public GetAdditionalTaskInfoUseCase additionalTaskInfoUseCase(ExternalServicePort externalServicePort) {
        return new GetAdditionalTaskInfoUseCaseImpl(externalServicePort);
    }
}
