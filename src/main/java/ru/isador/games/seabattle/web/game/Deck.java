package ru.isador.games.seabattle.web.game;

import java.util.List;

public class Deck {

    private final byte x;
    private final byte y;
    boolean alive;

    public Deck(byte x, byte y) {
        this.x = x;
        this.y = y;
        alive = true;
    }

    public List<Tuple> getWrappingCells() {
        return List.of(new Tuple((byte) (x - 1), y),
            new Tuple((byte) (x - 1), (byte) (y + 1)),
            new Tuple(x, (byte) (y + 1)),
            new Tuple((byte) (x + 1), (byte) (y + 1)),
            new Tuple((byte) (x + 1), y),
            new Tuple((byte) (x + 1), (byte) (y - 1)),
            new Tuple(x, (byte) (y - 1)),
            new Tuple((byte) (x - 1), (byte) (y - 1)));
    }

    public byte getX() {
        return x;
    }

    public byte getY() {
        return y;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Tuple getTuple() {
        return new Tuple(x, y);
    }
}
