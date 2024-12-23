package com.library.system.service.impl;

import com.library.system.repository.AuthorRepository;
import com.library.system.repository.impl.AuthorRepositoryImpl;
import com.library.system.model.Author;
import com.library.system.exception.authorException.AuthorNotFoundException;
import com.library.system.exception.authorException.AuthorEmailAlreadyExistsException;
import com.library.system.service.AuthorService;

import java.sql.Connection;
import java.util.List;

public class AuthorServiceImpl implements AuthorService {
    private AuthorRepository authorRepository;

    public AuthorServiceImpl(Connection connection) {
        this.authorRepository = new AuthorRepositoryImpl(connection);
    }

    @Override
    public Author createAuthor(Author author) {
        // Vérifier la validité de l'email et des autres informations si nécessaire
        try {
            return authorRepository.createAuthor(author);
        } catch (AuthorEmailAlreadyExistsException e) {
            // Gérer l'exception si l'email existe déjà
            throw new AuthorEmailAlreadyExistsException("Erreur : L'email existe déjà.");
        }
    }

    @Override
    public Author findAuthorById(int author_id) throws AuthorNotFoundException {
        return authorRepository.findAuthorById(author_id);
    }

    @Override
    public Author findAuthorByEmail(String author_email) throws AuthorNotFoundException {
        return authorRepository.findAuthorByEmail(author_email);
    }

    @Override
    public List<Author> displayAuthors() {
        return authorRepository.displayAuthors();
    }

    @Override
    public boolean deleteAuthor(int author_id) throws AuthorNotFoundException {
        return authorRepository.deleteAuthor(author_id);
    }
}
