package com.example.domain.ports.in;

import com.example.domain.models.AdditionalTaskInfo;

public interface GetAdditionalTaskInfoUseCase {
     AdditionalTaskInfo getAdditionalTaskInfo(Long id);
}
