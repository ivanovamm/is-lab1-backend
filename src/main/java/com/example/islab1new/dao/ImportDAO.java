package com.example.islab1new.dao;

import com.example.islab1new.models.history.ImportHistory;
import com.example.islab1new.models.history.Status;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Stateless
public class ImportDAO {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void saveFailedImportHistory(Integer userId) {
        ImportHistory importHistory = new ImportHistory();
        importHistory.setStatus(Status.FAILED);
        importHistory.setImportCount(0);
        importHistory.setUserId(userId);
        em.persist(importHistory);
    }

    @Transactional
    public void saveSuccessImportHistory(int count, Integer userId) {
        ImportHistory importHistory = new ImportHistory();
        importHistory.setStatus(Status.SUCCESS);
        importHistory.setImportCount(count);
        importHistory.setUserId(userId);
        em.persist(importHistory);
    }
}
