//package com.example.islab1new.security;
//
//import jakarta.annotation.PostConstruct;
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.inject.Inject;
//import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
//
//@ApplicationScoped
//public class SecurityConfig {
//
//    @Inject
//    private CustomAuthenticationMechanism authenticationMechanism;
//
//    @PostConstruct
//    public void configure() {
//        authenticationMechanism.addPublicPath("/api/auth/register");
//        authenticationMechanism.addPublicPath("/api/auth/login");
//    }
//}
