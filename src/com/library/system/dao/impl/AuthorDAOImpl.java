package com.library.system.dao.impl;

import com.library.system.dao.AuthorDAO;
import com.library.system.model.Author;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuthorDAOImpl implements AuthorDAO {

    private final Connection connection;

    public AuthorDAOImpl(Connection connection) {
        this.connection = connection;
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
