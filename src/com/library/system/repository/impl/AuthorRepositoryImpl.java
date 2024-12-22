package com.library.system.repository.impl;

import com.library.system.model.Author;
import com.library.system.repository.AuthorRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorRepositoryImpl implements AuthorRepository {

    private final Connection connection;

    public AuthorRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean doesAuthorExist(String email) throws SQLException {
        String query = "SELECT 1 FROM Author WHERE author_email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Retourne true si un résultat existe
            }
        }
    }


    @Override
    public void createAuthor(Author author) throws SQLException {
        String query = "INSERT INTO Author (first_name, last_name, author_email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, author.getFirst_name());
            stmt.setString(2, author.getLast_name());
            stmt.setString(3, author.getAuthor_email());
            stmt.executeUpdate();


        }

    }
}
