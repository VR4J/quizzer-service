package com.vreijsen.quizzer.messaging.message;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class PlayerJoinMessage implements Message {

    String name;
    MessageType type = MessageType.PLAYER_JOIN;
}
