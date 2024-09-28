package ru.isador.games.seabattle.web.game;

import java.util.ArrayList;
import java.util.List;

import ru.isador.games.seabattle.domain.player.Player;

public class PlayerWrapper {

    private final Player player;
    private final Squadron squadron;


    public PlayerWrapper(Player player, int[][] field) {
        this.player = player;
        squadron = constructSquadron(field);
    }

    private Squadron constructSquadron(int[][] field) {
        Squadron s = new Squadron(field.length);
        int[][] marks = new int[field.length][field.length];

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if (field[i][j] > 0) {
                    if (field[i][j] == 1) {
                        s.addShip(new Ship(new Deck(i, j)));
                    } else {
                        int deckCount = field[i][j];
                        List<Deck> decks = new ArrayList<>();
                        if ((j + 1 < field.length) && (field[i][j + 1] > 0)) {
                            for (int k = 0; k < deckCount; k++) {
                                decks.add(new Deck(i, j + k));
                            }
                            j += deckCount - 1;
                        } else if ((i + 1 < field.length) && (marks[i + 1][j] != -1) && (field[i + 1][j] > 0)) {
                            for (int k = 0; k < deckCount; k++) {
                                decks.add(new Deck(i + k, j));
                                marks[i + k][j] = -1;
                            }
                        }
                        if (!decks.isEmpty()) {
                            s.addShip(new Ship(decks));
                        }
                    }
                }
            }
        }
        return s;
    }

    public Player getPlayer() {
        return player;
    }

    public Squadron getSquadron() {
        return squadron;
    }
}
