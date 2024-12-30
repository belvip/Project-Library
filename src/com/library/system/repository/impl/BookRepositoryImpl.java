package com.library.system.repository.impl;

import com.library.system.exception.bookDaoException.BookAddException;
import com.library.system.model.Author;
import com.library.system.model.Book;
import com.library.system.model.Category;
import com.library.system.repository.BookRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BookRepositoryImpl implements BookRepository {

    private Connection connection;

    public BookRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addBook(Book book) {
        String query = "INSERT INTO Book (title, number_of_copies) VALUES (?, ?)";
        String bookAuthorQuery = "INSERT INTO book_author (book_id, author_id) VALUES (?, ?)";
        String bookCategoryQuery = "INSERT INTO books_category (book_id, category_id) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Ajouter le livre
            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getNumber_Of_Copies());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Récupérer l'ID généré automatiquement pour le livre
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        book.setBook_id(generatedKeys.getInt(1)); // Attribuer l'ID généré
                    }
                }

                // Ajouter les relations dans book_author
                for (Author author : book.getAuthors()) {
                    try (PreparedStatement authorStmt = connection.prepareStatement(bookAuthorQuery)) {
                        authorStmt.setInt(1, book.getBook_id());   // ID du livre
                        authorStmt.setInt(2, author.getAuthor_id()); // ID de l'auteur
                        authorStmt.executeUpdate();
                    }
                }

                // Ajouter les relations dans books_category
                for (Category category : book.getCategories()) {
                    try (PreparedStatement categoryStmt = connection.prepareStatement(bookCategoryQuery)) {
                        categoryStmt.setInt(1, book.getBook_id());    // ID du livre
                        categoryStmt.setInt(2, category.getCategory_id()); // ID de la catégorie
                        categoryStmt.executeUpdate();
                    }
                }
            } else {
                throw new BookAddException("Aucun livre n'a été ajouté.");
            }
        } catch (SQLException e) {
            throw new BookAddException("Erreur lors de l'ajout du livre ou des relations : " + e.getMessage());
        }
    }
}
