package ru.isador.games.seabattle.services.commands;

public class ResponseStartGame extends Response {

    private String turn;
    private String player1;
    private String player2;
    private String gameId;

    public ResponseStartGame(String turn, String player1, String player2, String gameId) {
        super(ResponseType.START_GAME);
        this.turn = turn;
        this.player1 = player1;
        this.player2 = player2;
        this.gameId = gameId;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public String getTurn() {
        return turn;
    }

    public String getGameId() {
        return gameId;
    }
}
