package com.library.system.model;

/**
 * Classe représentant un auteur dans le système de gestion de bibliothèque.
 */
public class Author {
    // Attributs privés pour représenter les propriétés de l'auteur
    private int author_id; // Identifiant unique de l'auteur
    private String first_name; // Prénom de l'auteur
    private String last_name; // Nom de famille de l'auteur
    private String author_email; // Adresse email de l'auteur

    // ================== Constructeurs ==================

    /**
     * Constructeur par défaut.
     */
    public Author() {}

    /**
     * Constructeur avec paramètres pour initialiser les attributs.
     *
     * @param first_name   Prénom de l'auteur.
     * @param last_name    Nom de famille de l'auteur.
     * @param author_email Email de l'auteur.
     */
    public Author(String first_name, String last_name, String author_email) {
        setFirst_name(first_name); // Utilise les setters pour inclure les validations
        setLast_name(last_name);
        setAuthor_email(author_email);
    }

    // ================== Getters et Setters ==================

    /**
     * Retourne l'identifiant unique de l'auteur.
     *
     * @return author_id.
     */
    public int getAuthor_id() {
        return author_id;
    }

    /**
     * Définit l'identifiant unique de l'auteur.
     *
     * @param author_id Identifiant de l'auteur.
     */
    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    /**
     * Retourne le prénom de l'auteur.
     *
     * @return first_name.
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * Définit le prénom de l'auteur avec validation.
     *
     * @param first_name Prénom de l'auteur.
     * @throws IllegalArgumentException si le prénom est null, vide ou dépasse 50 caractères.
     */
    public void setFirst_name(String first_name) {
        if (first_name == null || first_name.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
        }
        if (first_name.length() > 50) {
            throw new IllegalArgumentException("Le prénom ne peut pas dépasser 50 caractères.");
        }
        this.first_name = first_name;
    }

    /**
     * Retourne le nom de famille de l'auteur.
     *
     * @return last_name.
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * Définit le nom de famille de l'auteur avec validation.
     *
     * @param last_name Nom de famille de l'auteur.
     * @throws IllegalArgumentException si le nom de famille est null, vide ou dépasse 50 caractères.
     */
    public void setLast_name(String last_name) {
        if (last_name == null || last_name.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de famille ne peut pas être vide.");
        }
        if (last_name.length() > 50) {
            throw new IllegalArgumentException("Le nom de famille ne peut pas dépasser 50 caractères.");
        }
        this.last_name = last_name;
    }

    /**
     * Retourne l'email de l'auteur.
     *
     * @return author_email.
     */
    public String getAuthor_email() {
        return author_email;
    }

    /**
     * Définit l'email de l'auteur avec validation.
     *
     * @param author_email Email de l'auteur.
     * @throws IllegalArgumentException si l'email est null, vide ou invalide.
     */
    public void setAuthor_email(String author_email) {
        if (author_email == null || author_email.trim().isEmpty()) {
            throw new IllegalArgumentException("L'email ne peut pas être vide.");
        }
        // Regex pour valider l'email
        if (!author_email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("L'email n'est pas valide.");
        }
        this.author_email = author_email;
    }
}
