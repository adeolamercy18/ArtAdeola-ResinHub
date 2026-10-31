package com.example.artadeola.entity;

import jakarta.persistence.*;

@Entity
public class Customer {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)

    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private Long phoneNumber;
    @Column(nullable = false)
    private String password;

    public Customer(){
    }

    public Customer(Long id, String name, String email, Long phoneNumber, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {

        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }
}
