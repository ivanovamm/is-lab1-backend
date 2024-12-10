package com.example.islab1new.dao;

import com.example.islab1new.models.history.Action;
import com.example.islab1new.models.history.AddressHistory;
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
    public void save(Address address, Integer userId) {
        try {
            System.out.println("Creating address: " + address.getStreet());
            em.persist(address);
            AddressHistory history = new AddressHistory();
            history.setAddress(address);
            history.setAction(Action.CREATE);
            history.setActionDate(LocalDateTime.now().toString());
            history.setUserId(userId);
            em.persist(history);
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
    public void update(Address address, Integer userId) {
        try {
            System.out.println("Updating address: " + address);
            em.merge(address);
            AddressHistory history = new AddressHistory();
            history.setAddress(address);
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
            Address address = em.find(Address.class, id);
            if (address != null) {
                em.remove(address);
                AddressHistory history = new AddressHistory();
                history.setAddress(address);
                history.setAction(Action.DELETE);
                history.setActionDate(LocalDateTime.now().toString());
                history.setUserId(userId);
                em.persist(history);
            } else {
                throw new EntityNotFoundException("Address not found");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}