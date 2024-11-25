package com.example.islab1new.services;


import com.example.islab1new.dao.CoordinatesDAO;
import com.example.islab1new.models.Coordinates;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CoordinatesService {

    @Inject
    private CoordinatesDAO coordinatesDAO;

    public void addCoordinates(Coordinates coordinates) {
        coordinatesDAO.save(coordinates);
    }

    public void updateCoordinates(Coordinates coordinates){
        coordinatesDAO.update(coordinates);
    }

    public Coordinates getCoordinatesById(Integer id) {
        return coordinatesDAO.findById(id);
    }

    public List<Coordinates> getAllCoordinates() {
        return coordinatesDAO.findAll();
    }

    public void removeCoordinates(Integer id) {
        coordinatesDAO.delete(id);
    }

}
