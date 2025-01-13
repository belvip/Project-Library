package com.library.system.service.impl;

import com.library.system.repository.AuthorRepository;
import com.library.system.repository.impl.AuthorRepositoryImpl;
import com.library.system.model.Author;
import com.library.system.exception.authorException.AuthorNotFoundException;
import com.library.system.exception.authorException.AuthorEmailAlreadyExistsException;
import com.library.system.service.AuthorService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;
    private Connection connection;  // Déclarez la variable connection

    // Constructeur pour initialiser la connexion et l'auteurRepository
    public AuthorServiceImpl(Connection connection) {
        this.connection = connection;  // Initialisez la variable connection
        this.authorRepository = new AuthorRepositoryImpl(connection);  // Passez la connexion à AuthorRepositoryImpl
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

    @Override
    public Author getAuthorById(int authorId) {
        // Requête SQL pour trouver l'auteur par son ID
        String query = "SELECT * FROM Author WHERE author_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Passer l'ID de l'auteur dans la requête
            stmt.setInt(1, authorId);

            // Exécuter la requête
            ResultSet rs = stmt.executeQuery();

            // Si l'auteur est trouvé, créer et retourner un objet Author
            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String authorEmail = rs.getString("author_email");

                // Retourner l'auteur récupéré de la base de données
                return new Author(authorId, firstName, lastName, authorEmail);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Log l'erreur et retourne null en cas d'exception
        }

        // Si l'auteur n'est pas trouvé, retourner null
        return null;
    }
}
