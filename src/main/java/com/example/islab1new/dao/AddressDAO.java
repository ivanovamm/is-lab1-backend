package com.example.islab1new.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import com.example.islab1new.models.Address;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class AddressDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(Address address) {
        try {
            System.out.println("Creating address: " + address.getStreet());
            em.persist(address);
        } catch (Exception e) {
            throw e;
        }
    }

    public Address findById(Integer id) {
        return em.find(Address.class, id);
    }

    public List<Address> findAll() {
        return em.createQuery("SELECT a FROM Address a", Address.class).getResultList();

    }

    @Transactional
    public void update(Address address) {
        try {
            em.merge(address);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void delete(Integer id) {
        try {
            Address address = em.find(Address.class, id);
            if (address != null) {
                em.remove(address);
            } else {
                throw new EntityNotFoundException("Address not found");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
