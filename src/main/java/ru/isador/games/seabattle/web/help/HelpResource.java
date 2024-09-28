package ru.isador.games.seabattle.web.help;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/help")
public class HelpResource {

    private final Template help;

    @Inject
    public HelpResource(Template help) {
        this.help = help;
    }

    @GET
    @RolesAllowed("user")
    public TemplateInstance help() {
        return help.instance();
    }
}
