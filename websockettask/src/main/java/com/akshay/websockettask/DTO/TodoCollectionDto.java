package com.akshay.websockettask.DTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class TodoCollectionDto {

    private UUID id;
    private String title;
//    private List<UUID> todoIds;
}
