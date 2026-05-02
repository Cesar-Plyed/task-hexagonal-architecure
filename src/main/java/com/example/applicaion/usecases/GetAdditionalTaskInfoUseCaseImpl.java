package com.example.applicaion.usecases;

import com.example.domain.models.AdditionalTaskInfo;
import com.example.domain.ports.in.GetAdditionalTaskInfoUseCase;
import com.example.domain.ports.out.ExternalServicePort;

import jakarta.inject.Inject;

public class GetAdditionalTaskInfoUseCaseImpl implements GetAdditionalTaskInfoUseCase{
    
    private final ExternalServicePort externalServicePort;

    @Inject
    public GetAdditionalTaskInfoUseCaseImpl(ExternalServicePort externalServicePort) {
        this.externalServicePort = externalServicePort;
    }


    @Override
    public AdditionalTaskInfo getAdditionalTaskInfo(Long id) {
        return externalServicePort.getAdditionalTaskInfo(id);
    }

}
