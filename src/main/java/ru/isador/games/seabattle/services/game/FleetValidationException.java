package ru.isador.games.seabattle.services.game;

public class FleetValidationException extends Exception {

    public FleetValidationException(String message) {
        super(message, null, false, false);
    }
}
