
package com.library.system.model;

import com.library.system.exception.memberException.*;

import java.util.Date;
import java.util.Objects;

public class Member {
    private int member_id;
    private String firstName;
    private String lastName;
    private String email;
    private Date adhesionDate; // Utilisation de java.util.Date

    // Constructeurs
    public Member(int member_id, String firstName, String lastName, String email, Date adhesionDate) {
        if (member_id < 0) throw new InvalidMemberIdException("L'ID doit être positif.");
        if (firstName == null || firstName.trim().isEmpty()) throw new InvalidMemberNameException("Le prénom ne peut pas être vide.");
        if (lastName == null || lastName.trim().isEmpty()) throw new InvalidMemberNameException("Le nom ne peut pas être vide.");
        if (!com.library.system.util.EmailValidator.isValid(email)) throw new InvalidMemberEmailException("Email invalide.");
        if (adhesionDate == null || adhesionDate.after(new Date()))
            throw new InvalidMemberAdhesionDateException("La date d'adhésion ne peut pas être dans le futur.");

        this.member_id = member_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.adhesionDate = adhesionDate;
    }

    public Member(String firstName, String lastName, String email, Date adhesionDate) {
        this(0, firstName, lastName, email, adhesionDate);
    }

    public Member(String firstName, String lastName, String email) {
        this(firstName, lastName, email, new Date()); // Date actuelle par défaut
    }

    public int getMember_id() {
        return member_id;
    }

    public void setId(int member_id) {
        if (member_id < 0) throw new InvalidMemberIdException("L'ID doit être positif.");
        this.member_id = member_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new InvalidMemberNameException("Le prénom ne peut pas être vide.");
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new InvalidMemberNameException("Le nom ne peut pas être vide.");
        }
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!com.library.system.util.EmailValidator.isValid(email)) {
            throw new InvalidMemberEmailException("Email invalide.");
        }
        this.email = email;
    }

    public Date getAdhesionDate() {
        return new Date(adhesionDate.getTime()); // Retourner une copie pour éviter les modifications externes
    }

    public void setAdhesionDate(Date adhesionDate) {
        if (adhesionDate == null) {
            throw new InvalidMemberAdhesionDateException("Date d'adhésion invalide.");
        }

        // Comparaison uniquement sur la date (sans l'heure)
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        java.sql.Date providedDate = new java.sql.Date(adhesionDate.getTime());

        if (providedDate.after(today)) {
            throw new InvalidMemberAdhesionDateException("La date d'adhésion ne peut pas être dans le futur.");
        }

        this.adhesionDate = adhesionDate;
    }

    // Conversion vers java.sql.Date
    public java.sql.Date getSqlAdhesionDate() {
        return new java.sql.Date(adhesionDate.getTime());
    }

    // Comparaison basée sur l'email (car unique en base de données)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member member = (Member) obj;
        return Objects.equals(email, member.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + member_id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", adhesionDate=" + adhesionDate +
                '}';
    }
}
