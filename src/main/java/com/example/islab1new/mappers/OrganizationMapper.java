package com.example.islab1new.mappers;

import com.example.islab1new.dto.OrganizationDTO;
import com.example.islab1new.models.Organization;

public class OrganizationMapper {

    public static OrganizationDTO toDTO(Organization organization) {
        OrganizationDTO dto = new OrganizationDTO();
        dto.setId(organization.getId());
        dto.setName(organization.getName());
        dto.setCoordinates(organization.getCoordinates() != null ? organization.getCoordinates().getId() : null);
        dto.setPostalAddress(organization.getPostalAddress() != null ? organization.getPostalAddress().getId() : null);
        dto.setOfficialAddress(organization.getOfficialAddress() != null ? organization.getOfficialAddress().getId() : null);
        dto.setAnnualTurnover(organization.getAnnualTurnover());
        dto.setEmployeesCount(organization.getEmployeesCount());
        dto.setRating(organization.getRating());
        dto.setType(organization.getType() != null ? organization.getType() : null);
        dto.setCreatorId(organization.getCreatorId());
        dto.setCreationDate(organization.getCreationDate());
        return dto;
    }
}
