package com.example.applicaion.services;

import java.util.List;
import java.util.Optional;

import com.example.domain.models.AdditionalTaskInfo;
import com.example.domain.models.Task;
import com.example.domain.ports.in.CreateTaskUseCase;
import com.example.domain.ports.in.DeleteTaskUseCase;
import com.example.domain.ports.in.GetAdditionalTaskInfoUseCase;
import com.example.domain.ports.in.RetrieveTaskUseCase;
import com.example.domain.ports.in.UpdateTaskUseCase;


public class TaskService {

    private CreateTaskUseCase createTaskUseCase;
    private DeleteTaskUseCase deleteTaskUseCase;
    private RetrieveTaskUseCase retiveTaskUseCase;
    private UpdateTaskUseCase updateTaskUseCase;
    private GetAdditionalTaskInfoUseCase getAditionalTaskInfoUseCase;

    public TaskService(
        CreateTaskUseCase createTaskUseCase, 
        DeleteTaskUseCase deleteTaskUseCase,
        RetrieveTaskUseCase retiveTaskUseCase, 
        UpdateTaskUseCase updateTaskUseCase,
        GetAdditionalTaskInfoUseCase getAditionalTaskInfoUseCase
    ) {
        this.createTaskUseCase = createTaskUseCase;
        this.deleteTaskUseCase = deleteTaskUseCase;
        this.retiveTaskUseCase = retiveTaskUseCase;
        this.updateTaskUseCase = updateTaskUseCase;
        this.getAditionalTaskInfoUseCase = getAditionalTaskInfoUseCase;
    }

    public AdditionalTaskInfo getAdditionalTaskInfo(Long id) {
        return getAditionalTaskInfoUseCase.getAdditionalTaskInfo(id);
    }

    public Optional<Task> updateTaskOptional(Long id, Task updateTaskOptional) {
        return updateTaskUseCase.updateTaskOptional(id, updateTaskOptional);
    }

    public Optional<Task> getTaskByIdOptional(Long id) {
        return retiveTaskUseCase.getTaskByIdOptional(id);
    }

    public List<Task> getAllTasks() {
        return retiveTaskUseCase.getAllTasks();
    }

    public Boolean deletetask(Long id) {
        return deleteTaskUseCase.deletetask(id);
    }

    public Task createTask(Task task) {
        return createTaskUseCase.createTask(task);
    }
}
