package com.example.islab1new.dao;

import com.example.islab1new.models.AdminRequests;
import com.example.islab1new.models.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class AdminRequestsDAO{
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(AdminRequests adminRequest) {
        em.persist(adminRequest);
    }

    public List<AdminRequests> findPendingAdminRequests() {
        return em.createQuery("SELECT r FROM AdminRequests r WHERE r.status = :status", AdminRequests.class)
                .setParameter("status", "PENDING")
                .getResultList();
    }

    public AdminRequests findAdminRequestById(Integer id){
        return em.find(AdminRequests.class, id);
    }

    public void update(AdminRequests request) {
        em.merge(request);
    }
}