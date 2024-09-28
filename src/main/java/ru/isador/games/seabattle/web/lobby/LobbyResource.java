package ru.isador.games.seabattle.web.lobby;

import java.util.List;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.Blocking;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import ru.isador.games.seabattle.domain.config.Config;
import ru.isador.games.seabattle.domain.config.GameConfigRepository;

@Path("/lobby")
public class LobbyResource {

    private final Template lobby;
    private final GameConfigRepository gameConfigRepository;

    @Inject
    public LobbyResource(Template lobby, GameConfigRepository gameConfigRepository) {
        this.lobby = lobby;
        this.gameConfigRepository = gameConfigRepository;
    }

    @GET
    @Blocking
    @Produces("text/html")
    @RolesAllowed("user")
    public TemplateInstance lobby(@Context SecurityContext securityContext) {
        List<Config> configs = gameConfigRepository.listPredefined();
        return lobby.data("playerName", securityContext.getUserPrincipal().getName())
                    .data("gameConfigList", convert(configs));
    }

    private List<GameConfView> convert(List<Config> gamesConfig) {
        return gamesConfig.stream()
                   .map(c -> {
                       StringBuilder json = new StringBuilder("{");
                       c.getParameters()
                           .forEach(p -> {
                               if ("FIELD_SIZE".equals(p.getId().getName())) {
                                   json.append("\"")
                                       .append("fieldSize")
                                       .append("\":")
                                       .append(p.getValue())
                                       .append(",");
                               }
                               if ("BORDERS_ALLOWED".equals(p.getId().getName())) {
                                   json.append("\"")
                                       .append("bordersAllowed")
                                       .append("\":")
                                       .append(p.getValue())
                                       .append(",");
                               }
                               if ("CORNERS_ALLOWED".equals(p.getId().getName())) {
                                   json.append("\"")
                                       .append("cornersAllowed")
                                       .append("\":")
                                       .append(p.getValue())
                                       .append(",");
                               }
                               if ("SHIPS".equals(p.getId().getName())) {
                                   json.append("\"")
                                       .append("ships")
                                       .append("\":")
                                       .append("\"")
                                       .append(p.getValue())
                                       .append("\"")
                                       .append(",");
                               }
                           });
                       json.deleteCharAt(json.length() - 1)
                           .append('}');
                       return new GameConfView(c.getConfigId().toString(), c.getName(), json.toString());
                   })
                          .toList();
    }
}
