//package com.example.islab1new.security;
//
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.enterprise.inject.Alternative;
//import jakarta.inject.Inject;
//import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
//import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
//import jakarta.security.enterprise.identitystore.CredentialValidationResult;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@ApplicationScoped
//@Alternative
//public class CustomAuthenticationMechanism implements HttpAuthenticationMechanism {
//
//    @Inject
//    private CustomIdentityStore customIdentityStore;
//
//    private final Set<String> publicPaths = new HashSet<>();
//
//    public void addPublicPath(String path) {
//        publicPaths.add(path);
//    }
//
//    @Override
//    public jakarta.security.enterprise.AuthenticationStatus validateRequest(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            HttpMessageContext context) {
//
//        String path = request.getRequestURI();
//        System.out.println("Request Path: " + path); // Логирование пути
//
//        // Публичные пути
//        if (path.startsWith("/api/auth/register")) {
//            System.out.println("Public path accessed: " + path); // Логирование публичного пути
//            return context.doNothing(); // Путь доступен без авторизации
//        }
//
//        // Проверка аутентификации для защищённых путей
//        CustomIdentityStore customIdentityStore = new CustomIdentityStore();
//        CredentialValidationResult validationResult =
//                customIdentityStore.validate(context.getAuthParameters().getCredential());
//
//        if (validationResult.getStatus() == CredentialValidationResult.Status.VALID) {
//            return context.notifyContainerAboutLogin(
//                    validationResult.getCallerPrincipal(),
//                    validationResult.getCallerGroups());
//        } else {
//            System.out.println("Unauthorized access attempt for: " + path); // Логирование неавторизованного доступа
//            return context.responseUnauthorized();
//        }
//    }
//
//}
