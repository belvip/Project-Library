package com.library.system.service.impl;

import com.library.system.model.Author;
import com.library.system.repository.AuthorRepository;
import com.library.system.repository.impl.AuthorRepositoryImpl;
import com.library.system.exception.authorException.AuthorAlreadyExistsException;
import com.library.system.exception.authorException.AuthorNotFoundException;
import com.library.system.exception.authorException.AuthorDeleteException;
import com.library.system.service.AuthorService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    // Constructeur qui prend une connexion et initialise l'instance d'AuthorRepository
    public AuthorServiceImpl(Connection connection) {
        this.authorRepository = new AuthorRepositoryImpl(connection);
    }



    @Override
    public void createAuthor(Author author) throws SQLException, AuthorAlreadyExistsException {
        try {
            // Vérification de l'existence de l'auteur par email avant de le créer
            Author existingAuthor = authorRepository.findByEmail(author.getAuthor_email());

            // Si l'auteur existe déjà, on lance une exception AuthorAlreadyExistsException
            if (existingAuthor != null) {
                throw new AuthorAlreadyExistsException("L'auteur avec l'email " + author.getAuthor_email() + " existe déjà.");
            }

            // Si l'auteur n'existe pas, on peut procéder à la création de l'auteur
            authorRepository.createAuthor(author);

        } catch (AuthorNotFoundException e) {
            // Cette exception ne devrait pas être levée ici, mais on la capture au cas où
            // (elle est gérée pour des vérifications supplémentaires si nécessaire).
            // On peut éventuellement loguer l'exception ou gérer un cas particulier ici.
            // Par exemple, ici, on pourrait choisir de relancer une autre exception si nécessaire.
            System.out.println("Erreur: Auteur non trouvé lors de la vérification.");
        }
    }


    @Override
    public List<Author> displayAuthors() throws SQLException, AuthorNotFoundException {
        List<Author> authors = authorRepository.displayAuthors();
        if (authors.isEmpty()) {
            throw new AuthorNotFoundException("Aucun auteur trouvé.");
        }
        return authors;
    }

    @Override
    public void deleteAuthor(int authorId) throws SQLException, AuthorDeleteException {
        try {
            // Logique métier avant de supprimer l'auteur
            Author author = authorRepository.findAuthorById(authorId);

            // Si l'auteur n'est pas trouvé, on lance une exception AuthorDeleteException
            if (author == null) {
                throw new AuthorDeleteException("Auteur non trouvé avec l'ID : " + authorId);
            }

            // Suppression de l'auteur
            authorRepository.deleteAuthor(authorId);

        } catch (AuthorNotFoundException e) {
            // Si l'exception AuthorNotFoundException est lancée, on peut la gérer ici
            // Par exemple, on peut relancer une exception différente ou la loguer
            throw new AuthorDeleteException("Erreur lors de la suppression : Auteur non trouvé.", e);
        }
    }


    @Override
    public Author findAuthorById(int authorId) throws SQLException, AuthorNotFoundException {
        Author author = authorRepository.findAuthorById(authorId);
        if (author == null) {
            throw new AuthorNotFoundException("Aucun auteur trouvé avec l'ID : " + authorId);
        }
        return author;
    }

    @Override
    public Author findByEmail(String email) throws SQLException, AuthorNotFoundException {
        Author author = authorRepository.findByEmail(email);
        if (author == null) {
            throw new AuthorNotFoundException("Aucun auteur trouvé avec l'email : " + email);
        }
        return author;
    }
}
