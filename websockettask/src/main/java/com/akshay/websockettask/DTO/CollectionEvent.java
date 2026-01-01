package com.akshay.websockettask.DTO;

import com.akshay.websockettask.entity.TodoCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionEvent {
    private String event;
    private TodoCollection data;
}
