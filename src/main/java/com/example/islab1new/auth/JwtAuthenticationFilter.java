package com.example.islab1new.auth;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import io.jsonwebtoken.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;

@Provider
public class JwtAuthenticationFilter implements ContainerRequestFilter {

    private static final String SECRET_KEY = "9W6BJ9ITw20rjZHciqS4QfTP9G0bl05U1J77T5Grkp0";

//    @Override
//    public void filter(ContainerRequestContext requestContext) throws IOException {
//        String path = requestContext.getUriInfo().getPath();
//        if (path.equals("/auth/login") || path.equals("/auth/register")) {
//            return;
//        }
//
//        String authHeader = requestContext.getHeaderString("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new WebApplicationException("Authorization header must be provided", Response.Status.UNAUTHORIZED);
//        }
//
//        String token = authHeader.substring(7);
//
//        try {
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(SECRET_KEY.getBytes())
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            System.out.println("Token claims: " + claims);
//
//            String username = claims.getSubject();
//            String role = claims.get("role", String.class);
//
//            Date expiration = claims.getExpiration();
//            System.out.println("Token expiration: " + expiration);
//            if (expiration.before(new Date())) {
//                throw new WebApplicationException("Token has expired", Response.Status.UNAUTHORIZED);
//            }
//
//            requestContext.setSecurityContext(new SecurityContext() {
//                @Override
//                public Principal getUserPrincipal() {
//                    return () -> username;
//                }
//
//                @Override
//                public boolean isUserInRole(String roleToCheck) {
//                    return role.equals(roleToCheck);
//                }
//
//                @Override
//                public boolean isSecure() {
//                    return requestContext.getUriInfo().getRequestUri().getScheme().equals("https");
//                }
//
//                @Override
//                public String getAuthenticationScheme() {
//                    return "Bearer";
//                }
//            });
//
//        } catch (ExpiredJwtException e) {
//            throw new WebApplicationException("Token expired", Response.Status.UNAUTHORIZED);
//        } catch (JwtException e) {
//            throw new WebApplicationException("Invalid token", Response.Status.UNAUTHORIZED);
//        }
//    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();

        if (path.equals("/auth/login") || path.equals("/auth/register")) {
            return;
        }

        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new WebApplicationException("Authorization header must be provided", Response.Status.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            if (username == null) {
                throw new WebApplicationException("Invalid token: missing username", Response.Status.UNAUTHORIZED);
            }

            requestContext.setProperty("username", username);
            requestContext.setProperty("userRole", role);


        } catch (ExpiredJwtException e) {
            throw new WebApplicationException("Token expired", Response.Status.UNAUTHORIZED);
        } catch (JwtException e) {
            throw new WebApplicationException("Invalid token", Response.Status.UNAUTHORIZED);
        }
    }

}
