package com.library.system.service;

import com.library.system.model.Author;
import com.library.system.exception.authorException.AuthorAlreadyExistsException;
import com.library.system.exception.authorException.AuthorNotFoundException;
import com.library.system.exception.authorException.AuthorDeleteException;

import java.sql.SQLException;
import java.util.List;

public interface AuthorService {
    void createAuthor(Author author) throws SQLException, AuthorAlreadyExistsException;
    List<Author> displayAuthors() throws SQLException, AuthorNotFoundException;
    void deleteAuthor(int authorId) throws SQLException, AuthorDeleteException;
    Author findAuthorById(int authorId) throws SQLException, AuthorNotFoundException;
    Author findByEmail(String email) throws SQLException, AuthorNotFoundException;
}
