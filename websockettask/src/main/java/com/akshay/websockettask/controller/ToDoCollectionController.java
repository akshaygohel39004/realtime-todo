package com.akshay.websockettask.controller;

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
    public ResponseEntity<List<TodoCollection>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<TodoCollection> create(@RequestBody TodoCollection collection) {
        TodoCollection saved = service.create(collection);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoCollection> update(@PathVariable UUID id, @RequestBody TodoCollection updated) {
        TodoCollection saved = service.update(id, updated);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
