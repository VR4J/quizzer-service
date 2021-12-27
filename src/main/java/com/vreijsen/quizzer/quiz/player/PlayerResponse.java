package com.vreijsen.quizzer.quiz.player;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class PlayerResponse {

    String id;
    String answer;
    Long score;
}
