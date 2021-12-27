package com.vreijsen.quizzer.messaging.message;

public enum MessageType {
    OBSERVER_JOIN, PLAYER_JOIN, SHOW_QUESTION, SHOW_QUESTION_RESULT, QUESTION,
    QUESTION_EMPTY, ANSWER, SHOW_LEADERBOARD, LEADERBOARD, PLAYER_ANSWERED, TIMEOUT;

    MessageType() { }

    public String toString() {
        return this.name();
    }
}
