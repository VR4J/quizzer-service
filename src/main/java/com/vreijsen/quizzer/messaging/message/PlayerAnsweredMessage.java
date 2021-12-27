package com.vreijsen.quizzer.messaging.message;

import com.vreijsen.quizzer.quiz.player.Player;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class PlayerAnsweredMessage implements Message {

    Player player;
    MessageType type = MessageType.PLAYER_ANSWERED;
}
