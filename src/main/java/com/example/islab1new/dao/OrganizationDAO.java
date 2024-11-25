package com.example.islab1new.dao;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Persistence;
import com.example.islab1new.models.Organization;

import java.util.List;
@Named
@RequestScoped
public class OrganizationDAO {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("organizationPU");

    public void save(Organization organization) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(organization);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Organization findById(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Organization.class, id);
        } finally {
            em.close();
        }
    }

    public List<Organization> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT o FROM Organization o", Organization.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void update(Organization organization) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(organization);
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
            Organization organization = em.find(Organization.class, id);
            if (organization != null) {
                em.remove(organization);
            } else {
                throw new EntityNotFoundException("Organization not found");
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
