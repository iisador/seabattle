package ru.isador.games.seabattle.domain.game;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import ru.isador.games.seabattle.domain.config.Config;
import ru.isador.games.seabattle.domain.player.Player;

@Entity
@Table(name = "GAMES")
public class Game implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue()
    private UUID id;

    @Column(name = "CREATE_TSTMP")
    private LocalDateTime createTime;

    @Column(name = "START_TSTMP")
    private LocalDateTime startTime;

    @Column(name = "END_TSTMP")
    private LocalDateTime endTime;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "GAME_ID", referencedColumnName = "ID")
    private Set<Player> players = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CONFIG_ID", referencedColumnName = "ID")
    private Config config;

    public Game(Config config) {
        this.config = config;
        status = GameStatus.WAITING;
        createTime = LocalDateTime.now();
    }

    public Game() {
    }

    public void addPlayer(String playerName, String field) {
        players.add(new Player(id, playerName, field));
    }

    public Player getPlayer(String playerName) {
        for (Player player : players) {
            if (player.getPlayerName().equals(playerName)) {
                return player;
            }
        }

        return null;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public Config getConfig() {
        return config;
    }
}
