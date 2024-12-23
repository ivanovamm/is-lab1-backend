package com.example.islab1new.controllers;

import com.example.islab1new.auth.AuthenticationResponse;
import com.example.islab1new.dao.UserDAO;
import com.example.islab1new.models.history.AddressHistory;
import com.example.islab1new.requests.AddressRequest;
import com.example.islab1new.responses.AddressResponse;
import com.example.islab1new.services.AddressService;
import com.example.islab1new.services.OrganizationService;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.servlet.http.Part;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.EntityPart;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.example.islab1new.models.Address;

import java.io.InputStream;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Path("/addresses")
public class AddressController {

    @Inject
    private AddressService addressService;

    @Inject
    private UserDAO userDAO;

    @Inject
    private OrganizationService organizationService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @GET
    @Path("/history")
    @Produces(MediaType.APPLICATION_JSON)
    public  List<AddressHistory> getAllHistory(){
        return addressService.allHistory();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAddressById(@PathParam("id") Integer id) {
        Address address = addressService.getAddressById(id);
        if (address != null) {
            return Response.ok(address).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAddress(Address address, @Context jakarta.servlet.http.HttpServletRequest httpRequest) {
        String username = (String) httpRequest.getAttribute("username");
//        System.out.println("\n\n\n\n\n\n" + (String) httpRequest.getAttribute("username")+ "\n\n\n\n\n\n");
        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not authenticated").build();
        }

        Integer userId = userDAO.findUserByName(username).getId();
        //System.out.println("usernameID class = " + userId.getClass());

        address.setCreatorId(userId);
        address.setCreationDate(LocalDateTime.now().toString());

        addressService.addAddress(address, userId);

        return Response.status(Response.Status.CREATED).entity(address).build();
    }


    //todo
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAddress(@PathParam("id") Integer id, Address address, @Context jakarta.servlet.http.HttpServletRequest httpRequest) {
        String username = (String) httpRequest.getAttribute("username");
//        System.out.println("\n\n\n\n\n\n" + (String) httpRequest.getAttribute("username")+ "\n\n\n\n\n\n");
        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not authenticated").build();
        }

        Integer userId = userDAO.findUserByName(username).getId();
        Address existingAddress = addressService.getAddressById(id);
        if (!Objects.equals(userId, existingAddress.getCreatorId()) && !userDAO.findUserByName(username).getRole().equals("ADMIN")){
            return Response.status(Response.Status.FORBIDDEN).entity("Access forbidden").build();
        }
        if (existingAddress == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Address not found").build();
        }

        address.setId(existingAddress.getId());
        address.setCreatorId(existingAddress.getCreatorId());
        address.setCreationDate(existingAddress.getCreationDate());

        addressService.updateAddress(address, userId);

        return Response.ok(address).build();
    }



    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAddress(@PathParam("id") Integer id, @Context jakarta.servlet.http.HttpServletRequest httpRequest) {
        String username = (String) httpRequest.getAttribute("username");
//        System.out.println("\n\n\n\n\n\n" + (String) httpRequest.getAttribute("username")+ "\n\n\n\n\n\n");
        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not authenticated").build();
        }
        Integer userId = userDAO.findUserByName(username).getId();
        if (!Objects.equals(userId, addressService.getAddressById(id).getCreatorId()) && !userDAO.findUserByName(username).getRole().equals("ADMIN")){
            return Response.status(Response.Status.FORBIDDEN).entity("Access forbidden").build();
        }
        organizationService.deleteByAddress(id);
        addressService.removeAddress(id, userId);
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


            int importedCount = addressService.importAddresses(fileInputStream, userId);

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