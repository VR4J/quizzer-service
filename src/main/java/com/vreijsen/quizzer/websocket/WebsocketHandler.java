package com.vreijsen.quizzer.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vreijsen.quizzer.messaging.MessagingService;
import com.vreijsen.quizzer.messaging.message.Message;
import com.vreijsen.quizzer.messaging.message.MessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebsocketHandler implements WebSocketHandler {

    private final ObjectMapper mapper;
    private final MessagingService service;

    private final MessageListener listener;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String sessionId = getSessionId(session.getHandshakeInfo().getUri());

        Flux<WebSocketMessage> messages = service.getMessages(sessionId)
                .map(session::textMessage);

        Flux<WebSocketMessage> reading = session.receive()
                .doOnNext(message -> onMessage(message.getPayloadAsText(), sessionId)) ;

        return session.send(messages).and(reading);
    }

    private void onMessage(String payload, String playerId) {
        try {
            Message message = mapper.readValue(payload, Message.class);
            listener.onMessage(message, playerId);
        } catch (JsonProcessingException ex) {
            log.error("Exception while handling message {}", payload, ex);
        }
    }

    private String getSessionId(URI wsUri) {
        return wsUri.getQuery().split("=")[1];
    }
}
