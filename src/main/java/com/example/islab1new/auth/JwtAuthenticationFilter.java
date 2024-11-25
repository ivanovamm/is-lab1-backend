package com.example.islab1new.auth;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;
import java.security.Principal;

@Provider
public class JwtAuthenticationFilter implements jakarta.ws.rs.container.ContainerRequestFilter {

    private static final String SECRET_KEY = "r3g5d8T$!v8Y7sJ@k9cLq2zZpO&1Ws#tG";

    @Override

    public void filter(ContainerRequestContext requestContext) throws IOException {

        System.out.println("filter");

        String path = requestContext.getUriInfo().getPath();
        if (path.equals("/auth/login") || path.equals("/auth/register")) {
            return;
        }
        System.out.println("Authorization");
        String authHeader = requestContext.getHeaderString("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new WebApplicationException("Authorization header must be provided", Response.Status.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        try {
            String username = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> username;
                }

                @Override
                public boolean isUserInRole(String role) {
                    return role.equals(Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().get("role"));
                }

                @Override
                public boolean isSecure() {
                    return requestContext.getUriInfo().getRequestUri().getScheme().equals("https");
                }

                @Override
                public String getAuthenticationScheme() {
                    return SecurityContext.BASIC_AUTH;
                }
            });
        } catch (SignatureException | ExpiredJwtException e) {
            throw new WebApplicationException("Invalid or expired token", Response.Status.UNAUTHORIZED);
        }
    }

}
