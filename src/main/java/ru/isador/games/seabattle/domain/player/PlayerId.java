package ru.isador.games.seabattle.domain.player;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PlayerId implements Serializable {

    @Column(name = "GAME_ID")
    private UUID gameId;

    @Column(name = "PLAYER_NAME")
    private String playerName;

    public PlayerId() {
    }

    public PlayerId(UUID gameId, String playerName) {
        this.gameId = gameId;
        this.playerName = playerName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName, gameId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlayerId playerId = (PlayerId) o;
        return Objects.equals(playerName, playerId.playerName) && Objects.equals(gameId, playerId.gameId);
    }

    public UUID getGameId() {
        return gameId;
    }

    public String getPlayerName() {
        return playerName;
    }
}
