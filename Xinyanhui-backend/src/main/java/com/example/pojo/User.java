package com.example.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Table(name = "Users")
@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String username;

    @Column(name = "password_Hashwithsalt")
    private String password;
    private String salt;
    private String email;
    private String phone;
    private Date register_date;

    private String token;

    public User() {
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(Integer user_id, String username, String password, String salt, String email, String phone, Date register_date) {
        this.userId = user_id;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.email = email;
        this.phone = phone;
        this.register_date = register_date;
    }

}
