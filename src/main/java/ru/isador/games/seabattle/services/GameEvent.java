package ru.isador.games.seabattle.services;

import java.io.Serializable;

import ru.isador.games.seabattle.domain.game.GamesGrid;

public class GameEvent implements Serializable {

    private GamesGrid gg;
    private String gameId;
    private String gameStatus;

    public GamesGrid getGg() {
        return gg;
    }

    public void setGg(GamesGrid gg) {
        this.gg = gg;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }
}
