package com.example.islab1new.services;

import com.example.islab1new.SpecialOperationsRepository;
import com.example.islab1new.models.Address;
import com.example.islab1new.models.Organization;
import com.example.islab1new.models.OrganizationType;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class SpecialOperationsService {

    @Inject
    private SpecialOperationsRepository repository;

    public Double getAverageRating() {
        return repository.calculateAverageRating();
    }

    public List<Organization> getOrganizationsWithPostalAddressGreaterThan(int threshold) {
        return repository.findByPostalAddressGreaterThan(threshold);
    }

    public List<OrganizationType> getUniqueOrganizationTypes() {
        return repository.findUniqueTypes();
    }

    public void mergeOrganizations(int orgId1, int orgId2, String newName, Integer address) {
        repository.mergeOrganizations(orgId1, orgId2, newName, address);
    }

    public void absorbOrganization(int absorberId, int absorbedId) {
        repository.absorbOrganization(absorberId, absorbedId);
    }
}
