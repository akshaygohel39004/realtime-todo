package com.akshay.websockettask.service;

import com.akshay.websockettask.dto.TodoDto;
import jakarta.transaction.Transactional;

import java.util.UUID;


public interface TodoService {


    public Iterable<TodoDto> getTodos(UUID cid);

    @Transactional
    public TodoDto create(UUID cid, TodoDto todoDto) ;

    @Transactional
    public TodoDto update(UUID cid, UUID todoId, TodoDto updateDto);

    @Transactional
    public void deleteTodo(UUID cid, UUID todoId);
}
