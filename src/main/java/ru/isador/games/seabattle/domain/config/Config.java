package ru.isador.games.seabattle.domain.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONFIG")
public class Config implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private UUID configId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PREDEFINED")
    private boolean predefined;

    @Column(name = "ORD")
    private int order;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "CONFIG_ID", referencedColumnName = "ID")
    private List<GameParameter> parameters = new ArrayList<>();

    public Config() {
        predefined = false;
    }

    public void addParameter(String name, String value) {
        parameters.add(new GameParameter(configId, name, value));
    }

    public GameParameter getParameter(String name) {
        return parameters.stream()
                   .filter(p -> p.getId().getName().equals(name))
                   .findFirst().orElse(null);
    }

    public UUID getConfigId() {
        return configId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GameParameter> getParameters() {
        return parameters;
    }

    public boolean isPredefined() {
        return predefined;
    }

    public int getOrder() {
        return order;
    }
}
