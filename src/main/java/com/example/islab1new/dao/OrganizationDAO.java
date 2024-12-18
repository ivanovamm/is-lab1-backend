package com.example.islab1new.dao;

import com.example.islab1new.models.history.Action;
import com.example.islab1new.models.history.OrganizationHistory;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import com.example.islab1new.models.Organization;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Named
@RequestScoped
public class OrganizationDAO {

    @PersistenceContext
    private EntityManager em;


    @Transactional
    public void save(Organization organization, Integer userId) {
        try {
            em.persist(organization);
            System.out.println("Creating organization: " + organization.getName());

            OrganizationHistory history = new OrganizationHistory();
            history.setOrganization(organization.getId());
            history.setAction(Action.CREATE);
            history.setActionDate(LocalDateTime.now().toString());
            history.setUserId(userId);
            em.persist(history);

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

    public List<OrganizationHistory> findAllHistory(){
        return em.createQuery("SELECT o FROM OrganizationHistory o", OrganizationHistory.class).getResultList();
    }


    @Transactional
    public void update(Organization organization, Integer userId) {
        try {
            em.merge(organization);
            OrganizationHistory history = new OrganizationHistory();
            history.setOrganization(organization.getId());
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
            Organization organization = em.find(Organization.class, id);
            if (organization != null) {
                em.remove(organization);
                OrganizationHistory history = new OrganizationHistory();
                history.setOrganization(organization.getId());
                history.setAction(Action.DELETE);
                history.setActionDate(LocalDateTime.now().toString());
                history.setUserId(userId);
                em.persist(history);
            } else {
                throw new EntityNotFoundException("Organization not found");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void deleteByAddress(Integer addressId) {
        em.createQuery("DELETE FROM Organization o WHERE o.postalAddress.id = :addressId OR o.officialAddress.id = :addressId")
                .setParameter("addressId", addressId)
                .executeUpdate();
    }

    @Transactional
    public void deleteByCoordinates(Integer coordinatesId) {
        em.createQuery("DELETE FROM Organization o WHERE o.coordinates.id = :coordinatesId")
                .setParameter("coordinatesId", coordinatesId)
                .executeUpdate();
    }


}
