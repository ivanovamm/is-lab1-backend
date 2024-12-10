package com.example.islab1new.models.history;

import com.example.islab1new.models.Organization;
import com.example.islab1new.models.User;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "organization_history")
public class OrganizationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "organization_id",  referencedColumnName = "id")
    private Organization organization;

    @Column(name = "action")
    private Action action;

    @Column(name = "action_date")
    private String actionDate;

    @Column(name="user_id")
    private Integer userId;

}
