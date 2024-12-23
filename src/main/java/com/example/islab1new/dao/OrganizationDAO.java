package com.example.islab1new.dao;

import com.example.islab1new.models.Address;
import com.example.islab1new.models.Coordinates;
import com.example.islab1new.models.history.Action;
import com.example.islab1new.models.history.OrganizationHistory;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.*;
import com.example.islab1new.models.Organization;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named
@RequestScoped
public class OrganizationDAO {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private CoordinatesDAO coordinatesDAO;

    @Inject
    private AddressDAO addressDAO;
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

    @Transactional
    public int importOrganizations(InputStream fileInputStream, Integer userId) throws Exception {
        List<Organization> organizations = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Organization[] importedOrganizations = objectMapper.readValue(fileInputStream, Organization[].class);
            organizations.addAll(Arrays.asList(importedOrganizations));

            for (Organization organization : organizations) {

                organization.setCreatorId(userId);
                organization.setCreationDate(LocalDateTime.now().toString());

                if (organization.getCoordinates() != null) {
                    if (organization.getCoordinates().getX() == null || organization.getCoordinates().getY() == null) {
                        throw new IllegalArgumentException("Координаты X и Y не могут быть null");
                    }
                    Coordinates coordinates = new Coordinates();
                    coordinates.setX(organization.getCoordinates().getX());
                    coordinates.setY(organization.getCoordinates().getY());
                    coordinates.setCreationDate(LocalDateTime.now().toString());
                    coordinates.setCreatorId(userId);
                    coordinatesDAO.save(coordinates, userId);
                    organization.setCoordinates(coordinates);

                } else {
                    throw new IllegalArgumentException("Координаты отсутствуют у организации: " + organization.getName());
                }

                if (organization.getOfficialAddress() == null || organization.getOfficialAddress().getStreet() == null) {
                    throw new IllegalArgumentException("Официальный адрес отсутствует или неполный у организации: " + organization.getName());
                }
                if (organization.getPostalAddress() == null || organization.getPostalAddress().getStreet() == null) {
                    throw new IllegalArgumentException("Почтовый адрес отсутствует или неполный у организации: " + organization.getName());
                }

                Address officialAddress = new Address();
                officialAddress.setStreet(organization.getOfficialAddress().getStreet());
                officialAddress.setCreationDate(LocalDateTime.now().toString());
                officialAddress.setCreatorId(userId);
                addressDAO.save(officialAddress,userId);
                organization.setOfficialAddress(officialAddress);
                Address postalAddress = new Address();
                postalAddress.setStreet(organization.getPostalAddress().getStreet());
                postalAddress.setCreationDate(LocalDateTime.now().toString());
                postalAddress.setCreatorId(userId);
                addressDAO.save(postalAddress, userId);
                organization.setPostalAddress(postalAddress);
                save(organization, userId);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении данных из файла: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при импорте: " + e.getMessage(), e);
        }

        return organizations.size();
    }


}
