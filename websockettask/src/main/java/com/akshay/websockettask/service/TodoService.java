package com.akshay.websockettask.service;

import com.akshay.websockettask.DTO.TodoEvent;
import com.akshay.websockettask.Exceptions.NotFoundException;
import com.akshay.websockettask.Repository.TodoCollectionRepository;
import com.akshay.websockettask.Repository.TodoRepository;
import com.akshay.websockettask.entity.Todo;
import com.akshay.websockettask.entity.TodoCollection;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository repository;
    private final TodoCollectionRepository todoCollectionRepository;
    private  final SimpMessagingTemplate tamplate;

    private final String TOPIC="/topic/todos/";
    private void broadcast(UUID cid, String event, Todo todo) {

        if (TransactionSynchronizationManager.isActualTransactionActive()) {

            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            tamplate.convertAndSend(
                                    TOPIC + cid,
                                    new TodoEvent(event, todo)
                            );
                        }
                    }
            );
        } else {

            tamplate.convertAndSend(
                    TOPIC + cid,
                    new TodoEvent(event, todo)
            );
        }
    }


    public Iterable<Todo> getTodos(UUID cid){
        TodoCollection todoCollection = todoCollectionRepository.findById(cid)
                .orElseThrow(() -> new NotFoundException("Collection"));

        if (todoCollection.getTodos() == null) {
            throw new NotFoundException("Todos not initialized");
        }

        return todoCollection.getTodos();
    }


    @Transactional
    public Todo create(UUID cid,Todo todo) throws NotFoundException {
        TodoCollection collection=todoCollectionRepository.findById(cid).orElseThrow(
                ()->new NotFoundException("Collection")
        );

        todo.setTodoCollection(collection);
        Todo saved= repository.save(todo);
        broadcast(cid,"CREATED",saved);
        return saved;

    }

    @Transactional
    public Todo update(UUID cid,UUID todoId,Todo update) throws NotFoundException {
        Todo oldTodo= repository.findById(todoId).orElseThrow(
                ()-> new NotFoundException("todo")
        );

        oldTodo.setDescription(update.getDescription());
        oldTodo.setSubject(update.getSubject());

        Todo saved= repository.save(oldTodo);

        broadcast(cid,"UPDATED",saved);

        return saved;


    }

    @Transactional
    public void deleteTodo(UUID cid,UUID todoId) throws NotFoundException {
        Todo oldTodo= repository.findById(todoId).orElseThrow(
                ()-> new NotFoundException("todo")
        );

        repository.delete(oldTodo);
        broadcast(cid,"DELETED",oldTodo);
    }

}
