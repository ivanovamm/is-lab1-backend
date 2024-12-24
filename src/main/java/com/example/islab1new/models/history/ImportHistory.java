package com.example.islab1new.models.history;

import com.example.islab1new.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "import_history")
public class ImportHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "status")
    private Status status;

    @NotNull
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "import_count")
    private Integer importCount;

}
