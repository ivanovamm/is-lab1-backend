package com.example.islab1new.dto;

import com.example.islab1new.models.Address;
import lombok.Getter;
import lombok.Setter;

@Setter

@Getter
public class MergeOrganizationDTO {
    private Integer orgId1;
    private Integer orgId2;
    private String newName;
    private Integer newAddress;


}
