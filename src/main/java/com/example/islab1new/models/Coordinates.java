package com.example.islab1new.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
public class Coordinates {

    @NotNull
    @Column(name = "x")
    private Long x;

    @Min(value = -506)
    @Column(name = "y")
    private int y;


    @Id
    private Integer id;

    @NotNull
    @Column(name = "creator_id")
    private Long creatorId;

    @NotNull
    @Column(name = "creation_date")
    private LocalDateTime creationDate;


}
