package com.example.islab1new.controllers;

import com.example.islab1new.dao.AddressDAO;
import com.example.islab1new.dao.CoordinatesDAO;
import com.example.islab1new.dao.UserDAO;
import com.example.islab1new.dto.OrganizationDTO;
import com.example.islab1new.mappers.OrganizationMapper;
import com.example.islab1new.models.Address;
import com.example.islab1new.models.Coordinates;
import com.example.islab1new.models.OrganizationType;
import com.example.islab1new.models.history.OrganizationHistory;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.EntityPart;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.example.islab1new.models.Organization;
import com.example.islab1new.services.OrganizationService;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public Response getAllOrganizations() {
        List<Organization> organizations = organizationService.getAllOrganizations();

        List<OrganizationDTO> dtos = organizations.stream()
                .map(OrganizationMapper::toDTO)
                .collect(Collectors.toList());

        return Response.ok(dtos).build();
    }

    @GET
    @Path("/history")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganizationHistory> getAllHistory(){
        return organizationService.getAllHistory();
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

        organizationService.addOrganization(organization, userId);

        return Response.status(Response.Status.CREATED).entity(organization).build();
    }


    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateOrganization(
            @PathParam("id") Integer id,
            OrganizationDTO organizationDTO,
            @Context jakarta.servlet.http.HttpServletRequest httpRequest
    ) {
        String username = (String) httpRequest.getAttribute("username");
        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not authenticated").build();
        }

        Integer userId = userDAO.findUserByName(username).getId();
        Organization existingOrganization = organizationService.getOrganizationById(id);
        if (!Objects.equals(userId, existingOrganization.getCreatorId()) && !userDAO.findUserByName(username).getRole().equals("ADMIN")){
            return Response.status(Response.Status.FORBIDDEN).entity("Access forbidden").build();
        }
        if (existingOrganization == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Organization not found").build();
        }

        if (organizationDTO.getName() != null) {
            existingOrganization.setName(organizationDTO.getName());
        }
        if (organizationDTO.getAnnualTurnover() != 0) {
            existingOrganization.setAnnualTurnover(organizationDTO.getAnnualTurnover());
        }
        if (organizationDTO.getEmployeesCount() != null) {
            existingOrganization.setEmployeesCount(organizationDTO.getEmployeesCount());
        }
        if (organizationDTO.getRating() != 0) {
            existingOrganization.setRating(organizationDTO.getRating());
        }
        if (organizationDTO.getType() != null) {
            existingOrganization.setType(organizationDTO.getType());
        }
        if (organizationDTO.getOfficialAddress() != null) {
            Address officialAddress = addressDAO.findById(organizationDTO.getOfficialAddress());
            if (officialAddress != null) {
                existingOrganization.setOfficialAddress(officialAddress);
            }
        }
        if (organizationDTO.getPostalAddress() != null) {
            Address postalAddress = addressDAO.findById(organizationDTO.getPostalAddress());
            if (postalAddress != null) {
                existingOrganization.setPostalAddress(postalAddress);
            }
        }
        if (organizationDTO.getCoordinates() != null) {
            Coordinates coordinates = coordinatesDAO.findById(organizationDTO.getCoordinates());
            if (coordinates != null) {
                existingOrganization.setCoordinates(coordinates);
            }
        }

        organizationService.updateOrganization(existingOrganization, userId);

        return Response.ok(existingOrganization).build();
    }



    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteOrganization(@PathParam("id") Integer id,  @Context jakarta.servlet.http.HttpServletRequest httpRequest) {
        String username = (String) httpRequest.getAttribute("username");
        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not authenticated").build();
        }

        Integer userId = userDAO.findUserByName(username).getId();
        if (!Objects.equals(userId, organizationService.getOrganizationById(id).getCreatorId()) && !userDAO.findUserByName(username).getRole().equals("ADMIN")){
            return Response.status(Response.Status.FORBIDDEN).entity("Access forbidden").build();
        }
        organizationService.removeOrganization(id, userId);
        return Response.noContent().build();
    }

    @POST
    @Path("/import")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importFile(List<EntityPart> parts, @Context jakarta.servlet.http.HttpServletRequest httpRequest) {

        var filePart = parts.stream()
                .filter(part -> "file".equals(part.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Файл не найден"));

        String username = (String) httpRequest.getAttribute("username");
        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("User not authenticated")
                    .build();
        }
        Integer userId = userDAO.findUserByName(username).getId();

        try {

            InputStream fileInputStream = filePart.getContent();
            String fileName = filePart.getFileName().orElseThrow(() -> new IllegalArgumentException("Имя файла отсутствует"));


            int importedCount = organizationService.importOrganizations(fileInputStream, userId);

            return Response.ok(Map.of("message", "Успешно импортировано объектов: " + importedCount,
                            "fileName", fileName,
                            "importedCount", importedCount))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .build();
        } catch (Exception e) {

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("message", "Ошибка импорта", "error", e.getMessage()))
                    .build();
        }
    }
}
