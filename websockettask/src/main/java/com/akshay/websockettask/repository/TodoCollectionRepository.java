package com.akshay.websockettask.repository;

import com.akshay.websockettask.entity.TodoCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TodoCollectionRepository extends JpaRepository< TodoCollection, UUID> {

}
