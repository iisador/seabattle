package ru.isador.games.seabattle.web.game;

public class Deck {

    private final byte x;
    private final byte y;
    boolean alive;

    public Deck(byte x, byte y) {
        this.x = x;
        this.y = y;
        alive = true;
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
}
