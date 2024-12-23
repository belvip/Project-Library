package com.library.system.service.impl;

import com.library.system.model.Author;
import com.library.system.repository.AuthorRepository;
import com.library.system.repository.impl.AuthorRepositoryImpl;
import com.library.system.service.AuthorService;

import java.sql.Connection;
import java.sql.SQLException;

public class AuthorServiceImpl {
    /*private final AuthorRepository authorRepository;

    public AuthorServiceImpl(Connection connection) {
        this.authorRepository = new AuthorRepositoryImpl(connection);
    }

    @Override
    public void createAuthor(Author author) throws SQLException, AuthorAlreadyExistException {
        // Vérification si l'auteur existe déjà via l'email
        if (authorRepository.doesAuthorExist(author.getAuthor_email())) {
            throw new AuthorAlreadyExistException("L'auteur avec l'email : " + author.getAuthor_email() + " existe déjà.");
        } else {
            authorRepository.createAuthor(author);
        }
    }*/

}
