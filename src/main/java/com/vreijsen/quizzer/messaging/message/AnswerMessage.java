package com.vreijsen.quizzer.messaging.message;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class AnswerMessage implements Message {

    String answer;
    Long score;
    MessageType type = MessageType.ANSWER;
}
