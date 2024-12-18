package com.example.islab1new.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;

@Getter
@Setter
public class AbsorbOrganizationDTO {
    private Integer absorberId;
    private Integer absorbedId;
}
