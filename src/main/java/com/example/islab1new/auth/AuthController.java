package com.example.islab1new.auth;

import com.example.islab1new.dao.AdminRequestsDAO;
import com.example.islab1new.dao.UserDAO;
import com.example.islab1new.dto.AdminRequestsDTO;
import com.example.islab1new.dto.OrganizationDTO;
import com.example.islab1new.mappers.AdminRequestMapper;
import com.example.islab1new.mappers.OrganizationMapper;
import com.example.islab1new.models.AdminRequests;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Path("/auth")
public class AuthController {

    @Inject
    private AuthService authService;

    @Inject
    private AdminApprovalService adminApprovalService;

    @Inject
    private AdminRequestsDAO adminRequestsDAO;

    @Inject
    private UserDAO userDAO;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(RegisterRequest registerRequest) {
        try {
            System.out.println("before register");
            authService.registerUser(registerRequest.getUsername(), registerRequest.getPassword(), "USER");
            System.out.println("register");
            if (registerRequest.getRole().equals("ADMIN")){
                AdminRequests adminRequests = new AdminRequests();
                adminRequests.setStatus("PENDING");
                adminRequests.setUser(userDAO.findUserByName(registerRequest.getUsername()));
                adminRequestsDAO.save(adminRequests);
            }
            return Response.status(Response.Status.CREATED).entity("User registered successfully").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(LoginRequest loginRequest) {
        try {
            AuthenticationResponse response = authService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
            return Response.ok(response).build();
        } catch (LoginException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/pending-requests")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPendingAdminRequests() {
        try {
            List<AdminRequests> adminRequests = adminRequestsDAO.findPendingAdminRequests();
            List<AdminRequestsDTO> dtos = adminRequests.stream()
                    .map(AdminRequestMapper::toDTO)
                    .toList();
            return Response.ok(dtos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to fetch pending requests").build();
        }
    }

    @POST
    @Path("/approve/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response approveAdminRequest(@PathParam("id") Integer userId) {
        try {
            adminApprovalService.approveAdminRequest(userId);
            return Response.ok("Admin request approved successfully").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to approve admin request").build();
        }
    }

    @POST
    @Path("/deny/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response denyAdminRequest(@PathParam("id") Integer userId) {
        try {
            adminApprovalService.denyAdminRequest(userId);
            return Response.ok("Admin request denied successfully").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to deny admin request").build();
        }
    }
}
