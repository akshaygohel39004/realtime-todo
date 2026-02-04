package com.akshay.websockettask.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class TodoDto {

    private UUID todoId;
    private String subject;
    private String description;
    private UUID collectionId;
}
