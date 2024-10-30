package ru.isador.games.seabattle.web.game;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import ru.isador.games.seabattle.domain.game.Game;
import ru.isador.games.seabattle.domain.game.GameStatus;
import ru.isador.games.seabattle.domain.game.GamesGrid;
import ru.isador.games.seabattle.domain.player.Player;
import ru.isador.games.seabattle.services.commands.Command;
import ru.isador.games.seabattle.services.commands.CommandChatMessage;
import ru.isador.games.seabattle.services.commands.CommandFire;
import ru.isador.games.seabattle.services.commands.CommandType;
import ru.isador.games.seabattle.services.commands.ResponseChatMessage;
import ru.isador.games.seabattle.services.commands.ResponseError;
import ru.isador.games.seabattle.services.commands.ResponseFire;
import ru.isador.games.seabattle.services.commands.ResponseGameFinished;
import ru.isador.games.seabattle.services.commands.ResponsePlayerJoined;
import ru.isador.games.seabattle.services.commands.ResponsePlayerLeaved;
import ru.isador.games.seabattle.services.commands.ResponseStartGame;
import ru.isador.games.seabattle.services.commands.ResponseViewerJoined;
import ru.isador.games.seabattle.services.game.GameService;

@ServerEndpoint("/game/{id}")
@ApplicationScoped
public class GameWebSocket {

    // gameId - List<Session>
    private final Map<String, List<Session>> sessions = new ConcurrentHashMap<>();

    // sessionId - playerName
    private final Map<String, String> playerSessions = new HashMap<>();

    // sessionId - viewerName
    private final Map<String, String> viewerSessions = new HashMap<>();

    // gameId = gameWrapper
    private final Map<String, GameWrapper> activeGames = new ConcurrentHashMap<>();

    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final ObjectMapper m = new ObjectMapper();
    private GameService gameService;
    private EventBus eventBus;

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String gameId) throws ExecutionException, InterruptedException {
        if (!sessions.containsKey(gameId)) {
            sessions.put(gameId, new ArrayList<>());
        }
        sessions.get(gameId).add(session);

        // Создается новая игра
        if (!activeGames.containsKey(gameId)) {
            Game g = executor.submit(() -> gameService.getGame(UUID.fromString(gameId)))
                             .get();
            GameWrapper gw = new GameWrapper(g);
            activeGames.put(gameId, gw);
            PlayerWrapper pw = gw.getFirstPlayer();
            if (pw == null) {
                // Произошло непотребство => удаляем игру
                gw.getGame().setStatus(GameStatus.FAILED);
                gameService.removeGame(gw.getGame().getId());
                session.getAsyncRemote().sendText(toJson(new ResponseError("Произошло непотребство. Игра недоступна")));
                return;
            }
            playerSessions.put(session.getId(), pw.getPlayer().getPlayerName());

            broadcastToGame(gameId, new ResponsePlayerJoined(pw.getPlayer().getPlayerName(), pw.getSquadron().getAliveShips()));

            // Кто-то присоединился к игре
        } else {
            GameWrapper gw = activeGames.get(gameId);

            // Присоединяется соперник
            if (session.getRequestParameterMap().get("playerName") != null) {

                // Игра еще не началась => присоединился соперник
                if (!gw.canStartGame()) {
                    String joiningPlayerName = session.getRequestParameterMap().get("playerName").getFirst();
                    String joiningPlayerField = session.getRequestParameterMap().get("field").getFirst();

                    // Присоединяется игрок, которого нет в игре
                    if (gw.getPlayer(joiningPlayerName) == null) {
                        Player p = executor.submit(() -> gameService.joinGame(UUID.fromString(gameId), joiningPlayerName, joiningPlayerField)).get();
                        PlayerWrapper pw = gw.addPlayer(p, fromJson(p.getField()));

                        playerSessions.put(session.getId(), p.getPlayerName());
                        gw.getGame().setStatus(GameStatus.PLAYING);
                        eventBus.publish("gameBus",
                            new GamesGrid(gw.getGame().getId(), gw.getGame().getCreateTime(), gw.getGame().getStatus(), gw.getPlayersString()));
                        broadcastToGame(gameId, new ResponsePlayerJoined(pw.getPlayer().getPlayerName(), pw.getSquadron().getAliveShips()));

                        // Запускаем игру
                        broadcastToGame(gameId, new ResponseStartGame(gw.getOtherPlayer(pw.getPlayer().getPlayerName()).getPlayer().getPlayerName(),
                            gw.getOtherPlayer(pw.getPlayer().getPlayerName()).getPlayer().getPlayerName(),
                            pw.getPlayer().getPlayerName(), gameId));
                    } else {
                        // Произошло непотребство - кидаем ошибку
                        session.getAsyncRemote().sendText(toJson(new ResponseError("Нельзя присоединиться к своей игре вторым игроком")));
                    }

                    // Возвращаем не ок => кто-то присоединился раньше
                } else {
                    session.getAsyncRemote().sendText(toJson(new ResponseError("Кто-то присоединился раньше.")));
                }
                return;
            }

            // Присоединился зритель
            if (session.getRequestParameterMap().get("viewerName") != null) {
                String viewerName = session.getRequestParameterMap().get("viewerName").getFirst();
                viewerSessions.put(session.getId(), viewerName);

                // Вернуть зрителю статистику по игре
                List<ResponseViewerJoined.Player> players = gw.getSortedPlayerNames().stream()
                                                                .map(gw::getPlayer)
                                                                .map(p -> new ResponseViewerJoined.Player(p.getPlayer().getPlayerName(),
                                                                    p.getSquadron().getMatrix(), p.getSquadron().getAliveShips()))
                                                                .toList();
                if (players.size() > 1) {
                    if(gw.getFirstFireTime() != null) {
                        broadcastToGame(gameId, new ResponseViewerJoined(viewerName, players.get(0), players.get(1), gw.getRemainingTime()));
                    } else {
                        broadcastToGame(gameId, new ResponseViewerJoined(viewerName, players.get(0), players.get(1), 0L));
                    }
                } else {
                    broadcastToGame(gameId, new ResponseViewerJoined(viewerName, players.get(0)));
                }
            }
        }
    }

    private int[][] fromJson(String str) {
        try {
            return m.readValue(str, int[][].class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private void broadcastToGame(String gameId, Object msg) {
        List<Session> gameSessions = sessions.get(gameId);
        if (gameSessions != null) {
            gameSessions.forEach(s -> s.getAsyncRemote().sendText(toJson(msg)));
        }
    }

    private String toJson(Object o) {
        try {
            return m.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @OnMessage
    public void onMessage(Session session, @PathParam("id") String gameId, String message) {
        CommandType type = CommandType.getType(message);

        if (type == null) {
            return;
        }

        GameWrapper gw = activeGames.get(gameId);
        if (gw == null) {
            return;
        }

        switch (type) {
            case FIRE -> {
                CommandFire cmd = fromJson(gw, message, CommandFire.class);
                String attacker = playerSessions.get(session.getId());
                if (gw.getTurn().equals(attacker)) {

                    // Время игры вышло
                    if (gw.getFirstFireTime() != null
                        && LocalDateTime.now().isAfter(gw.getFirstFireTime().plus(gw.getGame().getConfig().getGameDuration()))) {
                        List<ResponseGameFinished.Player> players = gw.getSortedPlayerNames().stream()
                                                                        .map(gw::getPlayer)
                                                                        .map(p -> new ResponseGameFinished.Player(p.getPlayer().getPlayerName(),
                                                                            p.getSquadron().getMatrix(), p.getPlayer().getField(), p.getSquadron()
                                                                                                                                    .getAliveShips()))
                                                                        .toList();

                        byte attackerHints = gw.getPlayer(attacker).getSquadron().getHitCount();
                        byte opponentHints = gw.getOtherPlayer(attacker).getSquadron().getHitCount();
                        if (attackerHints > opponentHints) {
                            broadcastToGame(gameId,
                                new ResponseGameFinished(gw.getOtherPlayer(attacker).getPlayer().getPlayerName(), gameId, players.get(0),
                                    players.get(1)));
                        } else if (opponentHints > attackerHints) {
                            broadcastToGame(gameId,
                                new ResponseGameFinished(attacker, gameId, players.get(0),
                                    players.get(1)));
                        } else {
                            broadcastToGame(gameId, new ResponseGameFinished(null, gameId, players.get(0), players.get(1)));
                        }

                        eventBus.publish("gameBus",
                            new GamesGrid(gw.getGame().getId(), gw.getGame().getCreateTime(), gw.getGame().getStatus(), gw.getPlayersString()));

                        gw.setFinished(true);
                        gw.getGame().setStatus(GameStatus.FINISHED);
                        gameService.finishGame(gameId, gw.getTurn(), gw.getCommands());
                        return;
                    }
                    PlayerWrapper opponent = gw.getOtherPlayer(attacker);
                    Squadron s = opponent.getSquadron();
                    FireResult fr = s.fire(cmd.getX(), cmd.getY());

                    broadcastToGame(gameId,
                        new ResponseFire(attacker, opponent.getPlayer().getPlayerName(), cmd.getX(), cmd.getY(), fr, s.getMatrix(),
                            s.getAliveShips(),
                            gw.getFirstFireTime() == null ? gw.getGame().getConfig().getGameDuration().get(ChronoUnit.SECONDS) : 0));

                    // Запоминаем время первой атаки
                    if (gw.getFirstFireTime() == null) {
                        gw.setFirstFireTime(LocalDateTime.now());
                    }

                    if (fr.equals(FireResult.MISSED)) {
                        gw.setTurn(opponent.getPlayer().getPlayerName());
                    }

                    if (fr.equals(FireResult.ALL_KILLED)) {
                        gw.setFinished(true);
                        gw.getGame().setStatus(GameStatus.FINISHED);
                        gameService.finishGame(gameId, gw.getTurn(), gw.getCommands());
                        PlayerWrapper pw = gw.getPlayer(attacker);

                        List<ResponseGameFinished.Player> players = gw.getSortedPlayerNames().stream()
                                                                        .map(gw::getPlayer)
                                                                        .map(p -> new ResponseGameFinished.Player(p.getPlayer().getPlayerName(),
                                                                            p.getSquadron().getMatrix(), p.getPlayer().getField(), p.getSquadron()
                                                                                                                                    .getAliveShips()))
                                                                        .toList();
                        broadcastToGame(gameId, new ResponseGameFinished(pw.getPlayer().getPlayerName(), gameId, players.get(0), players.get(1)));
                        eventBus.publish("gameBus",
                            new GamesGrid(gw.getGame().getId(), gw.getGame().getCreateTime(), gw.getGame().getStatus(), gw.getPlayersString()));
                    }
                }
            }

            case CHAT_MESSAGE -> {
                CommandChatMessage cmd = fromJson(null, message, CommandChatMessage.class);
                String username = playerSessions.get(session.getId());
                if (username == null) {
                    username = viewerSessions.get(session.getId());
                }
                broadcastToGame(gameId, new ResponseChatMessage(username, cmd.getMessage()));
            }
        }
    }

    private <T extends Command> T fromJson(GameWrapper gw, String str, Class<T> cmdClass) {
        try {
            T t = m.readValue(str, cmdClass);
            if (gw != null) {
                gw.addCommand(str);
            }
            return t;
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("id") String gameId) {

        // Кто-то вышел из игры
        if (sessions.containsKey(gameId)) {

            // Вышел зритель
            if (viewerSessions.containsKey(session.getId())) {
                // Просто выкидываем его из мап
                sessions.get(gameId).remove(session);

                // Чистим мапу с сессиями
                if (sessions.get(gameId).isEmpty()) {
                    sessions.remove(gameId);
                }

                viewerSessions.remove(session.getId());
                return;
            }

            // Вышел игрок
            if (playerSessions.containsKey(session.getId())) {

                // Выкидываем его из мап
                String leavedPlayerName = playerSessions.remove(session.getId());
                sessions.get(gameId).remove(session);

                broadcastToGame(gameId, new ResponsePlayerLeaved(leavedPlayerName));

                // Чистим мапу с сессиями
                if (sessions.get(gameId).isEmpty()) {
                    sessions.remove(gameId);
                }

                GameWrapper gw = activeGames.get(gameId);

                // Если игра есть, и вышел один из игроков
                if (gw != null && gw.getPlayer(leavedPlayerName) != null) {
                    activeGames.remove(gameId);

                    // Если игра не была начата
                    if (!gw.canStartGame()) {
                        // Просто ставим ее в FAILED
                        gw.getGame().setStatus(GameStatus.FAILED);
                        gameService.removeGame(gw.getGame().getId());

                        // Если была начата игра, но не окончена, получается один игрок ливнул
                    } else if (!gw.isFinished()) {
                        List<ResponseGameFinished.Player> players = gw.getSortedPlayerNames().stream()
                                                                        .map(gw::getPlayer)
                                                                        .map(p -> new ResponseGameFinished.Player(p.getPlayer().getPlayerName(),
                                                                            p.getSquadron().getMatrix(), p.getPlayer().getField(), p.getSquadron()
                                                                                                                                    .getAliveShips()))
                                                                        .toList();
                        broadcastToGame(gameId,
                            new ResponseGameFinished(gw.getOtherPlayer(leavedPlayerName).getPlayer().getPlayerName(), gameId, players.get(0),
                                players.get(1)));

                        gw.removePlayer(leavedPlayerName);
                        gw.addCommand("{\"type\": \"PLAYER_LEAVED\", \"playerName\": \"" + leavedPlayerName + "\"}");
                        gw.getGame().setStatus(GameStatus.FINISHED);

                        gameService.finishGame(gameId, gw.getFirstPlayer().getPlayer().getPlayerName(), gw.getCommands());
                    }
                    eventBus.publish("gameBus",
                        new GamesGrid(gw.getGame().getId(), gw.getGame().getCreateTime(), gw.getGame().getStatus(), gw.getPlayersString()));
                }
            }
        }
    }

    @Inject
    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Inject
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }
}
