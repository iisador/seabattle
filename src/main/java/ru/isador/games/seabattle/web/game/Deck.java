package ru.isador.games.seabattle.web.game;

public class Deck {

    private final int x;
    private final int y;
    boolean alive;

    public Deck(int x, int y) {
        this.x = x;
        this.y = y;
        alive = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
