package com.vreijsen.quizzer.messaging.message;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ObserverJoinMessage implements Message {

    MessageType type = MessageType.OBSERVER_JOIN;
}
