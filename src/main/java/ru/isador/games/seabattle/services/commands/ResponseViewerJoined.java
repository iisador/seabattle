package ru.isador.games.seabattle.services.commands;

import java.util.Map;

public class ResponseViewerJoined extends Response {

    private final String viewerName;
    private final Player player1;
    private final Player player2;
    private final long timeRemaining;

    public ResponseViewerJoined(String viewerName, Player player1, Player player2, long timeRemaining) {
        super(ResponseType.VIEWER_JOINED);
        this.player1 = player1;
        this.player2 = player2;
        this.viewerName = viewerName;
        this.timeRemaining = timeRemaining;
    }

    public ResponseViewerJoined(String viewerName, Player player1) {
        super(ResponseType.VIEWER_JOINED);
        this.player1 = player1;
        this.player2 = null;
        this.viewerName = viewerName;
        this.timeRemaining = 0L;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public long getTimeRemaining() {
        return timeRemaining;
    }

    public String getViewerName() {
        return viewerName;
    }

    public record Player(String name, char[][] matrix, Map<Integer, Integer> ships) {
    }
}
