package com.example.islab1new.dao;

import com.example.islab1new.models.history.AddressHistory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AddressHistoryDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(AddressHistory history) {
        em.persist(history);
    }
}
