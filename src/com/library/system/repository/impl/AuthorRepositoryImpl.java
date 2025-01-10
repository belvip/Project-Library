package com.library.system.repository.impl;

import com.library.system.repository.AuthorRepository;
import com.library.system.model.Author;
import com.library.system.exception.authorException.AuthorNotFoundException;
import com.library.system.exception.authorException.AuthorEmailAlreadyExistsException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepositoryImpl implements AuthorRepository {
    private Connection connection;

    public AuthorRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Author createAuthor(Author author) {
        String checkEmailQuery = "SELECT * FROM author WHERE author_email = ?";
        try (PreparedStatement checkEmailStmt = connection.prepareStatement(checkEmailQuery)) {
            checkEmailStmt.setString(1, author.getAuthor_email());
            ResultSet rs = checkEmailStmt.executeQuery();
            if (rs.next()) {
                throw new AuthorEmailAlreadyExistsException("⚠️ L'email de l'auteur existe déjà.");
            }

            String insertQuery = "INSERT INTO author (first_name, last_name, author_email) VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, author.getFirst_name());
                insertStmt.setString(2, author.getLast_name());
                insertStmt.setString(3, author.getAuthor_email());

                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            author.setAuthor_id(generatedKeys.getInt(1));
                        }
                    }
                }
                return author;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Erreur lors de la création de l'auteur.");
        }
    }

    @Override
    public Author findAuthorById(int author_id) throws AuthorNotFoundException {
        String query = "SELECT * FROM author WHERE author_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, author_id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("author_email");

                return new Author(author_id, firstName, lastName, email);
            } else {
                throw new AuthorNotFoundException("❌ Auteur non trouvé pour l'id : " + author_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Author findAuthorByEmail(String author_email) throws AuthorNotFoundException {
        String query = "SELECT * FROM author WHERE author_email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, author_email);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int author_id = resultSet.getInt("author_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                return new Author(author_id, firstName, lastName, author_email);
            } else {
                throw new AuthorNotFoundException("❌ Auteur non trouvé pour l'email : " + author_email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Author> displayAuthors() {
        List<Author> authors = new ArrayList<>();
        String query = "SELECT * FROM author";
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("author_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("author_email");

                Author author = new Author(id, firstName, lastName, email);
                authors.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }

    @Override
    public boolean deleteAuthor(int author_id) throws AuthorNotFoundException {
        String query = "SELECT * FROM author WHERE author_id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(query)) {
            checkStmt.setInt(1, author_id);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                throw new AuthorNotFoundException("❌ Auteur non trouvé pour l'id : " + author_id);
            }

            String deleteQuery = "DELETE FROM author WHERE author_id = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, author_id);
                int rowsAffected = deleteStmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
