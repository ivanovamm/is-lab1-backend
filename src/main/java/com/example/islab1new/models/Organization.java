package com.example.islab1new.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String name;


    @Column(nullable = false)
    private String creationDate;
    // TODO: 26.11.2024 потом исправить на LocalDateTime

    @Min(value = 0)
    @Column(nullable = false)
    private double annualTurnover;

    @Min(value = 0)
    @Column(nullable = false)
    private Long employeesCount;

    @Min(value = 0)
    @Column(nullable = false)
    private int rating;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrganizationType type;

    @ManyToOne
    @JoinColumn(name = "official_address_id",  referencedColumnName = "id")
    private Address officialAddress;

    @ManyToOne
    @JoinColumn(name = "postal_address_id",  referencedColumnName = "id")
    private Address postalAddress;

    @ManyToOne
    @JoinColumn(name = "coordinates_id",  referencedColumnName = "id")
    private Coordinates coordinates;

    @NotNull
    @Column(name = "creator_id")
    private Integer creatorId;

}
