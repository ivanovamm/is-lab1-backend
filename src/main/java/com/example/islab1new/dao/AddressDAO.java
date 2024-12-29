package com.example.islab1new.dao;

import com.example.islab1new.models.history.Action;
import com.example.islab1new.models.history.AddressHistory;
import com.example.islab1new.models.history.ImportHistory;
import com.example.islab1new.models.history.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;
import com.example.islab1new.models.Address;
import jakarta.servlet.http.Part;
import jakarta.transaction.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;

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
            history.setAddress(address.getId());
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

    public List<AddressHistory> findAllHistory() {
        return em.createQuery("SELECT a FROM AddressHistory a", AddressHistory.class).getResultList();

    }

    @Transactional
    public void update(Address address, Integer userId) {
        try {
            System.out.println("Updating address: " + address);
            em.merge(address);
            AddressHistory history = new AddressHistory();
            history.setAddress(address.getId());
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
                history.setAddress(address.getId());
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


    //todo продебажить!!!
    @Transactional
    public int importAddresses(InputStream fileInputStream, Integer userId) throws Exception {
        List<Address> addresses = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        int importCount;

        ImportHistory importHistory = new ImportHistory();
        importHistory.setStatus(Status.FAILED);
        importHistory.setImportCount(0);
        importHistory.setUserId(userId);
        em.persist(importHistory);


        try {
            Address[] importedAddresses = objectMapper.readValue(fileInputStream, Address[].class);
            addresses.addAll(Arrays.asList(importedAddresses));
            Set<String> streets = new HashSet<>();
            importCount = addresses.size();

            for (Address address : addresses) {
                if (streets.contains(address.getStreet())){
                    importCount -= 1;
                    System.out.println("Названия улиц должны быть уникальными");
                    continue;
                }
                streets.add(address.getStreet());
                address.setCreatorId(userId);
                address.setCreationDate(LocalDateTime.now().toString());
                save(address, userId);
            }

            importHistory.setStatus(Status.SUCCESS);
            importHistory.setImportCount(addresses.size());
            em.merge(importHistory);

        } catch (IOException e) {

            throw new RuntimeException("Ошибка при чтении данных из файла: " + e.getMessage(), e);

        } catch (Exception e) {

            throw new RuntimeException("Ошибка при импорте: " + e.getMessage(), e);
        }

        return importCount;
    }
}