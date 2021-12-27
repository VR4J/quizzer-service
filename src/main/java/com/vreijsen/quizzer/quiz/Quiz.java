package com.vreijsen.quizzer.quiz;

import com.vreijsen.quizzer.exception.UnknownSessionException;
import com.vreijsen.quizzer.messaging.MessagingService;
import com.vreijsen.quizzer.messaging.message.PlayerAnsweredMessage;
import com.vreijsen.quizzer.messaging.message.QuestionEmptyMessage;
import com.vreijsen.quizzer.messaging.message.QuestionMessage;
import com.vreijsen.quizzer.messaging.message.ShowQuestionResultMessage;
import com.vreijsen.quizzer.quiz.observer.Observer;
import com.vreijsen.quizzer.quiz.player.Player;
import com.vreijsen.quizzer.quiz.player.PlayerResponse;
import com.vreijsen.quizzer.quiz.question.QuestionService;
import com.vreijsen.quizzer.quiz.question.QuestionState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class Quiz {

    private final Observer observer;
    private final QuestionState state;
    private final Leaderboard leaderboard;

    private final QuestionService questions;
    private final MessagingService messaging;

    private Map<String, Player> players = new HashMap<>();

    public void register(Player player, String playerId) {
        players.put(playerId, player);

        leaderboard.register(player);
        leaderboard.refresh();
    }

    public void next() {
        questions.getRandomQuestion()
                .ifPresentOrElse(
                        (question) -> {
                            state.setQuestion(question);

                            boolean isMultipleChoice = question.getOptions().size() > 1;

                            QuestionMessage qMessage = QuestionMessage.builder()
                                    .question(question.getQuestion())
                                    .image(question.getImage())
                                    .blurred(question.isBlurred())
                                    .players(new ArrayList<>(players.values()))
                                    .answers(question.getOptions())
                                    .deadline(Instant.now().plusSeconds(isMultipleChoice ? 30 : 45))
                                    .build();

                            observer.send(qMessage);
                            players.forEach((id, player) -> messaging.onNext(qMessage, id));
                        },
                        () -> {
                            QuestionEmptyMessage message = QuestionEmptyMessage.builder()
                                    .build();

                            observer.send(message);
                        }
                    );


    }

    public void answer(String playerId, String answer, Long score) {
        Player player = getPlayerByPlayerId(playerId)
                .orElseThrow(() -> new UnknownSessionException(playerId));

        PlayerResponse response = PlayerResponse.builder()
                .id(player.getId())
                .answer(answer)
                .score(score)
                .build();

        state.setResponse(response);

        PlayerAnsweredMessage answered = PlayerAnsweredMessage.builder()
                .player(player)
                .build();

        log.info("Received responses: {}/{}", state.getResponses().size(), players.size());

        observer.send(answered);

        if(state.getResponses().size() == players.size()) {
            showQuestionResult();
        }
    }

    public void showQuestionResult() {
        ShowQuestionResultMessage message = ShowQuestionResultMessage.builder()
                .answer(state.getQuestion().getAnswer())
                .correct(state.getCorrectResponses())
                .wrong(state.getWrongResponses())
                .build();

        state.getCorrectResponses().forEach(leaderboard::score);
        leaderboard.refresh();

        observer.send(message);
        players.forEach((id, player) -> messaging.onNext(message, id));
    }

    private Optional<Player> getPlayerByPlayerId(String playerId) {
        return Optional.ofNullable(
                players.get(playerId)
        );
    }
}
