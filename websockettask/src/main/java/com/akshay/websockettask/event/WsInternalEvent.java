package com.akshay.websockettask.event;

import com.akshay.websockettask.DTO.Event;
import lombok.Getter;

@Getter
public class WsInternalEvent<T> {

    private final String topic;
    private final Event<T> event;

    public WsInternalEvent(String topic, Event<T> event) {
        this.topic = topic;
        this.event = event;
    }
}
