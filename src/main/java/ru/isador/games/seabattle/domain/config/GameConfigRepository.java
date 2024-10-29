package ru.isador.games.seabattle.domain.config;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GameConfigRepository implements PanacheRepository<Config> {

    public List<Config> listPredefined() {
        return list("predefined", Sort.ascending("order"));
    }

    public Optional<Config> findById(UUID id) {
        return find("id", id).firstResultOptional();
    }
}
