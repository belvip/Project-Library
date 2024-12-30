package com.library.system.repository.impl;

import com.library.system.repository.BookRepository;  // Corriger l'importation
import com.library.system.exception.bookDaoException.BookAddException;
import com.library.system.model.Book;

import java.sql.*;

public class BookRepositoryImpl implements BookRepository {

    private Connection connection;

    public BookRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addBook(Book book) {
        String query = "INSERT INTO Book (title, number_of_copies) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getNumber_Of_Copies());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Récupérer l'ID généré automatiquement après l'insertion
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        book.setBook_id(generatedKeys.getInt(1));  // Attribuer l'ID généré
                    }
                }
            } else {
                throw new BookAddException("Aucun livre n'a été ajouté.");
            }
        } catch (SQLException e) {
            throw new BookAddException("Erreur lors de l'ajout du livre : " + e.getMessage());
        }
    }

}
