package ru.isador.games.seabattle.domain.log;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "GAME_LOG")
public class GameLog implements Serializable {

    @EmbeddedId
    private GameLogId id;

    @Column(name = "COMMAND")
    private String command;

    public GameLog() {
    }

    public GameLog(UUID gameId, String command) {
        id = new GameLogId(gameId, LocalDateTime.now());
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public GameLogId getId() {
        return id;
    }

    public void setId(GameLogId id) {
        this.id = id;
    }
}
