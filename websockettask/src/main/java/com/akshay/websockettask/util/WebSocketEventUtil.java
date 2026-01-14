package com.akshay.websockettask.util;

import com.akshay.websockettask.DTO.Event;
import com.akshay.websockettask.event.WsInternalEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketEventUtil {

    private final ApplicationEventPublisher publisher;

    public <T> void publish(String topic, Event<T> event) {
        publisher.publishEvent(
                new WsInternalEvent<>(topic, event)
        );
    }
}
