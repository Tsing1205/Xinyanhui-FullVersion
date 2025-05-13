package com.example.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "Supervisors")
@Data
@AllArgsConstructor
public class Supervisor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer supervisorId;

    private String name;
    private String professionalInfo;
    private String password;
    private String salt;
    private String token;
    private Boolean employed=true;

    public Supervisor() {
        this.employed=true;
    }

}
