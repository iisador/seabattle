package ru.isador.games.seabattle.domain.log;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class GameLogId implements Serializable {

    @Column(name = "GAME_ID")
    private UUID gameId;

    @Column(name = "TSTMP")
    private LocalDateTime tstmp;

    public GameLogId() {
    }

    public GameLogId(UUID gameId, LocalDateTime tstmp) {
        this.gameId = gameId;
        this.tstmp = tstmp;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public LocalDateTime getTstmp() {
        return tstmp;
    }

    public void setTstmp(LocalDateTime tstmp) {
        this.tstmp = tstmp;
    }
}
