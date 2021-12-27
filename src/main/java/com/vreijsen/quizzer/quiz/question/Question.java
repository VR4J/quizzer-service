package com.vreijsen.quizzer.quiz.question;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Question {

    String question;
    String image;
    String answer;
    boolean blurred;
    List<String> options;
}
