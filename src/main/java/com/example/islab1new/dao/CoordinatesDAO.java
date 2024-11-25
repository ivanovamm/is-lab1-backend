package com.example.islab1new.dao;


import com.example.islab1new.models.Coordinates;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Persistence;

import java.util.List;

@ApplicationScoped
public class CoordinatesDAO {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("coordinatesPU");

    public void save(Coordinates coordinates) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(coordinates);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Coordinates findById(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Coordinates.class, id);
        } finally {
            em.close();
        }
    }

    public List<Coordinates> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT a FROM Coordinates a", Coordinates.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void update(Coordinates coordinates) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(coordinates);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Coordinates coordinates = em.find(Coordinates.class, id);
            if (coordinates != null) {
                em.remove(coordinates);
            } else {
                throw new EntityNotFoundException("coordinates not found");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
}
