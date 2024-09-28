package ru.isador.games.seabattle.domain.config;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class GameParameterId implements Serializable {

    @Column(name = "config_id")
    private UUID configId;

    @Column(name = "NAME")
    private String name;

    public GameParameterId(UUID configId, String name) {
        this.configId = configId;
        this.name = name;
    }

    public GameParameterId() {
    }

    public UUID getConfigId() {
        return configId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameParameterId that = (GameParameterId) o;
        return Objects.equals(configId, that.configId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configId, name);
    }
}
