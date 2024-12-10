package com.example.islab1new.models.history;

import com.example.islab1new.models.Address;
import com.example.islab1new.models.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "address_history")
public class AddressHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "address_id",  referencedColumnName = "id")
    private Address address;

    @Column(name = "action")
    private Action action;

    @Column(name = "action_date")
    private String actionDate;

    @Column(name = "user_id")
    private Integer userId;

}
