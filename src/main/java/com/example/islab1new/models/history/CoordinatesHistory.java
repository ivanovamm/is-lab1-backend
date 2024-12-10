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

    @ManyToOne
    @JoinColumn(name = "Coordinates_id",  referencedColumnName = "id")
    private Coordinates coordinates;

    @Column(name = "action")
    private Action action;

    @Column(name = "action_date")
    private String actionDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
