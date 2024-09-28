package ru.isador.games.seabattle.domain.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "PLAYERS")
public class Player implements Serializable {

    @EmbeddedId
    private PlayerId id;

    @Column(name = "FIELD")
    private String field;

    @Column(name = "WINNER")
    private Boolean winner;

    public Player() {
    }

    public Player(UUID gameId, String userName, String field) {
        id = new PlayerId(gameId, userName);
        this.field = field;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        return Objects.equals(id, player.id);
    }

    public String getField() {
        return field;
    }

    public String getPlayerName() {
        return id.getPlayerName();
    }

    public Boolean getWinner() {
        return winner;
    }

    public void setWinner(Boolean winner) {
        this.winner = winner;
    }
}
