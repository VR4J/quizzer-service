package com.vreijsen.quizzer.exception;

public class UnknownSessionException extends RuntimeException {

    public UnknownSessionException(String playerId) {
        super(String.format("Unknown player with id [%s].", playerId));
    }
}
