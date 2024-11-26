package com.example.islab1new.dao;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import com.example.islab1new.models.Organization;
import jakarta.transaction.Transactional;

import java.util.List;

@Named
@RequestScoped
public class OrganizationDAO {

    @PersistenceContext
    private EntityManager em;


    @Transactional
    public void save(Organization organization) {
        try {
            em.persist(organization);

        } catch (Exception e) {
            throw e;
        }
    }


    public Organization findById(Integer id) {
        return em.find(Organization.class, id);
    }

    public List<Organization> findAll() {
        return em.createQuery("SELECT o FROM Organization o", Organization.class).getResultList();
    }

    @Transactional
    public void update(Organization organization) {
        try {
            em.merge(organization);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void delete(Integer id) {
        try {
            Organization organization = em.find(Organization.class, id);
            if (organization != null) {
                em.remove(organization);
            } else {
                throw new EntityNotFoundException("Organization not found");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
