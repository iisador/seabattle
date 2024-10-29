package ru.isador.games.seabattle.services.game;

import java.util.UUID;

public class NewGameException extends Exception {

    public NewGameException(UUID message) {
        super(message.toString(), null, false, false);
    }
}
