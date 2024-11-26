package com.example.islab1new.dao;


import com.example.islab1new.models.Coordinates;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class CoordinatesDAO {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(Coordinates coordinates) {
        try {
            System.out.println("Creating coordinates: " + coordinates.getX()+ " " + coordinates.getY());
            em.persist(coordinates);
        } catch (Exception e) {
            throw e;
        }
    }

    public Coordinates findById(Integer id) {

        return em.find(Coordinates.class, id);

    }

    public List<Coordinates> findAll() {
        return em.createQuery("SELECT a FROM Coordinates a", Coordinates.class).getResultList();

    }

    @Transactional
    public void update(Coordinates coordinates) {
        try {
            em.merge(coordinates);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void delete(Integer id) {
        try {
            Coordinates coordinates = em.find(Coordinates.class, id);
            if (coordinates != null) {
                em.remove(coordinates);
            } else {
                throw new EntityNotFoundException("coordinates not found");
            }
        } catch (Exception e) {
            throw e;
        }
    }

}
