package com.example.islab1new.models.history;

import com.example.islab1new.models.Coordinates;
import com.example.islab1new.models.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "coordinates_history")
public class CoordinatesHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Coordinates_id")
    private Integer coordinates;

    @Column(name = "action")
    private Action action;

    @Column(name = "action_date")
    private String actionDate;

    @Column(name = "user_id")
    private Integer userId;

}
