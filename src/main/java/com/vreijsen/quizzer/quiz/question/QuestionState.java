package com.vreijsen.quizzer.quiz.question;

import com.vreijsen.quizzer.quiz.player.PlayerResponse;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Component
public class QuestionState {

    private Question question;
    private List<PlayerResponse> responses = new ArrayList<>();

    public void setQuestion(Question question) {
        this.question = question;
        this.responses = new ArrayList<>();
    }
    public void setResponse(PlayerResponse response) {
        responses.removeIf(existing -> existing.getId().equals(response.getId()));
        responses.add(response);
    }

    public List<PlayerResponse> getCorrectResponses() {
        if(question.getOptions().size() == 1) {
            return responses.stream()
                    .filter(response -> isCorrectOpenQuestionAnswer(response.getAnswer(), question.getAnswer()))
                    .collect(Collectors.toList());
        }

        return responses.stream()
                .filter(response -> response.getAnswer().equals(question.getAnswer()))
                .collect(Collectors.toList());
    }

    public List<PlayerResponse> getWrongResponses() {
        if(question.getOptions().size() == 1) {
            return responses.stream()
                    .filter(response -> ! isCorrectOpenQuestionAnswer(response.getAnswer(), question.getAnswer()))
                    .collect(Collectors.toList());
        }

        return responses.stream()
                .filter(response -> ! response.getAnswer().equals(question.getAnswer()))
                .collect(Collectors.toList());
    }

    private boolean isCorrectOpenQuestionAnswer(String givenAnswer, String actualAnswer) {
        String given = strip(givenAnswer);
        String actual = strip(actualAnswer);

        if(actual.length() < 5) return given.equals(actual);

        List<String> givenCharArray = Arrays.asList(given.split(""));
        List<String> actualCharArray = Arrays.asList(actual.split(""));

        // correct_answer contains all characters from given answer
        // correct_answer length is equal, or 1 more, or 1 less.
        return actualCharArray.containsAll(givenCharArray)
                && (actual.length() == given.length() || actual.length() == given.length() - 1 || actual.length() == given.length() + 1);
    }

    private String strip(String value) {
        return value.toLowerCase().replaceAll("\\s", "");
    }
}
