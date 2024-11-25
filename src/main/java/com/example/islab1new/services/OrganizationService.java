package com.example.islab1new.services;

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

    public void addOrganization(Organization organization) {
        OrganizationDAO.save(organization);
    }

    public void updateOrganization(Organization organization){
        OrganizationDAO.update(organization);
    }

    public Organization getOrganizationById(Integer id) {
        return OrganizationDAO.findById(id);
    }

    public List<Organization> getAllOrganizations() {
        return OrganizationDAO.findAll();
    }

    public void removeOrganization(Integer id) {
        OrganizationDAO.delete(id);
    }
}
