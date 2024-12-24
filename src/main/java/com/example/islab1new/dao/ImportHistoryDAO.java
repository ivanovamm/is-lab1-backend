package com.example.islab1new.dao;

import com.example.islab1new.models.history.CoordinatesHistory;
import com.example.islab1new.models.history.ImportHistory;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class ImportHistoryDAO {

    @PersistenceContext
    private EntityManager em;

    public void saveImportHistory(ImportHistory importHistory) {
        em.persist(importHistory);
    }

    public List<ImportHistory> findAllHistory(){
        return em.createQuery("SELECT a FROM ImportHistory a", ImportHistory.class).getResultList();
    }
}
