package com.example.islab1new.dto;

import com.example.islab1new.models.OrganizationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationDTO {
    private String name;
    private Integer coordinates;
    private Integer officialAddress;
    private Integer annualTurnover;
    private Long employeesCount;
    private int rating;
    private OrganizationType type;
    private Integer postalAddress;
}
