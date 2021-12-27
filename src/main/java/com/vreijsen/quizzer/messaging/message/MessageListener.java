package com.vreijsen.quizzer.messaging.message;

import com.vreijsen.quizzer.quiz.Leaderboard;
import com.vreijsen.quizzer.quiz.Quiz;
import com.vreijsen.quizzer.quiz.observer.Observer;
import com.vreijsen.quizzer.quiz.player.Player;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener {

    private final Quiz quiz;
    private final Leaderboard leaderboard;

    private final Observer observer;

    private Boolean isTimeOut = false;

    public void onMessage(Message wsMessage, String sessionId) {
        log.info("Message received: {}", wsMessage);
        MessageType type = wsMessage.getType();

        switch (type) {
            case OBSERVER_JOIN -> {
                observer.setSessionId(sessionId);
            }
            case PLAYER_JOIN -> {
                PlayerJoinMessage message = (PlayerJoinMessage) wsMessage;

                Player player = Player.builder()
                        .id(sessionId)
                        .name(message.getName())
                        .score(0)
                        .build();

                quiz.register(player, sessionId);

                // Also notify the observer of a player join.
                observer.send(message);
            }
            case SHOW_QUESTION -> {
                quiz.next();
                isTimeOut = false;
            }
            case ANSWER -> {
                AnswerMessage message = (AnswerMessage) wsMessage;
                quiz.answer(sessionId, message.getAnswer(), message.getScore());
            }
            case SHOW_LEADERBOARD -> {
                leaderboard.show();
            }
            case TIMEOUT -> {
                if(! isTimeOut) {
                    quiz.showQuestionResult();
                    isTimeOut = true;
                }
            }
        }
    }
}
