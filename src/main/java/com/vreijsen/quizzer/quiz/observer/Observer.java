package com.vreijsen.quizzer.quiz.observer;

import com.vreijsen.quizzer.messaging.MessagingService;
import com.vreijsen.quizzer.messaging.message.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Observer {

    private final MessagingService messaging;

    String sessionId;

    public void send(Message message) {
        messaging.onNext(message, this.sessionId);
    }

    public boolean isActive() {
        return sessionId != null;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
