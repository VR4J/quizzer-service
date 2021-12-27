package com.vreijsen.quizzer.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vreijsen.quizzer.messaging.message.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessagingService {

    private final ObjectMapper mapper;

    private static final Map<String, Sinks.Many<String>> sinks = new HashMap<>();

    public void onNext(Message next, String session) {
        if(! sinks.containsKey(session)) return;

        try {
            String payload = mapper.writeValueAsString(next);
            sinks.get(session).emitNext(payload, Sinks.EmitFailureHandler.FAIL_FAST);
        } catch (JsonProcessingException e) {
            log.error("Unable to send message {} to session {}", next, session, e);
        }
    }

    public Flux<String> getMessages(String session) {
        sinks.putIfAbsent(session, Sinks.many().multicast().onBackpressureBuffer());
        return sinks.get(session).asFlux();
    }
}
