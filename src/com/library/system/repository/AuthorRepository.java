package com.library.system.repository;

import com.library.system.model.Author;

import java.sql.SQLException;

public interface AuthorRepository {
    // Verifier si l'uteur existe
    boolean doesAuthorExist(String email) throws SQLException;
    // Ajouter un auteur
    void createAuthor(Author author) throws SQLException;
}
