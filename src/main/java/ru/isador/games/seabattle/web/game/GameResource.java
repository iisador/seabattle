package ru.isador.games.seabattle.web.game;

import java.util.UUID;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.eventbus.EventBus;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriBuilder;
import ru.isador.games.seabattle.domain.game.Game;
import ru.isador.games.seabattle.domain.game.GameStatus;
import ru.isador.games.seabattle.domain.player.Player;
import ru.isador.games.seabattle.services.game.GameService;

@Path("/games")
@RolesAllowed("user")
public class GameResource {

    private final GameService gameService;
    private final Template game;
    private final Template viewGame;
    private final Template joinGame;
    private final EventBus eventBus;

    @Inject
    public GameResource(GameService gameService, Template game, Template viewGame, Template joinGame, EventBus eventBus) {
        this.gameService = gameService;
        this.game = game;
        this.viewGame = viewGame;
        this.joinGame = joinGame;
        this.eventBus = eventBus;
    }

    @POST
    @Blocking
    @Produces("text/plain")
    public UUID createGame(@Context SecurityContext securityContext, NewGameResource newGameResource) {
        String playerName = securityContext.getUserPrincipal().getName();
        Game g = gameService.newGame(playerName, newGameResource.field(), newGameResource.config());
        eventBus.publish("gameBus", gameService.getGameGrid(g.getId()));
        return g.getId();
    }

    @GET
    @Blocking
    @Path("/{id}")
    public Object getGame(@Context SecurityContext securityContext, @PathParam("id") UUID id, @QueryParam("field") String field) {
        Game g = gameService.getGame(id);
        if (g.getStatus().equals(GameStatus.FAILED) || g.getStatus().equals(GameStatus.FINISHED)) {
            return Response.seeOther(UriBuilder.fromPath("/lobby").build()).build();
        }
        Player me = g.getPlayer(securityContext.getUserPrincipal().getName());
        return game.data("myShips", me == null ? field : me.getField())
                   .data("playerName", securityContext.getUserPrincipal().getName())
                   .data("gameId", g.getId());
    }

    @GET
    @Path("/{id}/view")
    public TemplateInstance viewGame(@Context SecurityContext securityContext, @PathParam("id") UUID id) {
        return viewGame.data("viewerName", securityContext.getUserPrincipal().getName())
                   .data("gameId", id);
    }

    @GET
    @Blocking
    @Path("/{id}/join")
    public TemplateInstance joinGame(@Context SecurityContext securityContext, @PathParam("id") UUID id) {
        Game g = gameService.getGame(id);
        Player opponent = g.getPlayers().stream()
                              .findFirst().orElse(null);
        return joinGame.data("gameId", id)
                       .data("opponentName", opponent.getPlayerName())
                       .data("playerName", securityContext.getUserPrincipal().getName())
                       .data("FIELD_SIZE", g.getConfig().getParameter("FIELD_SIZE").getValue())
                       .data("BORDERS_ALLOWED", Boolean.parseBoolean(g.getConfig().getParameter("BORDERS_ALLOWED").getValue()))
                       .data("CORNERS_ALLOWED", Boolean.parseBoolean(g.getConfig().getParameter("CORNERS_ALLOWED").getValue()))
                       .data("SHIPS", g.getConfig().getParameter("SHIPS").getValue());
    }
}
