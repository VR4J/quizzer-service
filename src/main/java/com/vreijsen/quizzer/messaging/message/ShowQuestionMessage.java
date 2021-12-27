package com.vreijsen.quizzer.messaging.message;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ShowQuestionMessage implements Message {

    MessageType type = MessageType.SHOW_QUESTION;
}
