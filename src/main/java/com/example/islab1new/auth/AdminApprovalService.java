package com.example.islab1new.auth;

import com.example.islab1new.models.User;
import com.example.islab1new.dao.UserDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class AdminApprovalService {

    // TODO: 11.12.2024 панель админа, поиск по ид, привелегии админа + пофиксить баги и добавить эксепшены

    // TODO: 11.12.2024 асинхронное обновление + пагинация (но необязательно)
    @Inject
    private UserDAO userDAO;

    @Transactional
    public void approveAdminRequest(Integer userId) {
        User user = userDAO.findById(userId);
        if (user != null && !user.getRole().equals(User.ROLE_ADMIN)) {
            user.setRole(User.ROLE_ADMIN);
            userDAO.save(user);
        } else {
            throw new IllegalArgumentException("User is already an admin or not found");
        }
    }

    public List<User> getPendingAdminRequests() {
        return userDAO.findAll();
    }
}
