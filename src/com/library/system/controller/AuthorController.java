package com.library.system.controller;

import com.library.system.model.Author;
import com.library.system.service.AuthorService;

import java.sql.SQLException;

public class AuthorController {
    /*private final AuthorService authorService;

    // Constructeur pour initialiser le service
    /*public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // Méthode pour créer un nouvel auteur
    public void createAuthor(Author author) throws AuthorAlreadyExistException {
        try {
            authorService.createAuthor(author);
            System.out.println("Auteur créé avec succès : " + author.getFirst_name() + " " + author.getLast_name());
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'auteur : " + e.getMessage());
        }
    }


    /* Méthode pour afficher tous les auteurs
    public void listAuthors() {
        try {
            List<Author> authors = authorService.findAllAuthors();
            if (authors.isEmpty()) {
                System.out.println("Aucun auteur trouvé.");
            } else {
                authors.forEach(author -> System.out.println(author));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des auteurs : " + e.getMessage());
        }
    }

    // Méthode pour supprimer un auteur
    public void deleteAuthor(int authorId) {
        try {
            authorService.deleteAuthor(authorId);
            System.out.println("Auteur supprimé avec succès : ID = " + authorId);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'auteur : " + e.getMessage());
        } catch (RuntimeException e) { // Pour gérer une exception personnalisée comme AuthorNotFoundException
            System.err.println(e.getMessage());
        }
    }

    // Méthode pour trouver un auteur par ID
    public void findAuthorById(int authorId) {
        try {
            Author author = authorService.findAuthorById(authorId);
            if (author != null) {
                System.out.println("Auteur trouvé : " + author);
            } else {
                System.out.println("Auteur introuvable avec l'ID : " + authorId);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'auteur : " + e.getMessage());
        }
    } */
}
