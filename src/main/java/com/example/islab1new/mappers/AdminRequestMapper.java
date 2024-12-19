package com.example.islab1new.mappers;

import com.example.islab1new.dto.AdminRequestsDTO;
import com.example.islab1new.dto.OrganizationDTO;
import com.example.islab1new.models.AdminRequests;

public class AdminRequestMapper {
    public static AdminRequestsDTO toDTO(AdminRequests adminRequests){
        AdminRequestsDTO dto = new AdminRequestsDTO();
        dto.setId(adminRequests.getId());
        dto.setUserId(adminRequests.getUser().getId());
        dto.setStatus(adminRequests.getStatus());
        return dto;
    }

}
