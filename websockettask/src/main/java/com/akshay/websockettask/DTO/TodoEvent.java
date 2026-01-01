package com.akshay.websockettask.DTO;

import com.akshay.websockettask.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoEvent {
    private String event;
    private Todo data;
}

