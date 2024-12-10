package com.example.islab1new.controllers;

import com.example.islab1new.dao.AddressDAO;
import com.example.islab1new.dao.CoordinatesDAO;
import com.example.islab1new.dao.UserDAO;
import com.example.islab1new.dto.OrganizationDTO;
import com.example.islab1new.models.Address;
import com.example.islab1new.models.Coordinates;
import com.example.islab1new.models.OrganizationType;
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

    @Inject
    private AddressDAO addressDAO;

    @Inject
    private CoordinatesDAO coordinatesDAO;

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
    public Response createOrganization(OrganizationDTO organizationDTO, @Context jakarta.servlet.http.HttpServletRequest httpRequest) {
        String username = (String) httpRequest.getAttribute("username");
        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not authenticated").build();
        }

        Integer userId = userDAO.findUserByName(username).getId();

        Organization organization = new Organization();
        organization.setName(organizationDTO.getName());
        organization.setCreatorId(userId);
        organization.setCreationDate(LocalDateTime.now().toString());

        Address officialAddress = addressDAO.findById(organizationDTO.getOfficialAddress());
        Address postalAddress = addressDAO.findById(organizationDTO.getPostalAddress());
        Coordinates coordinates = coordinatesDAO.findById(organizationDTO.getCoordinates());

        if (officialAddress == null || postalAddress == null || coordinates == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid related entity IDs").build();
        }

        organization.setOfficialAddress(officialAddress);
        organization.setPostalAddress(postalAddress);
        organization.setCoordinates(coordinates);

        organization.setAnnualTurnover(organizationDTO.getAnnualTurnover());
        organization.setEmployeesCount(organizationDTO.getEmployeesCount());
        organization.setRating(organizationDTO.getRating());
        organization.setType(organizationDTO.getType());

        organizationService.addOrganization(organization);

        return Response.status(Response.Status.CREATED).entity(organization).build();
    }


    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateOrganization(@PathParam("id") Integer id, Organization organization) {
        Organization existingOrganization = organizationService.getOrganizationById(id);
        if (existingOrganization == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Organization not found").build();
        }

        if (organization.getName() != null) {
            existingOrganization.setName(organization.getName());
        }
        if (organization.getCreationDate() != null) {
            existingOrganization.setCreationDate(organization.getCreationDate());
        }
        if (organization.getAnnualTurnover() != 0) {
            existingOrganization.setAnnualTurnover(organization.getAnnualTurnover());
        }
        if (organization.getEmployeesCount() != null) {
            existingOrganization.setEmployeesCount(organization.getEmployeesCount());
        }
        if (organization.getRating() != 0) {
            existingOrganization.setRating(organization.getRating());
        }
        if (organization.getType() != null) {
            existingOrganization.setType(organization.getType());
        }
        if (organization.getOfficialAddress() != null) {
            existingOrganization.setOfficialAddress(organization.getOfficialAddress());
        }
        if (organization.getPostalAddress() != null) {
            existingOrganization.setPostalAddress(organization.getPostalAddress());
        }
        if (organization.getCoordinates() != null) {
            existingOrganization.setCoordinates(organization.getCoordinates());
        }

        organizationService.updateOrganization(existingOrganization);

        return Response.ok(existingOrganization).build();
    }


    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteOrganization(@PathParam("id") Integer id) {
        organizationService.removeOrganization(id);
        return Response.noContent().build();
    }
}
