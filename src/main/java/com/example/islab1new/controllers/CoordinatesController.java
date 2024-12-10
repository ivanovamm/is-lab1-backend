package com.example.islab1new.controllers;

import com.example.islab1new.dao.UserDAO;
import com.example.islab1new.models.Address;
import com.example.islab1new.models.Coordinates;
import com.example.islab1new.services.CoordinatesService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;

@Path("/coordinates")
public class CoordinatesController {
    @Inject
    private CoordinatesService coordinatesService;

    @Inject
    UserDAO userDAO;

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
    public Response createCoordinates(Coordinates coordinates, @Context jakarta.servlet.http.HttpServletRequest httpRequest) {
        String username = (String) httpRequest.getAttribute("username");
        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not authenticated").build();
        }
        Integer userId = userDAO.findUserByName(username).getId();

        coordinates.setCreatorId(userId);
        coordinates.setCreationDate(LocalDateTime.now().toString());

        coordinatesService.addCoordinates(coordinates, userId);

        return Response.status(Response.Status.CREATED).entity(coordinates).build();
    }


    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCoordinates(@PathParam("id") Integer id, Coordinates coordinates,  @Context jakarta.servlet.http.HttpServletRequest httpRequest) {
        String username = (String) httpRequest.getAttribute("username");
        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not authenticated").build();
        }
        Integer userId = userDAO.findUserByName(username).getId();

        Coordinates existingCoordinates = coordinatesService.getCoordinatesById(id);
        if (existingCoordinates == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Coordinates not found").build();
        }

        if (coordinates.getX() != null) {
            existingCoordinates.setX(coordinates.getX());
        }
        if (coordinates.getY() != null) {
            existingCoordinates.setY(coordinates.getY());
        }

        coordinatesService.updateCoordinates(existingCoordinates, userId);

        return Response.ok(existingCoordinates).build();
    }


    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCoordinates(@PathParam("id") Integer id,  @Context jakarta.servlet.http.HttpServletRequest httpRequest) {
        String username = (String) httpRequest.getAttribute("username");
        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not authenticated").build();
        }
        Integer userId = userDAO.findUserByName(username).getId();
        coordinatesService.removeCoordinates(id, userId);
        return Response.noContent().build();
    }
}
