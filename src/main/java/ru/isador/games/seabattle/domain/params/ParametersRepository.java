package ru.isador.games.seabattle.domain.params;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ParametersRepository implements PanacheRepository<Parameter> {

}
