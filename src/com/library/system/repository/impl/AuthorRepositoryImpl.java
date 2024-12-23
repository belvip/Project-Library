package com.library.system.repository.impl;

import com.library.system.repository.AuthorRepository;
import com.library.system.dao.AuthorDAO;
import com.library.system.dao.impl.AuthorDAOImpl;
import com.library.system.model.Author;
import com.library.system.exception.authorException.AuthorAlreadyExistsException;
import com.library.system.exception.authorException.AuthorNotFoundException;
import com.library.system.exception.authorException.AuthorDeleteException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AuthorRepositoryImpl implements AuthorRepository {

    private final AuthorDAO authorDAO;

    // Constructeur qui prend une connexion et initialise l'instance d'AuthorDAO
    public AuthorRepositoryImpl(Connection connection) {
        this.authorDAO = new AuthorDAOImpl(connection);
    }

    @Override
    public void createAuthor(Author author) throws SQLException, AuthorAlreadyExistsException {
        authorDAO.createAuthor(author);
    }

    @Override
    public List<Author> displayAuthors() throws SQLException, AuthorNotFoundException {
        return authorDAO.displayAuthors();
    }

    @Override
    public void deleteAuthor(int authorId) throws SQLException, AuthorDeleteException {
        authorDAO.deleteAuthor(authorId);
    }

    @Override
    public Author findAuthorById(int authorId) throws SQLException, AuthorNotFoundException {
        return authorDAO.findAuthorById(authorId);
    }

    @Override
    public Author findByEmail(String email) throws SQLException, AuthorNotFoundException {
        return authorDAO.findByEmail(email);
    }
}
