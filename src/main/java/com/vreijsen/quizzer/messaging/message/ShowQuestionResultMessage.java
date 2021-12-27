package com.vreijsen.quizzer.messaging.message;

import com.vreijsen.quizzer.quiz.player.Player;
import com.vreijsen.quizzer.quiz.player.PlayerResponse;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class ShowQuestionResultMessage implements Message {


    String answer;
    Player fastest;

    List<PlayerResponse> correct;
    List<PlayerResponse> wrong;

    MessageType type = MessageType.SHOW_QUESTION_RESULT;
}
