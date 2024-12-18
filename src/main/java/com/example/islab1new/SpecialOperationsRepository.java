package com.example.islab1new;

import com.example.islab1new.dao.AddressDAO;
import com.example.islab1new.dto.MergeOrganizationDTO;
import com.example.islab1new.models.Address;
import com.example.islab1new.models.Organization;
import com.example.islab1new.models.OrganizationType;
import jakarta.ejb.Local;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class SpecialOperationsRepository {

    @Inject
    private AddressDAO addressDAO;

    @PersistenceContext
    private EntityManager em;

    public Double calculateAverageRating() {
        return em.createQuery("SELECT AVG(o.rating) FROM Organization o", Double.class).getSingleResult();
    }

    public List<Organization> findByPostalAddressGreaterThan(int threshold) {
        return em.createQuery("SELECT o FROM Organization o WHERE o.postalAddress.id > :threshold", Organization.class)
                .setParameter("threshold", threshold)
                .getResultList();
    }

    public List<OrganizationType> findUniqueTypes() {
        return em.createQuery("SELECT DISTINCT o.type FROM Organization o", OrganizationType.class).getResultList();
    }

    public void mergeOrganizations(int orgId1, int orgId2, String newName, Integer newAddressId) {
        Organization org1 = em.find(Organization.class, orgId1);
        Organization org2 = em.find(Organization.class, orgId2);

        if (org1 == null || org2 == null) {
            throw new IllegalArgumentException("One of the organizations does not exist");
        }

        Organization newOrg = new Organization();
        newOrg.setName(newName);
        Address newAddress =  addressDAO.findById(newAddressId);
        newOrg.setCoordinates(org1.getCoordinates());
        newOrg.setOfficialAddress(newAddress);
        newOrg.setPostalAddress(newAddress);
        newOrg.setEmployeesCount(org1.getEmployeesCount() + org2.getEmployeesCount());
        newOrg.setAnnualTurnover(org1.getAnnualTurnover() + org2.getAnnualTurnover());
        newOrg.setRating((org1.getRating() + org2.getRating()) / 2);
        newOrg.setType(org1.getType());
        newOrg.setCreatorId(org1.getCreatorId());
        newOrg.setCreationDate(LocalDateTime.now().toString());

        em.persist(newOrg);

        em.remove(org1);
        em.remove(org2);
    }

    public void absorbOrganization(int absorberId, int absorbedId) {
        Organization absorber = em.find(Organization.class, absorberId);
        Organization absorbed = em.find(Organization.class, absorbedId);
        System.out.println(absorbedId + " "+ absorberId);
        if (absorber == null || absorbed == null) {
            throw new IllegalArgumentException("One of the organizations does not exist");
        }

        absorber.setEmployeesCount(absorber.getEmployeesCount() + absorbed.getEmployeesCount());
        absorber.setAnnualTurnover(absorber.getAnnualTurnover() + absorbed.getAnnualTurnover());
        absorber.setRating((absorber.getRating() + absorbed.getRating()) / 2);

        em.remove(absorbed);
    }
}

