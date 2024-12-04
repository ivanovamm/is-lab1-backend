package com.example.islab1new.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Coordinates {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @NotNull
    @Column(name = "x")
    private Long x;

    @Min(value = -506)
    @Column(name = "y")
    private int y;


    @NotNull
    @Column(name = "creator_id")
    private Integer creatorId;

    @NotNull
    @Column(name = "creation_date")
    private String creationDate;


}
