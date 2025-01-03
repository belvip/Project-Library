package com.library.system.model;

import com.library.system.exception.member.*;

import java.util.Date;

public class Member {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private Date adhesionDate; // Utilisation de java.util.Date

    // Constructeurs
    public Member(int id, String firstName, String lastName, String email, Date adhesionDate) {
        this.setId(id);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setAdhesionDate(adhesionDate);
    }

    public Member(String firstName, String lastName, String email, Date adhesionDate) {
        this(0, firstName, lastName, email, adhesionDate);
    }

    public Member(String firstName, String lastName, String email) {
        this(firstName, lastName, email, new Date()); // Date actuelle par défaut
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) {
        if (id < 0) throw new InvalidMemberIdException("L'ID doit être positif.");
        this.id = id;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new InvalidMemberNameException("Le prénom ne peut pas être vide.");
        }
        this.firstName = firstName;
    }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new InvalidMemberNameException("Le nom ne peut pas être vide.");
        }
        this.lastName = lastName;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new InvalidMemberEmailException("Email invalide.");
        }
        this.email = email;
    }

    public Date getAdhesionDate() { return adhesionDate; }
    public void setAdhesionDate(Date adhesionDate) {
        if (adhesionDate == null || adhesionDate.after(new Date())) {
            throw new InvalidMemberAdhesionDateException("Date d'adhésion invalide.");
        }
        this.adhesionDate = adhesionDate;
    }

    // Conversion vers java.sql.Date
    public java.sql.Date getSqlAdhesionDate() {
        return new java.sql.Date(adhesionDate.getTime());
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", adhesionDate=" + adhesionDate +
                '}';
    }
}
