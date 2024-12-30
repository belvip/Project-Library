package com.library.system.dao.impl;

import com.library.system.dao.BookDAO;
import com.library.system.model.Book;
import com.library.system.exception.bookDaoException.*;
import com.library.system.model.Category;
import com.library.system.model.Author;

import java.sql.*;

public class BookDAOImpl implements BookDAO {

    private Connection connection;

    public BookDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addBook(Book book) {
        String bookQuery = "INSERT INTO Book (book_id, title, number_of_copies) VALUES (?, ?, ?)";
        String authorQuery = "INSERT INTO Book_Author (book_id, author_id) VALUES (?, ?)";
        String categoryQuery = "INSERT INTO Books_Category (book_id, category_id) VALUES (?, ?)";

        try {
            // Start a transaction
            connection.setAutoCommit(false);

            // 1. Ajouter le livre dans la table Book
            try (PreparedStatement psBook = connection.prepareStatement(bookQuery)) {
                psBook.setInt(1, book.getBook_id());  // Ensure the ID is generated if not auto-incremented
                psBook.setString(2, book.getTitle());
                psBook.setInt(3, book.getNumber_Of_Copies());
                psBook.executeUpdate();
            } catch (SQLException e) {
                connection.rollback();
                throw new BookAddException("Erreur lors de l'ajout du livre : " + e.getMessage());
            }

            // 2. Ajouter les auteurs et les lier au livre
            for (Author author : book.getAuthors()) {
                addOrUpdateAuthor(author);
                linkAuthorToBook(book.getBook_id(), author);
            }

            // 3. Ajouter les catégories et les lier au livre
            for (Category category : book.getCategories()) {
                addOrUpdateCategory(category);
                linkCategoryToBook(book.getBook_id(), category);
            }

            // Commit the transaction
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();  // Rollback in case of any error
            } catch (SQLException rollbackEx) {
                throw new BookAddException("Erreur lors du rollback : " + rollbackEx.getMessage());
            }
            throw new BookAddException("Erreur générale lors de l'ajout du livre : " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);  // Restore default auto-commit mode
            } catch (SQLException e) {
                throw new BookAddException("Erreur lors de la réinitialisation du mode auto-commit : " + e.getMessage());
            }
        }
    }

    private void addOrUpdateAuthor(Author author) {
        String checkAuthorQuery = "SELECT * FROM Author WHERE author_email = ?";
        String insertAuthorQuery = "INSERT INTO Author (first_name, last_name, author_email) VALUES (?, ?, ?)";

        try (PreparedStatement psCheckAuthor = connection.prepareStatement(checkAuthorQuery)) {
            psCheckAuthor.setString(1, author.getAuthor_email());
            ResultSet rsAuthor = psCheckAuthor.executeQuery();
            if (!rsAuthor.next()) {
                try (PreparedStatement psInsertAuthor = connection.prepareStatement(insertAuthorQuery)) {
                    psInsertAuthor.setString(1, author.getFirst_name());
                    psInsertAuthor.setString(2, author.getLast_name());
                    psInsertAuthor.setString(3, author.getAuthor_email());
                    psInsertAuthor.executeUpdate();
                } catch (SQLException e) {
                    throw new BookAddException("Erreur lors de l'ajout de l'auteur : " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            throw new BookAddException("Erreur lors de la vérification de l'auteur : " + e.getMessage());
        }
    }

    private void linkAuthorToBook(int bookId, Author author) {
        String getAuthorIdQuery = "SELECT author_id FROM Author WHERE author_email = ?";
        try (PreparedStatement psGetAuthorId = connection.prepareStatement(getAuthorIdQuery)) {
            psGetAuthorId.setString(1, author.getAuthor_email());
            ResultSet rsAuthorId = psGetAuthorId.executeQuery();
            if (rsAuthorId.next()) {
                int authorId = rsAuthorId.getInt("author_id");
                try (PreparedStatement psAuthor = connection.prepareStatement("INSERT INTO Book_Author (book_id, author_id) VALUES (?, ?)")) {
                    psAuthor.setInt(1, bookId);
                    psAuthor.setInt(2, authorId);
                    psAuthor.executeUpdate();
                } catch (SQLException e) {
                    throw new BookAddException("Erreur lors de l'ajout de l'auteur au livre : " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            throw new BookAddException("Erreur lors de la récupération de l'id de l'auteur : " + e.getMessage());
        }
    }

    private void addOrUpdateCategory(Category category) {
        String checkCategoryQuery = "SELECT * FROM Category WHERE category_name = ?";
        String insertCategoryQuery = "INSERT INTO Category (category_name) VALUES (?)";

        try (PreparedStatement psCheckCategory = connection.prepareStatement(checkCategoryQuery)) {
            psCheckCategory.setString(1, category.getCategory_name());
            ResultSet rsCategory = psCheckCategory.executeQuery();
            if (!rsCategory.next()) {
                try (PreparedStatement psInsertCategory = connection.prepareStatement(insertCategoryQuery)) {
                    psInsertCategory.setString(1, category.getCategory_name());
                    psInsertCategory.executeUpdate();
                } catch (SQLException e) {
                    throw new BookAddException("Erreur lors de l'ajout de la catégorie : " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            throw new BookAddException("Erreur lors de la vérification de la catégorie : " + e.getMessage());
        }
    }

    private void linkCategoryToBook(int bookId, Category category) {
        String getCategoryIdQuery = "SELECT category_id FROM Category WHERE category_name = ?";
        try (PreparedStatement psGetCategoryId = connection.prepareStatement(getCategoryIdQuery)) {
            psGetCategoryId.setString(1, category.getCategory_name());
            ResultSet rsCategoryId = psGetCategoryId.executeQuery();
            if (rsCategoryId.next()) {
                int categoryId = rsCategoryId.getInt("category_id");
                try (PreparedStatement psCategory = connection.prepareStatement("INSERT INTO Books_Category (book_id, category_id) VALUES (?, ?)")) {
                    psCategory.setInt(1, bookId);
                    psCategory.setInt(2, categoryId);
                    psCategory.executeUpdate();
                } catch (SQLException e) {
                    throw new BookAddException("Erreur lors de l'ajout de la catégorie au livre : " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            throw new BookAddException("Erreur lors de la récupération de l'id de la catégorie : " + e.getMessage());
        }
    }
}
