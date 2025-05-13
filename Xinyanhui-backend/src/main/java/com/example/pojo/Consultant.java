package com.example.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name="Consultants")
@Data
@AllArgsConstructor
public class Consultant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer consultantId;

    private String name;
    private String professionalInfo;
    private String password;
    private String salt;
    private String token;
    private Integer supervisorId;
    private Boolean employed=true;

    public Consultant() {
        // Default constructor
        this.employed=true;
    }

}
