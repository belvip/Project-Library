package com.library.system.service;

import com.library.system.exception.authorException.AuthorAlreadyExistException;
import com.library.system.model.Author;

import java.sql.SQLException;

public interface AuthorService {
    // Ajouter un auteur
    void createAuthor(Author author) throws SQLException, AuthorAlreadyExistException;
}
