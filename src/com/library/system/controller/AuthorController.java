
package com.library.system.controller;

import com.library.system.model.Author;
import com.library.system.service.AuthorService;
import com.library.system.service.impl.AuthorServiceImpl;
import com.library.system.exception.authorException.AuthorAlreadyExistsException;
import com.library.system.exception.authorException.AuthorNotFoundException;
import com.library.system.exception.authorException.AuthorDeleteException;

import java.sql.SQLException;
import java.util.List;

public class AuthorController {

    private AuthorService authorService;


    public AuthorController(AuthorServiceImpl authorService) {
        this.authorService = authorService;
    }


    public void addAuthor(Author author) {
        try {
            authorService.createAuthor(author);
            System.out.println("Auteur ajouté avec succès !");
        } catch (AuthorAlreadyExistsException | SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Author> showAuthors() {
        try {
            List<Author> authors = authorService.displayAuthors();
            authors.forEach(author -> System.out.println(author.getFirst_name() + " " + author.getLast_name()));
        } catch (AuthorNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeAuthor(int authorId) {
        try {
            authorService.deleteAuthor(authorId);
            System.out.println("Auteur supprimé avec succès !");
        } catch (AuthorDeleteException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour rechercher un auteur par ID
    public void findAuthorById(int authorId) {
        try {
            Author author = authorService.findAuthorById(authorId);
            System.out.println("Auteur trouvé: " + author.getFirst_name() + " " + author.getLast_name());
        } catch (AuthorNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour rechercher un auteur par email
    public void findAuthorByEmail(String email) {
        try {
            Author author = authorService.findByEmail(email);
            System.out.println("Auteur trouvé: " + author.getFirst_name() + " " + author.getLast_name());
        } catch (AuthorNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
