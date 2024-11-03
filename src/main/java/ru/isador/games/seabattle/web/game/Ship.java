package ru.isador.games.seabattle.web.game;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    public Collection<Tuple> getWrappingPoints() {
        List<Tuple> shipCoords = decks.stream()
                                     .map(Deck::getTuple)
                                     .toList();
        return decks.stream()
                   .flatMap(d -> d.getWrappingCells().stream())
                   .filter(tuple -> !shipCoords.contains(tuple))
                   .collect(Collectors.toSet());
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
