package ru.isador.games.seabattle.web.game;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.isador.games.seabattle.domain.game.Game;
import ru.isador.games.seabattle.domain.log.GameLog;
import ru.isador.games.seabattle.domain.player.Player;

public class GameWrapper {

    private final Game game;
    private final Map<String, PlayerWrapper> players;
    private final ObjectMapper m = new ObjectMapper();
    private final List<GameLog> commands = new ArrayList<>();
    private String turn;
    private boolean finished = false;
    private LocalDateTime firstFireTime;

    public GameWrapper(Game game) {
        this.game = game;
        players = new LinkedHashMap<>();
        Player p = game.getPlayers().stream()
                       .findFirst().orElse(null);
        players.put(p.getPlayerName(), new PlayerWrapper(p, fromJson(p.getField())));
        turn = p.getPlayerName();
    }

    private int[][] fromJson(String str) {
        try {
            return m.readValue(str, int[][].class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public PlayerWrapper addPlayer(Player player, int[][] field) {
        PlayerWrapper pw = new PlayerWrapper(player, field);
        players.put(player.getPlayerName(), pw);
        return pw;
    }

    public void removePlayer(String playerName) {
        players.remove(playerName);
    }

    public boolean canStartGame() {
        return players.size() == 2;
    }

    public PlayerWrapper getPlayer(String name) {
        return players.get(name);
    }

    public PlayerWrapper getOtherPlayer(String name) {
        return players.entrySet().stream()
                   .filter(e -> !e.getKey().equals(name))
                   .map(Map.Entry::getValue)
                   .findAny().orElse(null);
    }

    public void addCommand(String cmdStr) {
        commands.add(new GameLog(game.getId(), cmdStr));
    }

    public List<GameLog> getCommands() {
        return commands;
    }

    public PlayerWrapper getFirstPlayer() {
        return players.keySet().stream()
                   .map(players::get)
                   .findFirst()
                   .orElse(null);
    }

    public Game getGame() {
        return game;
    }

    public String getPlayersString() {
        return String.join(" vs ", players.keySet());
    }

    public long getRemainingTime() {
        if (firstFireTime == null) {
            return game.getConfig().getGameDuration().get(ChronoUnit.SECONDS);
        }

        long gameTime = Duration.between(getFirstFireTime(), LocalDateTime.now()).get(ChronoUnit.SECONDS);
        return game.getConfig().getGameDuration().get(ChronoUnit.SECONDS) - gameTime;
    }

    public LocalDateTime getFirstFireTime() {
        return firstFireTime;
    }

    public void setFirstFireTime(LocalDateTime firstFireTime) {
        this.firstFireTime = firstFireTime;
    }

    public List<String> getSortedPlayerNames() {
        return players.keySet().stream()
                   .toList();
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
