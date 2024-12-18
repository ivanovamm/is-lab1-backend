package com.example.islab1new.services;

import com.example.islab1new.models.history.OrganizationHistory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import com.example.islab1new.models.Organization;
import com.example.islab1new.dao.OrganizationDAO;

import java.util.List;
@ApplicationScoped
public class OrganizationService {

    @Inject
    private OrganizationDAO OrganizationDAO;

    public void deleteByAddress(Integer addressId){
        OrganizationDAO.deleteByAddress(addressId);
    }

    public void deleteByCoordinates(Integer coordinatesId){
        OrganizationDAO.deleteByCoordinates(coordinatesId);
    }

    public void addOrganization(Organization organization, Integer userId) {
        OrganizationDAO.save(organization, userId);
    }

    public void updateOrganization(Organization organization, Integer userId){
        OrganizationDAO.update(organization, userId);
    }

    public Organization getOrganizationById(Integer id) {
        return OrganizationDAO.findById(id);
    }

    public List<Organization> getAllOrganizations() {
        return OrganizationDAO.findAll();
    }
    public List<OrganizationHistory> getAllHistory(){
        return OrganizationDAO.findAllHistory();
    }

    public void removeOrganization(Integer id, Integer userId) {
        OrganizationDAO.delete(id, userId);
    }
}
