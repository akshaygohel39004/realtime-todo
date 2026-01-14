package com.akshay.websockettask.event;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessagingTemplate template;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public <T> void handle(WsInternalEvent<T> internalEvent) {

        template.convertAndSend(
                internalEvent.getTopic(),
                internalEvent.getEvent() // ONLY Event<T>
        );
    }
}
