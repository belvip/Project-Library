package com.library.system.dao.impl;

import com.library.system.dao.AuthorDAO;
import com.library.system.exception.authorException.AuthorAlreadyExistsException;
import com.library.system.exception.authorException.AuthorNotFoundException;
import com.library.system.model.Author;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAOImpl implements AuthorDAO {
    private final Connection connection;

    public AuthorDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createAuthor(Author author) throws SQLException, AuthorAlreadyExistsException {
        // Vérification si l'auteur existe déjà (exemple basé sur l'email)
        String checkQuery = "SELECT COUNT(*) FROM Author WHERE author_email = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, author.getAuthor_email());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new AuthorAlreadyExistsException("L'auteur avec l'email " + author.getAuthor_email() + " existe déjà.");
            }
        }

        // Si l'auteur n'existe pas, on continue avec l'ajout
        String query = "INSERT INTO Author (first_name, last_name, author_email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, author.getFirst_name());
            stmt.setString(2, author.getLast_name());
            stmt.setString(3, author.getAuthor_email());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Author> displayAuthors() throws SQLException, AuthorNotFoundException {
        List<Author> authors = new ArrayList<>();
        String query = "SELECT * FROM Author";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Author author = new Author();
                author.setAuthor_id(rs.getInt("author_id"));
                author.setFirst_name(rs.getString("first_name"));
                author.setLast_name(rs.getString("last_name"));
                author.setAuthor_email(rs.getString("author_email"));
                authors.add(author);
            }
        }

        // Si la liste des auteurs est vide, lancer l'exception
        if (authors.isEmpty()) {
            throw new AuthorNotFoundException("Aucun auteur trouvé dans la base de données.");
        }

        return authors;
    }

    @Override
    public void deleteAuthor(int authorId) throws SQLException {
        String query = "DELETE FROM Author WHERE author_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, authorId);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Auteur supprimé avec succès.");
            } else {
                System.out.println("Aucun auteur trouvé avec cet ID.");
            }
        }
    }

    @Override
    public Author findAuthorById(int authorId) throws SQLException {
        String query = "SELECT * FROM Author WHERE author_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, authorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Author(
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("author_email")
                    );
                } else {
                    System.out.println("Aucun auteur trouvé avec cet ID.");
                    return null;
                }
            }
        }
    }
}
