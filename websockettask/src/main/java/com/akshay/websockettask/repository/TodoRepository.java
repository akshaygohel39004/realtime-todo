package com.akshay.websockettask.repository;

import com.akshay.websockettask.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TodoRepository extends JpaRepository< Todo, UUID> {

}
