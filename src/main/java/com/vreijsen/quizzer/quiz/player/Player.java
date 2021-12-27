package com.vreijsen.quizzer.quiz.player;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Player {

    String id;
    String name;
    int rank;
    long score;
}
