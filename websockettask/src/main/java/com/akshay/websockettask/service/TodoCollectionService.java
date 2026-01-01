package com.akshay.websockettask.service;

import com.akshay.websockettask.DTO.CollectionEvent;
import com.akshay.websockettask.Exceptions.NotFoundException;
import com.akshay.websockettask.Repository.TodoCollectionRepository;
import com.akshay.websockettask.entity.TodoCollection;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TodoCollectionService {
    private final TodoCollectionRepository repository;
    private final SimpMessagingTemplate template;
    private static final String TOPIC = "/topic/collections";

    private void publishEvent(String type, TodoCollection collection) {


        if (TransactionSynchronizationManager.isActualTransactionActive()) {

            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            template.convertAndSend(TOPIC, new CollectionEvent(type, collection)
                            );
                        }
                    }
            );
        } else {
            template.convertAndSend(TOPIC, new CollectionEvent(type, collection)
            );
        }
    }

    public List<TodoCollection> getAll(){
        return repository.findAll();
    }

    @Transactional
    public TodoCollection create(TodoCollection collection){
        TodoCollection saved=repository.save(collection);
        publishEvent("CREATED",saved);
        return saved;
    }

    @Transactional
    public TodoCollection update(UUID id,TodoCollection updated) throws NotFoundException {
        TodoCollection alreadyExist=repository.findById(id).orElseThrow(()->{
            return new NotFoundException("TodoCollection");
        });
        alreadyExist.setTitle(updated.getTitle());
        repository.save(alreadyExist);
        publishEvent("UPDATED",alreadyExist);
        return alreadyExist;
    }

    @Transactional
    public void delete(UUID id) throws NotFoundException {
        TodoCollection alreadyExist=repository.findById(id).orElseThrow(()->{
            return new NotFoundException("TodoCollection");
        });
        repository.delete(alreadyExist);
        publishEvent("DELETED",alreadyExist);
    }
}
