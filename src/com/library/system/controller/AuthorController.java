package com.library.system.controller;

import com.library.system.model.Author;
import com.library.system.service.AuthorService;
import com.library.system.service.impl.AuthorServiceImpl;
import com.library.system.exception.authorException.AuthorNotFoundException;
import com.library.system.exception.authorException.AuthorEmailAlreadyExistsException;
import com.library.system.util.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AuthorController {
    private AuthorService authorService;

    public AuthorController(Connection connection) {
        this.authorService = new AuthorServiceImpl(connection);
    }

    // Méthode pour créer un auteur
    public Author createAuthor(String first_name, String last_name, String author_email) {
        Author author = new Author(first_name, last_name, author_email);
        try {
            return authorService.createAuthor(author);
        } catch (AuthorEmailAlreadyExistsException e) {
            Logger.logWarn("Création de l'auteur", "L'email existe déjà : " + author_email);
            return null;  // Retourne null pour signaler l'échec
        } catch (Exception e) { // Capture toutes les autres erreurs
            Logger.logError("Création de l'auteur", e);
            return null;
        }
    }


    // Méthode pour récupérer un auteur par ID
    public Author getAuthorById(int author_id) {
        try {
            return authorService.findAuthorById(author_id);
        } catch (AuthorNotFoundException e) {
            //System.out.println("Auteur non trouvé : " + e.getMessage());
            Logger.logError("Auteur non trouvé : " , e);
            return null;
        }
    }

    // Méthode pour récupérer un auteur par email
    public Author getAuthorByEmail(String author_email) {
        try {
            return authorService.findAuthorByEmail(author_email);
        } catch (AuthorNotFoundException e) {
            //System.out.println("❌ Auteur non trouvé : " + e.getMessage());
            Logger.logError("Auteur non trouvé : " , e);
            return null;
        }
    }

    // Méthode pour afficher tous les auteurs
    public List<Author> displayAuthors() {
        return authorService.displayAuthors();
    }

    // Méthode pour supprimer un auteur
    public boolean deleteAuthor(int author_id) {
        try {
            return authorService.deleteAuthor(author_id);
        } catch (AuthorNotFoundException e) {
            Logger.logError("Erreur lors de la suppression de l'auteur : " + e.getMessage());
            return false;
        }
    }
}
