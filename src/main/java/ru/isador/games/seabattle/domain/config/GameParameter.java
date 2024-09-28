package ru.isador.games.seabattle.domain.config;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "GAME_CONFIG_PARAMS")
public class GameParameter implements Serializable {

    @EmbeddedId
    private GameParameterId id;

    @Column(name = "VALUE")
    private String value;

    public GameParameter(UUID configId, String name, String value) {
        id = new GameParameterId(configId, name);
        this.value = value;
    }

    public GameParameter() {
    }

    public GameParameterId getId() {
        return id;
    }

    public void setId(GameParameterId id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
