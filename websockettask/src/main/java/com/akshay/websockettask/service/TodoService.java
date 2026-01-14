package com.akshay.websockettask.service;

import com.akshay.websockettask.DTO.Event;
import com.akshay.websockettask.DTO.EventType;
import com.akshay.websockettask.DTO.TodoDto;
import com.akshay.websockettask.Exceptions.NotFoundException;
import com.akshay.websockettask.Repository.TodoCollectionRepository;
import com.akshay.websockettask.Repository.TodoRepository;
import com.akshay.websockettask.entity.Todo;
import com.akshay.websockettask.entity.TodoCollection;
import com.akshay.websockettask.mapper.TodoMapper;
import com.akshay.websockettask.util.WebSocketEventUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository repository;
    private final TodoCollectionRepository todoCollectionRepository;
    private final TodoMapper todoMapper;
    private final WebSocketEventUtil webSocketEventUtil;

    private final String TOPIC = "/topic/todos/";

    private void broadcast(UUID cid, EventType eventType, Todo todo) {
        TodoDto dto = todoMapper.toDto(todo);
        webSocketEventUtil.publish(
                TOPIC + cid,
                new Event<>(eventType, dto)
        );
    }

    public Iterable<TodoDto> getTodos(UUID cid) {
        TodoCollection collection = todoCollectionRepository.findById(cid)
                .orElseThrow(() -> new NotFoundException("Collection"));
        return todoMapper.toDtoList(collection.getTodos());
    }

    @Transactional
    public TodoDto create(UUID cid, TodoDto todoDto) {

        TodoCollection collection = todoCollectionRepository.findById(cid)
                .orElseThrow(() -> new NotFoundException("Collection"));

        Todo todo = todoMapper.toEntity(todoDto);
        todo.setTodoCollection(collection);

        Todo saved = repository.save(todo);
        broadcast(cid, EventType.TODO_CREATED, saved);

        return todoMapper.toDto(saved);
    }

    @Transactional
    public TodoDto update(UUID cid, UUID todoId, TodoDto updateDto) {

        Todo todo = repository.findById(todoId)
                .orElseThrow(() -> new NotFoundException("Todo"));

        todo.setSubject(updateDto.getSubject());
        todo.setDescription(updateDto.getDescription());

        Todo saved = repository.save(todo);
        broadcast(cid, EventType.TODO_UPDATED, saved);

        return todoMapper.toDto(saved);
    }

    @Transactional
    public void deleteTodo(UUID cid, UUID todoId) {

        Todo todo = repository.findById(todoId)
                .orElseThrow(() -> new NotFoundException("Todo"));

        repository.delete(todo);
        broadcast(cid, EventType.TODO_DELETED, todo);
    }
}
