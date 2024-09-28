package ru.isador.games.seabattle.services.commands;

import java.io.Serializable;

public class Response implements Serializable {

    private final ResponseType type;

    public Response(ResponseType type) {
        this.type = type;
    }

    public ResponseType getType() {
        return type;
    }
}
