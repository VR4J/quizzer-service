package com.vreijsen.quizzer.quiz;

import com.vreijsen.quizzer.messaging.message.LeaderboardMessage;
import com.vreijsen.quizzer.quiz.observer.Observer;
import com.vreijsen.quizzer.quiz.player.Player;
import com.vreijsen.quizzer.quiz.player.PlayerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class Leaderboard {

    private final Observer observer;

    private List<Player> scoreboard = new ArrayList<>();

    public void register(Player player) {
        scoreboard.stream()
                .filter(existing -> existing.getId().equals(player.getId()))
                .findFirst()
                .ifPresentOrElse(
                        existing -> {
                            Player replacement = existing.toBuilder()
                                    .name(player.getName())
                                    .build();

                            scoreboard.remove(existing);
                            scoreboard.add(replacement);
                        },
                        () -> scoreboard.add(player)
            );
    }

    public void refresh() {
        this.scoreboard.sort(
                Comparator.comparing(Player::getScore).reversed()
                        .thenComparing(Player::getName)
        );

        List<Player> reordered = new ArrayList<>();

        for (int i = 0; i < this.scoreboard.size(); i++) {
            Player player = this.scoreboard.get(i);

            player = player.toBuilder()
                        .rank(i + 1)
                        .build();

            reordered.add(player);
        }

        this.scoreboard = reordered;
    }

    public void show() {
        LeaderboardMessage message = LeaderboardMessage.builder()
                .players(this.scoreboard)
                .build();

        observer.send(message);
    }

    public void score(PlayerResponse response) {
        this.scoreboard = this.scoreboard.stream()
                .map(player -> {
                    if(player.getId().equals(response.getId())) {
                        return player.toBuilder()
                                .score(player.getScore() + response.getScore())
                                .build();
                    }

                    return player;
                })
                .collect(Collectors.toList());
    }
}
