package com.example.islab1new.auth;

import com.example.islab1new.dao.AdminRequestsDAO;
import com.example.islab1new.models.AdminRequests;
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

    @Inject
    private AdminRequestsDAO adminRequestsDAO;

    @Transactional
    public void approveAdminRequest(Integer userId) {
        AdminRequests adminRequest = adminRequestsDAO.findAdminRequestById(userId);
        adminRequest.setStatus("APPROVED");
        adminRequestsDAO.update(adminRequest);
        adminRequest.getUser().setRole("ADMIN");
        userDAO.save(adminRequest.getUser());
    }

    @Transactional
    public void denyAdminRequest(Integer userId) {
        AdminRequests adminRequest = adminRequestsDAO.findAdminRequestById(userId);
        adminRequest.setStatus("DENIED");
        adminRequestsDAO.update(adminRequest);
    }



}
