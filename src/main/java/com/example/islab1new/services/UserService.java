package com.example.islab1new.services;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import com.example.islab1new.models.User;
import com.example.islab1new.dao.UserDAO;

import java.util.List;

@Named
@RequestScoped
public class UserService {

    @Inject
    private UserDAO userDAO;

    public String register(String username, String password, String role) {
        if (!isPasswordUnique(password)) {
            return "Пароль должен быть уникальным";
        }

        if (User.ROLE_ADMIN.equals(role)) {
            if (hasAdmin()) {
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setRole(User.ROLE_USER);
                userDAO.save(user);
                return "Требуется одобрение существующего администратора";
            }
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        userDAO.save(user);
        return "Пользователь успешно зарегистрирован";
    }


    public User getUser(Integer id) {
        return userDAO.findById(id);
    }

    public User getUserByName(String name){
        return userDAO.findUserByName(name);
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public void updateUser(User user) {
        userDAO.update(user);
    }

    public void deleteUser(Integer id) {
        userDAO.delete(id);
    }


    private boolean isPasswordUnique(String password) {
        return userDAO.findAll().stream()
                .noneMatch(user -> user.getPassword().equals(password));
    }

    private boolean hasAdmin() {
        return userDAO.findAll().stream()
                .anyMatch(user -> User.ROLE_ADMIN.equals(user.getRole()));
    }

}
