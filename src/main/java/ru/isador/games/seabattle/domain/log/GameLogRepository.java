package ru.isador.games.seabattle.domain.log;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GameLogRepository implements PanacheRepository<GameLog> {
}
