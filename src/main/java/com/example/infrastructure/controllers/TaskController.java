package com.example.infrastructure.controllers;

import io.micronaut.http.HttpResponse;

import java.util.List;

import com.example.applicaion.services.TaskService;
import com.example.domain.models.AdditionalTaskInfo;
import com.example.domain.models.Task;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

@Controller("/api/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Post
    public HttpResponse<Task> createTask(@Body Task task){
        Task createdTask = taskService.createTask(task);
        return HttpResponse.created(createdTask);
    } 

    @Get("/{taskId}")
    public HttpResponse<Task> getTaskById(@PathVariable Long taskId){
        return taskService.getTaskByIdOptional(taskId).map(HttpResponse::ok).orElse(HttpResponse.notFound());
    }

    @Get
    public HttpResponse<List<Task>> getAllTask(){
        List<Task> tasks = taskService.getAllTasks();
        return HttpResponse.ok(tasks);
    }

    @Put("/{taskId}")
    public HttpResponse<Task> updateTask(@PathVariable Long taskId, @Body Task updateTask){
        updateTask.setId(taskId);
        return taskService.updateTaskOptional(taskId, updateTask).map(HttpResponse::ok).orElse(HttpResponse.notFound());
    }

    @Delete("/{taskId}")
    public HttpResponse<Void> deleteTaskById(@PathVariable Long taskId){
        if (taskService.deletetask(taskId)) {
            return HttpResponse.noContent();
        } else {
            return HttpResponse.notFound();
        }
    }

    @Get("/{taskId}/aditionalInfo")
    @ExecuteOn(TaskExecutors.BLOCKING)
    public HttpResponse<AdditionalTaskInfo> getAdditionalTaskInfo(@PathVariable Long taskId){
        AdditionalTaskInfo additionalTaskInfo = taskService.getAdditionalTaskInfo(taskId);
        return HttpResponse.ok(additionalTaskInfo);
    }
}
