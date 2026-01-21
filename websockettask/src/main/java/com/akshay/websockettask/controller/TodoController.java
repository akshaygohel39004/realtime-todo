package com.akshay.websockettask.controller;

import com.akshay.websockettask.DTO.TodoDto;
import com.akshay.websockettask.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {


    private final TodoService service;

    @GetMapping("/{cid}")
    public ResponseEntity<Iterable<TodoDto>> getTodos(@PathVariable UUID cid) {
        return ResponseEntity.ok(service.getTodos(cid));
    }

    @PostMapping("/{cid}")
    public ResponseEntity<TodoDto> create(@PathVariable UUID cid, @RequestBody TodoDto todoDto) {
            TodoDto saved = service.create(cid, todoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{cid}/{todoId}")
    public ResponseEntity<TodoDto> update(@PathVariable UUID cid, @PathVariable UUID todoId, @RequestBody TodoDto todoDto) {
            TodoDto updated = service.update(cid, todoId, todoDto);
            return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/{cid}/{todoId}")
    public ResponseEntity<Void> delete(@PathVariable UUID cid, @PathVariable UUID todoId) {
            service.deleteTodo(cid, todoId);
            return ResponseEntity.noContent().build();
    }

}
