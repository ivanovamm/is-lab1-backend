package com.example.islab1new.controllers;

import com.example.islab1new.dto.AbsorbOrganizationDTO;
import com.example.islab1new.dto.MergeOrganizationDTO;
import com.example.islab1new.models.Address;
import com.example.islab1new.models.Organization;
import com.example.islab1new.models.OrganizationType;
import com.example.islab1new.services.SpecialOperationsService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/special-operations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SpecialOperationsController {

    @Inject
    private SpecialOperationsService service;

    //works
    @GET
    @Path("/average-rating")
    public Double getAverageRating() {
        return service.getAverageRating();
    }

    @GET
    @Path("/postal-address")
    public List<Organization> getOrganizationsWithPostalAddressGreaterThan(@QueryParam("threshold") int threshold) {
        return service.getOrganizationsWithPostalAddressGreaterThan(threshold);
    }

    //works
    @GET
    @Path("/unique-types")
    public List<OrganizationType> getUniqueOrganizationTypes() {
        return service.getUniqueOrganizationTypes();
    }

    @POST
    @Path("/merge")
    public Response mergeOrganizations(MergeOrganizationDTO request) {
        service.mergeOrganizations(request.getOrgId1(), request.getOrgId2(), request.getNewName(), request.getNewAddress());
        return Response.ok("Organizations merged successfully").build();
    }


    @POST
    @Path("/absorb")
    public Response absorbOrganization(AbsorbOrganizationDTO request) {
        service.absorbOrganization(request.getAbsorberId(), request.getAbsorbedId());
        return Response.ok("Organization absorbed successfully").build();
    }
}
