package ru.isador.games.seabattle.services.game;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.isador.games.seabattle.domain.config.Config;
import ru.isador.games.seabattle.domain.config.GameConfigRepository;
import ru.isador.games.seabattle.domain.game.Game;
import ru.isador.games.seabattle.domain.game.GameRepository;
import ru.isador.games.seabattle.domain.game.GameStatus;
import ru.isador.games.seabattle.domain.game.GamesGrid;
import ru.isador.games.seabattle.domain.log.GameLog;
import ru.isador.games.seabattle.domain.log.GameLogRepository;
import ru.isador.games.seabattle.domain.player.Player;
import ru.isador.games.seabattle.services.commands.ResponseGameChanged;

@ApplicationScoped
public class GameService {

    private final GameConfigRepository gameConfigRepository;
    private final GameRepository gameRepository;
    private final GameLogRepository gameLogRepository;
    private final FleetValidator fleetValidator;

    @Inject
    public GameService(GameConfigRepository gameConfigRepository, GameRepository gameRepository, GameLogRepository gameLogRepository,
        FleetValidator fleetValidator) {
        this.gameConfigRepository = gameConfigRepository;
        this.gameRepository = gameRepository;
        this.gameLogRepository = gameLogRepository;
        this.fleetValidator = fleetValidator;
    }

    @Transactional
    public Game newGame(String playerName, String field, UUID configId) throws NewGameException, FleetValidationException, JsonProcessingException {
        Config predefinedConfig = gameConfigRepository.findById(configId)
                                                      .orElseThrow(() -> new NewGameException(configId));
        fleetValidator.validate(predefinedConfig, field);
        Config config;

        config = newConfig(predefinedConfig);

        Game newGame = new Game(config);
        gameRepository.persist(newGame);
        newGame.addPlayer(playerName, field);
        gameRepository.persist(newGame);
        return newGame;
    }

    private Config newConfig(Config predefinedConfig) {
        Config config = new Config();
        config.setName("Кастом");
        gameConfigRepository.persist(config);

        predefinedConfig.getParameters().forEach(p -> config.addParameter(p.getId().getName(), p.getValue()));
        gameConfigRepository.persist(config);
        return config;
    }

    @Transactional
    public Player joinGame(UUID gameId, String playerName, String field) {
        Game g = gameRepository.find("id", gameId).firstResult();
        g.setStartTime(LocalDateTime.now());
        g.addPlayer(playerName, field);
        gameRepository.persist(g);
        return g.getPlayer(playerName);
    }

    @Transactional
    public Game getGame(UUID id) {
        return gameRepository.find("id", id).firstResult();
    }

    public GamesGrid getGameGrid(UUID id) {
        return GamesGrid.find("id", id).firstResult();
    }

    @Transactional
    public void finishGame(String gameId, String winnerName, List<GameLog> gameLog) {
        Game g = gameRepository.find("id", UUID.fromString(gameId)).firstResult();
        g.setEndTime(LocalDateTime.now());
        g.setStatus(GameStatus.FINISHED);
        g.getPlayer(winnerName).setWinner(true);
        gameRepository.persist(g);

        gameLogRepository.persist(gameLog);
    }

    @Transactional
    public void removeGame(UUID gameId) {
        Game g = gameRepository.find("id", gameId).firstResult();
        g.setStatus(GameStatus.FAILED);
        gameRepository.persist(g);
    }

    @Transactional
    public List<ResponseGameChanged> getWaitingGames() {
        List<GamesGrid> gg = GamesGrid.list("status in (:status)", Sort.descending("createTime"),
            Parameters.with("status", List.of(GameStatus.WAITING, GameStatus.PLAYING)));

        return gg.stream()
                   .map(g -> new ResponseGameChanged(g.getCreateTime(), g.getPlayers(), g.getConfigName(), g.getStatus().toString(),
                       g.getId().toString()))
                   .toList();
    }
}
