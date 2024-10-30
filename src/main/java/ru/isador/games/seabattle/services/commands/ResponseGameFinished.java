package ru.isador.games.seabattle.services.commands;

import java.util.Map;

public class ResponseGameFinished extends Response {

    private final String winner;
    private final String gameId;
    private final Player player1;
    private final Player player2;

    public ResponseGameFinished(String winner, String gameId, Player player1, Player player2) {
        super(ResponseType.GAME_FINISHED);
        this.winner = winner;
        this.gameId = gameId;
        this.player1 = player1;
        this.player2 = player2;
    }

    public String getGameId() {
        return gameId;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public String getWinner() {
        return winner;
    }

    public record Player(String name, char[][] matrix, String field, Map<Byte, Byte> ships) {
    }
}
