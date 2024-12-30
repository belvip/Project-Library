package com.library.system.repository.impl;

import com.library.system.dao.BookDAO;
import com.library.system.exception.bookDaoException.BookAddException;
import com.library.system.exception.bookDaoException.BookDisplayException;
import com.library.system.model.Author;
import com.library.system.model.Book;
import com.library.system.model.Category;
import com.library.system.repository.BookRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {

    private Connection connection;
    private final BookDAO bookDAO;

    public BookRepositoryImpl(Connection connection, BookDAO bookDAO) {
        this.connection = connection;
        this.bookDAO = bookDAO;
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

    /* ========================= AFFICHER LES LIVRES ============================ */
    @Override
    public List<Book> displayAvailableBooks() throws BookDisplayException {
        try {
            return bookDAO.displayAvailableBooks();
        } catch (Exception e) {
            throw new BookDisplayException("Erreur lors de l'affichage des livres disponibles.", e);
        }
    }

    @Override
    public Book displayBookById(int bookId) throws BookDisplayException {
        try{
            return bookDAO.displayBookById(bookId);
        }catch (Exception e) {
            throw new BookDisplayException("Erreur lors de l'affichage des livres disponibles.", e);
        }
    }

    @Override
    public Book getBookById(int bookId) throws Exception {
        // Logique pour récupérer un livre par ID à partir de la base de données
        String query = "SELECT * FROM book WHERE book_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Mapper les données du résultat dans un objet Book
                Book book = new Book();
                book.setBook_id(resultSet.getInt("book_id"));
                book.setTitle(resultSet.getString("title"));
                // Mapper d'autres champs du livre...
                return book;
            } else {
                return null;  // Aucun livre trouvé avec l'ID spécifié
            }
        }
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM book"; // Remplacez "books" par le nom de votre table

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Book book = new Book();
                book.setBook_id(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                // Définir les autres propriétés du livre ici
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'exception selon votre logique
        }

        return books;
    }



}
