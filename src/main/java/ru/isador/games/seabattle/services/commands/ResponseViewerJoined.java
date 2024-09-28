package ru.isador.games.seabattle.services.commands;

import java.util.Map;

public class ResponseViewerJoined extends Response {

    private final String viewerName;
    private final Player player1;
    private final Player player2;

    public ResponseViewerJoined(String viewerName, Player player1, Player player2) {
        super(ResponseType.VIEWER_JOINED);
        this.player1 = player1;
        this.player2 = player2;
        this.viewerName = viewerName;
    }

    public ResponseViewerJoined(String viewerName, Player player1) {
        super(ResponseType.VIEWER_JOINED);
        this.player1 = player1;
        this.player2 = null;
        this.viewerName = viewerName;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public String getViewerName() {
        return viewerName;
    }

    public record Player(String name, char[][] matrix, Map<Integer, Integer> ships) {
    }
}
