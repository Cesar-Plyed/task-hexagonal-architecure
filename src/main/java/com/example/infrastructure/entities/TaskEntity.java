package com.example.infrastructure.entities;

import java.time.LocalDateTime;

import com.example.domain.models.Task;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor  // JPA requiere constructor vacío obligatoriamente
@AllArgsConstructor // para tu constructor con todos los parámetros
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private LocalDateTime creationDate;
    private boolean completed;

    public static TaskEntity fromDomainModel(Task task) {
        return new TaskEntity(task.getId(), task.getTitle(), task.getDescription(), task.getCreationDate(), task.isCompleted());
    }

    public Task toDomainModel() {
        return new Task(id, title, description, creationDate, completed);
    }

}
