package ru.isador.games.seabattle.web.stats;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/stats")
public class StatsResource {

    private final Template stats;

    @Inject
    public StatsResource(Template stats) {
        this.stats = stats;
    }

    @GET
    @RolesAllowed("user")
    public TemplateInstance stats() {
        return stats.instance();
    }
}
