package com.akshay.websockettask.service;

import com.akshay.websockettask.DTO.Event;
import com.akshay.websockettask.DTO.EventType;
import com.akshay.websockettask.DTO.TodoDto;
import com.akshay.websockettask.Exceptions.NotFoundException;
import com.akshay.websockettask.repository.TodoCollectionRepository;
import com.akshay.websockettask.repository.TodoRepository;
import com.akshay.websockettask.entity.Todo;
import com.akshay.websockettask.entity.TodoCollection;
import com.akshay.websockettask.mapper.TodoMapper;
import com.akshay.websockettask.util.WebSocketEventUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
