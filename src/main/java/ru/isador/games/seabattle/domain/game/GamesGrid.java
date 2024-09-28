package ru.isador.games.seabattle.domain.game;

import java.time.LocalDateTime;
import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "GAMES_GRID")
public class GamesGrid extends PanacheEntityBase {

    @Id
    @Column(name = "ID")
    private UUID id;

    @Column(name = "CREATE_TSTMP")
    private LocalDateTime createTime;

    @Column
    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Column(name = "PLAYERS")
    private String players;

    @Column(name = "CONFIG_NAME")
    private String configName;

    @Column(name = "CONFIG")
    private String config;

    @Column(name = "WINNER")
    private String winner;

    public GamesGrid() {
    }

    public GamesGrid(UUID id, LocalDateTime createTime, GameStatus status, String players) {
        this.id = id;
        this.createTime = createTime;
        this.status = status;
        this.players = players;
    }

    public String getConfig() {
        return config;
    }

    public String getConfigName() {
        return configName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public UUID getId() {
        return id;
    }

    public String getPlayers() {
        return players;
    }

    public GameStatus getStatus() {
        return status;
    }

    public String getWinner() {
        return winner;
    }
}
