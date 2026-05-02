package com.example.infrastructure.ports.rest;

import com.example.infrastructure.ports.rest.dto.JsonPlaceholderToDo;
import com.example.infrastructure.ports.rest.dto.JsonPlaceholderToDoUser;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client("https://jsonplaceholder.typicode.com")
public interface TaskInfoClient {

    @Get("/todos/{id}")
    JsonPlaceholderToDo getToDo(Long id);

    @Get("/users/{id}")
    JsonPlaceholderToDoUser getUser(Long id);
}