package com.akshay.websockettask.controller;

import com.akshay.websockettask.entity.Todo;
import com.akshay.websockettask.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private final TodoService service;

    @GetMapping("/{cid}")
    public ResponseEntity<Iterable<Todo>> getTodos(@PathVariable UUID cid) {
        return ResponseEntity.ok(service.getTodos(cid));
    }

    @PostMapping("/{cid}")
    public ResponseEntity<?> create(@PathVariable UUID cid, @RequestBody Todo todo) {
            Todo saved = service.create(cid, todo);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }

    @PutMapping("/{cid}/{todoId}")
    public ResponseEntity<?> update(@PathVariable UUID cid, @PathVariable UUID todoId, @RequestBody Todo todo) {
            Todo updated = service.update(cid, todoId, todo);
            return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/{cid}/{todoId}")
    public ResponseEntity<?> delete(@PathVariable UUID cid, @PathVariable UUID todoId) {
            service.deleteTodo(cid, todoId);
            return ResponseEntity.noContent().build();

    }

}
