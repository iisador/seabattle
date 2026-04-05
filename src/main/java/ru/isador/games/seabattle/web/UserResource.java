package ru.isador.games.seabattle.web;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.isador.games.seabattle.domain.user.UserService;

@Path("/users")
public class UserResource {

    @Inject
    UserService userService;

    @POST
    @RolesAllowed("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(CreateUserRequest request) {
        userService.createUser(request.username(), request.password());
        return Response.status(Response.Status.CREATED).build();
    }

    public record CreateUserRequest(String username, String password) {
    }
}
