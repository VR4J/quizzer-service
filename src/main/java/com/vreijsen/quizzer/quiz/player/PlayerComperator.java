package com.vreijsen.quizzer.quiz.player;

import java.util.Comparator;

public class PlayerComperator implements Comparator<Player> {

    @Override
    public int compare(Player left, Player right) {
        int scoreCompare = Long.compare(left.getScore(), right.getScore());

        return scoreCompare == 0
                ? left.getName().compareTo(right.getName())
                : scoreCompare;
    }
}
