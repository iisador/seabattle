package ru.isador.games.seabattle.services.commands;

public class ResponseError extends Response {

    private final String error;

    public ResponseError(String error) {
        super(ResponseType.ERROR);
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
