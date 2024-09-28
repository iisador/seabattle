package ru.isador.games.seabattle.web.hello;

import io.quarkus.qute.Template;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import ru.isador.games.seabattle.domain.user.UserService;

@Path("/hello")
@PermitAll
public class HelloResource {

    private final Template hello;
    private final UserService userService;

    @Inject
    public HelloResource(Template hello, UserService userService) {
        this.hello = hello;
        this.userService = userService;
    }

    @GET
    @Produces("text/html")
    public Object hello() {
        return hello.data("error", false);
    }

    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    public String register(UserResource newUser) {
        if (userService.hasUser(newUser.username())) {
            return "contains";
        } else {
            userService.newUser(newUser.username(), newUser.password());
            return "created";
        }
    }
}
