package com.example.islab1new.services;


import com.example.islab1new.dao.CoordinatesDAO;
import com.example.islab1new.models.Coordinates;
import com.example.islab1new.models.history.CoordinatesHistory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CoordinatesService {

    @Inject
    private CoordinatesDAO coordinatesDAO;

    public void addCoordinates(Coordinates coordinates, Integer userId) {
        coordinatesDAO.save(coordinates, userId);
    }

    public void updateCoordinates(Coordinates coordinates, Integer userId){
        coordinatesDAO.update(coordinates, userId);
    }

    public Coordinates getCoordinatesById(Integer id) {
        return coordinatesDAO.findById(id);
    }

    public List<Coordinates> getAllCoordinates() {
        return coordinatesDAO.findAll();
    }

    public List<CoordinatesHistory> getAllHistory(){
        return coordinatesDAO.findAllHistory();
    }

    public void removeCoordinates(Integer id, Integer userId) {
        coordinatesDAO.delete(id, userId);
    }

}
