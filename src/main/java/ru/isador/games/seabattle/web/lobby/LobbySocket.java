package ru.isador.games.seabattle.web.lobby;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.vertx.ConsumeEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import ru.isador.games.seabattle.domain.game.GamesGrid;
import ru.isador.games.seabattle.services.commands.ResponseGameChanged;
import ru.isador.games.seabattle.services.game.GameService;

@ServerEndpoint("/lobby/games")
@ApplicationScoped
public class LobbySocket implements Serializable {

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private GameService gameService;

    @OnOpen
    public void onOpen(Session session) {
        sessions.put(session.getId(), session);
        executor.submit(() -> {
            try {
                session.getAsyncRemote().sendText(toJson(gameService.getWaitingGames()));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String toJson(Object o) {
        ObjectMapper m = new ObjectMapper();
        m.registerModule(new JavaTimeModule());
        try {
            return m.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session.getId());
    }

    @ConsumeEvent("gameBus")
    public void gameChanged(GamesGrid game) {
        switch (game.getStatus()) {
            case WAITING -> sessions.values()
                                .forEach(s -> s.getAsyncRemote().sendText(toJson(
                                    new ResponseGameChanged(game.getCreateTime(), game.getPlayers(), game.getConfigName(),
                                        game.getStatus().toString(), game.getId().toString()))));
            case PLAYING -> sessions.values()
                                .forEach(s -> s.getAsyncRemote().sendText(toJson(
                                    new ResponseGameChanged(game.getCreateTime(), game.getPlayers(), game.getConfigName(),
                                        game.getStatus().toString(), game.getId().toString()))));
            case FAILED, FINISHED -> sessions.values()
                                         .forEach(s -> s.getAsyncRemote().sendText(toJson(
                                             new ResponseGameChanged(game.getCreateTime(), game.getPlayers(), game.getConfigName(),
                                                 game.getStatus().toString(), game.getId().toString()))));
        }
    }

    @Inject
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }
}
