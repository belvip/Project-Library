package com.library.system.service.impl;

import com.library.system.exception.authorException.AuthorAlreadyExistException;
import com.library.system.model.Author;
import com.library.system.repository.AuthorRepository;
import com.library.system.service.AuthorService;

import java.sql.SQLException;

public class AuthorServiceImpl implements AuthorService {
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    private AuthorRepository authorRepository;

    @Override
    public void createAuthor(Author author) throws SQLException, AuthorAlreadyExistException {
        // Vérification si l'auteur existe déjà via l'email
        if (authorRepository.doesAuthorExist(author.getAuthor_email())) {
            throw new AuthorAlreadyExistException("L'auteur avec l'email : " + author.getAuthor_email() + " existe déjà.");
        } else {
            authorRepository.createAuthor(author);
        }
    }

}
