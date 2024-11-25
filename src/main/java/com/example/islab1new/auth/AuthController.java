package com.example.islab1new.auth;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import javax.security.auth.login.LoginException;

@RequestScoped
@Path("/auth")
public class AuthController {

    @Inject
    private AuthService authService;

    @POST
    @Path("/register")
    @Consumes("application/json")
    public Response register(RegisterRequest registerRequest) {
        try {
            System.out.println("before register");
            authService.registerUser(registerRequest.getUsername(), registerRequest.getPassword());
            System.out.println("register");
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
}
