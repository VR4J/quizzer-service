package com.vreijsen.quizzer.messaging.message;

import com.vreijsen.quizzer.quiz.player.Player;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;
import java.util.List;

@Value
@Builder
@Jacksonized
public class QuestionMessage implements Message {

    MessageType type = MessageType.QUESTION;

    String question;
    String image;
    boolean blurred;
    List<Player> players;
    List<String> answers;
    Instant deadline;

}
