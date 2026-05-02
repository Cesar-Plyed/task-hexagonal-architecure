package com.example.infrastructure.repositoryes;

import com.example.infrastructure.entities.TaskEntity;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

@Repository
public interface JpaTaskRepository extends JpaRepository<TaskEntity, Long> {
}
