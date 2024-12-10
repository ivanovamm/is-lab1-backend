package com.example.islab1new.controllers;

import com.example.islab1new.auth.AuthenticationResponse;
import com.example.islab1new.dao.UserDAO;
import com.example.islab1new.requests.AddressRequest;
import com.example.islab1new.responses.AddressResponse;
import com.example.islab1new.services.AddressService;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.example.islab1new.models.Address;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Path("/addresses")
public class AddressController {

    @Inject
    private AddressService addressService;

    @Inject
    private UserDAO userDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
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

        addressService.addAddress(address);

        return Response.status(Response.Status.CREATED).entity(address).build();
    }


    //todo
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAddress(@PathParam("id") Integer id, Address address) {
        Address existingAddress = addressService.getAddressById(id);
        if (existingAddress == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Address not found").build();
        }

        address.setId(existingAddress.getId());
        address.setCreatorId(existingAddress.getCreatorId());
        address.setCreationDate(existingAddress.getCreationDate());

        addressService.updateAddress(address);

        return Response.ok(address).build();
    }



    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAddress(@PathParam("id") Integer id) {
        addressService.removeAddress(id);
        return Response.noContent().build();
    }
}