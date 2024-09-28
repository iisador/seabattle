package ru.isador.games.seabattle.web.game;

import ru.isador.games.seabattle.services.GameConfigResource;

public record NewGameResource(GameConfigResource config, String field) {
}
