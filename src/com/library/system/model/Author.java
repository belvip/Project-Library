package com.library.system.model;


import com.library.system.exception.authorException.InvalidAuthorEmailException;
import com.library.system.exception.authorException.InvalidAuthorIdException;
import com.library.system.exception.authorException.InvalidAuthorNameException;

/**
 * Classe représentant un auteur dans le système de gestion de bibliothèque.
 */
public class Author {
    private int author_id;
    private String first_name;
    private String last_name;
    private String author_email;

    // ================== Constructeurs ==================

    public Author() {}

    public Author(int author_id, String first_name, String last_name, String author_email) {
        setAuthor_id(author_id);
        setFirst_name(first_name);
        setLast_name(last_name);
        setAuthor_email(author_email);
    }

    public Author(String first_name, String last_name, String author_email) {
        setFirst_name(first_name);
        setLast_name(last_name);
        setAuthor_email(author_email);
    }

    // ================== Getters et Setters ==================

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        if (author_id <= 0) {
            throw new InvalidAuthorIdException("L'identifiant de l'auteur doit être un entier positif.");
        }
        this.author_id = author_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        if (first_name == null || first_name.trim().isEmpty()) {
            throw new InvalidAuthorNameException("Le prénom ne peut pas être vide.");
        }
        if (first_name.length() > 50) {
            throw new InvalidAuthorNameException("Le prénom ne peut pas dépasser 50 caractères.");
        }
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        if (last_name == null || last_name.trim().isEmpty()) {
            throw new InvalidAuthorNameException("Le nom de famille ne peut pas être vide.");
        }
        if (last_name.length() > 50) {
            throw new InvalidAuthorNameException("Le nom de famille ne peut pas dépasser 50 caractères.");
        }
        this.last_name = last_name;
    }

    public String getAuthor_email() {
        return author_email;
    }

    public void setAuthor_email(String author_email) {
        if (author_email == null || author_email.trim().isEmpty()) {
            throw new InvalidAuthorEmailException("L'email ne peut pas être vide.");
        }
        if (!author_email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new InvalidAuthorEmailException("L'email n'est pas valide.");
        }
        this.author_email = author_email;

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Author author = (Author) obj;
        return author_email != null && author_email.equals(author.author_email);
    }

    @Override
    public int hashCode() {
        return author_email != null ? author_email.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Author{" +
                "author_id=" + author_id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", author_email='" + author_email + '\'' +
                '}';
    }

}
