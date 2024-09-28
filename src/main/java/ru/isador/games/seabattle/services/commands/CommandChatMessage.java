package ru.isador.games.seabattle.services.commands;

public class CommandChatMessage extends Command {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
