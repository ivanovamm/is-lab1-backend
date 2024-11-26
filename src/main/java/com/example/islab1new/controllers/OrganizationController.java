package com.example.islab1new.controllers;

import com.example.islab1new.dao.UserDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.example.islab1new.models.Organization;
import com.example.islab1new.services.OrganizationService;

import java.time.LocalDateTime;
import java.util.List;

@Path("/organizations")
public class OrganizationController {

    @Inject
    private OrganizationService organizationService;

    @Inject
    private UserDAO userDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Organization> getAllOrganizations() {
        return organizationService.getAllOrganizations();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrganizationById(@PathParam("id") Integer id) {
        Organization organization = organizationService.getOrganizationById(id);
        if (organization != null) {
            return Response.ok(organization).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrganization(Organization organization, @Context jakarta.servlet.http.HttpServletRequest httpRequest) {
        String username = (String) httpRequest.getAttribute("username");
        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not authenticated").build();
        }
        Integer userId = userDAO.findUserByName(username).getId();

        organization.setCreatorId(userId);
        organization.setCreationDate(LocalDateTime.now().toString());

        organizationService.addOrganization(organization);
        return Response.status(Response.Status.CREATED).entity(organization).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateOrganization(@PathParam("id") Integer id, Organization organization) {
        organization.setId(id);
        organizationService.updateOrganization(organization);
        return Response.ok(organization).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteOrganization(@PathParam("id") Integer id) {
        organizationService.removeOrganization(id);
        return Response.noContent().build();
    }
}
