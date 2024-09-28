package ru.isador.games.seabattle.domain.config;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GameConfigRepository implements PanacheRepository<Config> {

    public List<Config> listPredefined() {
        return list("predefined", Sort.ascending("order"));
    }
}
