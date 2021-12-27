package com.vreijsen.quizzer.messaging.message;

import com.vreijsen.quizzer.quiz.player.Player;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class LeaderboardMessage implements Message {

    List<Player> players;
    MessageType type = MessageType.LEADERBOARD;
}
