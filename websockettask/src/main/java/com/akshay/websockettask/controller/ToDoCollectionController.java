package com.akshay.websockettask.controller;

import com.akshay.websockettask.DTO.TodoCollectionDto;
import com.akshay.websockettask.entity.TodoCollection;
import com.akshay.websockettask.service.TodoCollectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/collections")
public class ToDoCollectionController {

    private final TodoCollectionService service;

    @GetMapping
    public ResponseEntity<List<TodoCollectionDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<TodoCollectionDto> create(@RequestBody TodoCollectionDto collectionDto) {
        TodoCollectionDto saved = service.create(collectionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoCollectionDto> update(@PathVariable UUID id, @RequestBody TodoCollectionDto updatedDto) {
        TodoCollectionDto saved = service.update(id, updatedDto);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
