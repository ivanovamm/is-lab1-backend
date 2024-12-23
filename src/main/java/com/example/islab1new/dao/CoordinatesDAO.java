package com.example.islab1new.dao;


import com.example.islab1new.models.Address;
import com.example.islab1new.models.Coordinates;
import com.example.islab1new.models.history.Action;
import com.example.islab1new.models.history.AddressHistory;
import com.example.islab1new.models.history.CoordinatesHistory;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class CoordinatesDAO {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(Coordinates coordinates, Integer userId) {
        try {
            System.out.println("Creating coordinates: " + coordinates.getX()+ " " + coordinates.getY());
            em.persist(coordinates);
            CoordinatesHistory history = new CoordinatesHistory();
            history.setCoordinates(coordinates.getId());
            history.setAction(Action.CREATE);
            history.setActionDate(LocalDateTime.now().toString());
            history.setUserId(userId);
            em.persist(history);
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

    public List<CoordinatesHistory> findAllHistory(){
        return em.createQuery("SELECT a FROM CoordinatesHistory a", CoordinatesHistory.class).getResultList();
    }

    @Transactional
    public void update(Coordinates coordinates, Integer userId) {
        try {
            em.merge(coordinates);
            CoordinatesHistory history = new CoordinatesHistory();
            history.setCoordinates(coordinates.getId());
            history.setAction(Action.UPDATE);
            history.setActionDate(LocalDateTime.now().toString());
            history.setUserId(userId);
            em.persist(history);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void delete(Integer id, Integer userId) {
        try {
            Coordinates coordinates = em.find(Coordinates.class, id);
            if (coordinates != null) {
                em.remove(coordinates);
                CoordinatesHistory history = new CoordinatesHistory();
                history.setCoordinates(coordinates.getId());
                history.setAction(Action.DELETE);
                history.setActionDate(LocalDateTime.now().toString());
                history.setUserId(userId);
                em.persist(history);
            } else {
                throw new EntityNotFoundException("coordinates not found");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public int importCoordinates(InputStream fileInputStream, Integer userId) throws Exception {
        List<Coordinates> coordinates = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Coordinates[] importedCoordinates = objectMapper.readValue(fileInputStream, Coordinates[].class);
            coordinates.addAll(Arrays.asList(importedCoordinates));

            for (Coordinates coordinate : coordinates) {
                coordinate.setCreatorId(userId);
                coordinate.setCreationDate(LocalDateTime.now().toString());

                save(coordinate, userId);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении данных из файла: " + e.getMessage(), e);
        } catch (Exception e) {

            throw new RuntimeException("Ошибка при импорте: " + e.getMessage(), e);
        }
        return coordinates.size();
    }

}
