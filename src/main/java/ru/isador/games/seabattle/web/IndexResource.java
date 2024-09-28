package ru.isador.games.seabattle.web;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriBuilder;

@Path("")
public class IndexResource {

    @GET
    public Response index(@Context SecurityContext securityContext) {
        if (securityContext.getUserPrincipal() != null) {
            return Response.seeOther(UriBuilder.fromUri("/lobby").build()).build();
        }

        return Response.seeOther(UriBuilder.fromUri("/hello").build()).build();
    }
}
