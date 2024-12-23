package com.library.system.repository;

import com.library.system.model.Author;
import com.library.system.exception.authorException.AuthorAlreadyExistsException;
import com.library.system.exception.authorException.AuthorNotFoundException;
import com.library.system.exception.authorException.AuthorDeleteException;

import java.sql.SQLException;
import java.util.List;

public interface AuthorRepository {
    // Ajouter un auteur
    void createAuthor(Author author) throws SQLException, AuthorAlreadyExistsException;

    // Afficher tous les auteurs
    List<Author> displayAuthors() throws SQLException, AuthorNotFoundException;

    // Supprimer un auteur par son ID
    void deleteAuthor(int authorId) throws SQLException, AuthorDeleteException;

    // Trouver un auteur par son ID
    Author findAuthorById(int authorId) throws SQLException, AuthorNotFoundException;

    // Méthode pour récupérer un auteur par son email
    Author findByEmail(String email) throws SQLException, AuthorNotFoundException;
}
