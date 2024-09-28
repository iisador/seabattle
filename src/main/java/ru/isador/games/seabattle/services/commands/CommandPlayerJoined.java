package ru.isador.games.seabattle.services.commands;

public class CommandPlayerJoined extends Command {

    private String playerName;
    private String field;

    public String getField() {
        return field;
    }

    public String getPlayerName() {
        return playerName;
    }
}
