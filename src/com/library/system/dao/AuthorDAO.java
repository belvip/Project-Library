package com.library.system.dao;

import com.library.system.model.Author;

import java.util.List;

/**
 * Interface pour les opérations CRUD (Create, Read, Update, Delete) sur les auteurs.
 */
public interface AuthorDAO {

    /**
     * Crée un nouvel auteur dans la base de données.
     *
     * @param author L'auteur à ajouter.
     * @return L'auteur ajouté avec son identifiant généré.
     */
    Author createAuthor(Author author);

    /**
     * Affiche tous les auteurs.
     *
     * @return Une liste d'auteurs.
     */
    List<Author> displayAuthors();

    /**
     * Supprime un auteur de la base de données par son identifiant.
     *
     * @param author_id L'identifiant de l'auteur à supprimer.
     * @return true si l'auteur a été supprimé avec succès, false sinon.
     */
    boolean deleteAuthor(int author_id);

    /**
     * Recherche un auteur par son identifiant.
     *
     * @param author_id L'identifiant de l'auteur à rechercher.
     * @return L'auteur trouvé, ou null si aucun auteur n'a été trouvé.
     */
    Author findAuthorById(int author_id);

    /**
     * Recherche un auteur par son adresse email.
     *
     * @param author_email L'email de l'auteur à rechercher.
     * @return L'auteur trouvé, ou null si aucun auteur n'a été trouvé.
     */
    Author findAuthorByEmail(String author_email);
}
