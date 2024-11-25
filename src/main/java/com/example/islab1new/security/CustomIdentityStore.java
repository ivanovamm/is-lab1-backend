//package com.example.islab1new.security;
//
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.security.enterprise.identitystore.CredentialValidationResult;
//import jakarta.security.enterprise.identitystore.IdentityStore;
//import jakarta.security.enterprise.credential.UsernamePasswordCredential;
//
//import java.util.Set;
//
//@ApplicationScoped
//public class CustomIdentityStore implements IdentityStore {
//
//    @Override
//    public CredentialValidationResult validate(jakarta.security.enterprise.credential.Credential credential) {
//        if (credential instanceof UsernamePasswordCredential) {
//            UsernamePasswordCredential usernamePassword = (UsernamePasswordCredential) credential;
//
//            // Пример проверки логина и пароля
//            String username = usernamePassword.getCaller();
//            String password = usernamePassword.getPasswordAsString();
//
//            // Здесь реализуется логика проверки (например, через базу данных)
//            if ("admin".equals(username) && "password".equals(password)) {
//                return new CredentialValidationResult(username, Set.of("ADMIN"));
//            }
//        }
//
//        return CredentialValidationResult.INVALID_RESULT;
//    }
//}
