package ru.isador.games.seabattle.web.game;

import java.util.List;

public class Ship {

    private final List<Deck> decks;

    public Ship(Deck d) {
        this.decks = List.of(d);
    }

    public Ship(List<Deck> decks) {
        this.decks = decks;
    }

    public List<Deck> getDecks() {
        return decks;
    }

    public boolean isAlive() {
        boolean alive = false;
        for (Deck deck : decks) {
            if (deck.isAlive()) {
                alive = true;
                break;
            }
        }
        return alive;
    }
}
