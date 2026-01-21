package com.akshay.websockettask.service;

import com.akshay.websockettask.DTO.Event;
import com.akshay.websockettask.DTO.EventType;
import com.akshay.websockettask.DTO.TodoCollectionDto;
import com.akshay.websockettask.Exceptions.NotFoundException;
import com.akshay.websockettask.repository.TodoCollectionRepository;
import com.akshay.websockettask.entity.TodoCollection;
import com.akshay.websockettask.mapper.TodoCollectionMapper;
import com.akshay.websockettask.util.WebSocketEventUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PostgresTodoCollectionService implements  TodoCollectionService{
    private final TodoCollectionRepository repository;
    private final TodoCollectionMapper todoCollectionMapper;
    private final WebSocketEventUtil webSocketEventUtil;

    private final String TOPIC = "/topic/collections";

    private void publishEvent(EventType type, TodoCollection collection) {
        TodoCollectionDto dto = todoCollectionMapper.toDto(collection);
        webSocketEventUtil.publish(
                TOPIC,
                new Event<>(type, dto)
        );
    }

    @Override
    public List<TodoCollectionDto> getAll() {
        return todoCollectionMapper.toDtoList(repository.findAll());
    }

    @Override
    @Transactional
    public TodoCollectionDto create(TodoCollectionDto collectionDto) {
        TodoCollection saved =
                repository.save(todoCollectionMapper.toEntity(collectionDto));

        publishEvent(EventType.COLLECTION_CREATED, saved);
        return todoCollectionMapper.toDto(saved);
    }

    @Override
    @Transactional
    public TodoCollectionDto update(UUID id, TodoCollectionDto updatedDto) {

        TodoCollection existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("TodoCollection"));

        existing.setTitle(updatedDto.getTitle());
        TodoCollection saved = repository.save(existing);

        publishEvent(EventType.COLLECTION_UPDATED, saved);
        return todoCollectionMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void delete(UUID id) {

        TodoCollection existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("TodoCollection"));

        repository.delete(existing);
        publishEvent(EventType.COLLECTION_DELETED, existing);
    }
}
