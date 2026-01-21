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

public interface TodoCollectionService {

    public List<TodoCollectionDto> getAll();
    @Transactional
    public TodoCollectionDto create(TodoCollectionDto collectionDto) ;

    @Transactional
    public TodoCollectionDto update(UUID id, TodoCollectionDto updatedDto);
    @Transactional
    public void delete(UUID id) ;
}
