package com.library.system.controller;

import com.library.system.model.Author;
import com.library.system.service.AuthorService;
import com.library.system.service.impl.AuthorServiceImpl;
import com.library.system.exception.authorException.AuthorNotFoundException;
import com.library.system.exception.authorException.AuthorEmailAlreadyExistsException;

import java.sql.Connection;
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
            System.out.println("Erreur lors de la création de l'auteur : " + e.getMessage());
            return null;
        }
    }

    // Méthode pour récupérer un auteur par ID
    public Author getAuthorById(int author_id) {
        try {
            return authorService.findAuthorById(author_id);
        } catch (AuthorNotFoundException e) {
            System.out.println("Auteur non trouvé : " + e.getMessage());
            return null;
        }
    }

    // Méthode pour récupérer un auteur par email
    public Author getAuthorByEmail(String author_email) {
        try {
            return authorService.findAuthorByEmail(author_email);
        } catch (AuthorNotFoundException e) {
            System.out.println("Auteur non trouvé : " + e.getMessage());
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
            System.out.println("Erreur lors de la suppression de l'auteur : " + e.getMessage());
            return false;
        }
    }
}
