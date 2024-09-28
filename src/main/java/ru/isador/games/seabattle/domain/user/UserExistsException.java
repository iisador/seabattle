package ru.isador.games.seabattle.domain.user;

public class UserExistsException extends Throwable {

    private final String playerName;

    public UserExistsException(String playerName) {
        super(null, null, false, false);
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
