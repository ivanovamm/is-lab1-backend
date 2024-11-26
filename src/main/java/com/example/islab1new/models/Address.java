package com.example.islab1new.models;

import com.example.islab1new.services.UserService;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "address")
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String street;

    public Address() {}

    @NotNull
    @Column(name = "creator_id")
    private Integer creatorId;



    @NotNull
    @Column(name = "creation_date")
    private String creationDate;
    

}
