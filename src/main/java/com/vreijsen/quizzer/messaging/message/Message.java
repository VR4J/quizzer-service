package com.vreijsen.quizzer.messaging.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AnswerMessage.class, name = "ANSWER"),
        @JsonSubTypes.Type(value = ObserverJoinMessage.class, name = "OBSERVER_JOIN"),
        @JsonSubTypes.Type(value = PlayerJoinMessage.class, name = "PLAYER_JOIN"),
        @JsonSubTypes.Type(value = QuestionMessage.class, name = "QUESTION"),
        @JsonSubTypes.Type(value = ShowQuestionMessage.class, name = "SHOW_QUESTION"),
        @JsonSubTypes.Type(value = ShowQuestionResultMessage.class, name = "SHOW_QUESTION_RESULT"),
        @JsonSubTypes.Type(value = LeaderboardMessage.class, name = "LEADERBOARD"),
        @JsonSubTypes.Type(value = ShowLeaderboardMessage.class, name = "SHOW_LEADERBOARD"),
        @JsonSubTypes.Type(value = PlayerAnsweredMessage.class, name = "PLAYER_ANSWERED"),
        @JsonSubTypes.Type(value = TimeoutMessage.class, name = "TIMEOUT"),
        @JsonSubTypes.Type(value = QuestionEmptyMessage.class, name = "QUESTION_EMPTY")
})
public interface Message {

    MessageType getType();
}
