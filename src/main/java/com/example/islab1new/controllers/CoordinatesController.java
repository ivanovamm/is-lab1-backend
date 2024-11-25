package com.example.islab1new.controllers;

import com.example.islab1new.models.Coordinates;
import com.example.islab1new.services.CoordinatesService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/coordinates")
public class CoordinatesController {
    @Inject
    private CoordinatesService coordinatesService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Coordinates> getAllCoordinates() {
        return coordinatesService.getAllCoordinates();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCoordinatesById(@PathParam("id") Integer id) {
        Coordinates Coordinates = coordinatesService.getCoordinatesById(id);
        if (Coordinates != null) {
            return Response.ok(Coordinates).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCoordinates(Coordinates Coordinates) {
        coordinatesService.addCoordinates(Coordinates);
        return Response.status(Response.Status.CREATED).entity(Coordinates).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCoordinates(@PathParam("id") Integer id, Coordinates coordinates) {
        coordinates.setId(id);
        coordinatesService.updateCoordinates(coordinates);
        return Response.ok(coordinates).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCoordinates(@PathParam("id") Integer id) {
        coordinatesService.removeCoordinates(id);
        return Response.noContent().build();
    }
}
