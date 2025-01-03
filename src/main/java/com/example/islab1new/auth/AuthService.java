package com.example.islab1new.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.islab1new.models.User;
import com.example.islab1new.dao.UserDAO;
import com.example.islab1new.security.JWTUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.NotAuthorizedException;

import javax.security.auth.login.LoginException;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class AuthService {

    @Inject
    private UserDAO userDAO;

    @Inject
    private JWTUtil jwtUtil;


    private static final int MIN_PASSWORD_LENGTH = 8;

    public boolean authenticate(String username, String password) {
        return "user".equals(username) && "password".equals(password);
    }

    @Transactional
    public boolean registerUser(@NotNull String username, @NotNull String password, @NotNull String role) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
        }

        Optional<User> existingUser = userDAO.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashPassword(password));
        user.setRole(role);
        userDAO.save(user);
        return true;
    }


    public AuthenticationResponse loginUser(String username, String password, String role) throws LoginException {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new LoginException("Invalid credentials"));

        if (!user.getPassword().equals(hashPassword(password))) {
            throw new LoginException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(username, password, role);

        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    private String hashPassword(String password) {
        return org.apache.commons.codec.digest.DigestUtils.sha512Hex(password);
    }

    public String getRoleFromToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);

            String role = decodedJWT.getClaim("role").asString();

            if (role == null) {
                throw new NotAuthorizedException("Role not found in token");
            }

            return role;
        } catch (JWTDecodeException e) {
            throw new NotAuthorizedException("Invalid token format");
        }
    }

}
