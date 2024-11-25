package com.example.islab1new.controllers;

import com.example.islab1new.services.AddressService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.example.islab1new.models.Address;

import java.util.List;

@Path("/addresses")
public class AddressController {

    @Inject
    private AddressService addressService;

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
    public Response createAddress(Address address) {
        addressService.addAddress(address);
        return Response.status(Response.Status.CREATED).entity(address).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAddress(@PathParam("id") Integer id, Address address) {
        address.setId(id);
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
