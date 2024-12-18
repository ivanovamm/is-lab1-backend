package com.example.islab1new.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private String role;

    @JsonCreator
    public RegisterRequest(@JsonProperty("username") String username,
                           @JsonProperty("password") String password, @JsonProperty("role") String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

}
