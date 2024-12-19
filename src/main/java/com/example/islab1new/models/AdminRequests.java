package com.example.islab1new.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "admin_requests")
@Getter
@Setter
public class AdminRequests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false,  referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private String status;
}