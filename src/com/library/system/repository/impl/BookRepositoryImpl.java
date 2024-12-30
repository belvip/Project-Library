package com.library.system.repository.impl;

import com.library.system.repository.BookRepository;
import com.library.system.model.Book;
import com.library.system.model.Author;
import com.library.system.exception.bookDaoException.*;
import com.library.system.model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookRepositoryImpl implements BookRepository {

    private Connection connection;

    public BookRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addBook(Book book) throws BookAddException {
        String query = "INSERT INTO book (title, authors, categories, number_of_copies, available) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, book.getTitle());

            // Conversion des auteurs en chaîne de caractères
            String authors = book.getAuthors().stream()
                    .map(Author::getFirst_name) // Utilisation de getFirst_name() pour obtenir le prénom de l'auteur
                    .collect(Collectors.joining(", "));
            stmt.setString(2, authors);

            // Conversion des catégories en chaîne de caractères
            String categories = book.getCategories().stream()
                    .map(Category::getCategory_name) // Utilisation de getCategory_name() pour obtenir le nom de la catégorie
                    .collect(Collectors.joining(", "));
            stmt.setString(3, categories);

            // Ajout de l'attribut number_Of_Copies dans la requête
            stmt.setInt(4, book.getNumber_Of_Copies());

            // Utilisation de l'état de disponibilité
            stmt.setBoolean(5, book.isAvailable());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BookAddException("Erreur lors de l'ajout du livre : " + book.getTitle(), e);
        }
    }


    @Override
    public List<Book> displayAvailableBooks() throws BookDisplayException {
        List<Book> availableBooks = new ArrayList<>();
        String query = "SELECT * FROM book WHERE available = true";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                availableBooks.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            throw new BookDisplayException("Erreur lors de l'affichage des livres disponibles.", e);
        }
        return availableBooks;
    }

    @Override
    public void updateBook(Book book) throws BookUpdateException {
        String query = "UPDATE book SET title = ?, authors = ?, categories = ?, available = ? WHERE book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, book.getTitle());

            // Convertir Set<Author> en une chaîne de caractères
            String authors = book.getAuthors().stream()
                    .map(Author::getName) // Supposons que vous ayez une méthode getName() dans la classe Author
                    .collect(Collectors.joining(", "));
            stmt.setString(2, authors);

            // Convertir Set<Category> en une chaîne de caractères
            String categories = book.getCategories().stream()
                    .map(Category::getCategory_name) // Utiliser getCategory_name() pour obtenir le nom de la catégorie
                    .collect(Collectors.joining(", "));
            stmt.setString(3, categories);

            stmt.setBoolean(4, book.isAvailable());
            stmt.setInt(5, book.getBook_id());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BookUpdateException("Erreur lors de la mise à jour du livre avec l'ID : " + book.getBook_id(), e);
        }
    }


    @Override
    public void removeBook(int bookId) throws BookRemoveException {
        String query = "DELETE FROM book WHERE book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BookRemoveException("Erreur lors de la suppression du livre avec l'ID : " + bookId, e);
        }
    }

    @Override
    public boolean isAvailable(int bookId) throws BookAvailabilityException {
        String query = "SELECT available FROM book WHERE book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("available");
                }
            }
        } catch (SQLException e) {
            throw new BookAvailabilityException("Erreur lors de la vérification de la disponibilité du livre avec l'ID : " + bookId, e);
        }
        return false;
    }


    @Override
    public Book getBookById(int bookId) throws BookGetByIdException {
        String query = "SELECT * FROM book WHERE book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }
        } catch (SQLException e) {
            // Lancer l'exception avec uniquement le message, sans la SQLException
            throw new BookGetByIdException("Livre avec l'ID " + bookId + " introuvable.");
        }
        return null;
    }


    @Override
    public List<Book> searchBookByTitle(String title) throws BookSearchByTitleException {
        List<Book> booksByTitle = new ArrayList<>();
        String query = "SELECT * FROM book WHERE title LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + title + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    booksByTitle.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            throw new BookSearchByTitleException("Erreur lors de la recherche du livre avec le titre : " + title, e);
        }
        return booksByTitle;
    }

    @Override
    public List<Book> searchBookByCategory(String category) throws BookSearchByCategoryException {
        List<Book> booksByCategory = new ArrayList<>();
        String query = "SELECT * FROM book WHERE category_name LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + category + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    booksByCategory.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            throw new BookSearchByCategoryException("Erreur lors de la recherche de livres dans la catégorie : " + category, e);
        }
        return booksByCategory;
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        int bookId = rs.getInt("book_id");
        String title = rs.getString("title");
        String authorsStr = rs.getString("authors"); // Chaîne contenant les auteurs, formatée selon vos données
        String categoriesStr = rs.getString("categories"); // Chaîne contenant les catégories
        boolean available = rs.getBoolean("available");

        Book book = new Book();
        book.setBook_id(bookId);
        book.setTitle(title);

        // Convertir les noms des auteurs en un Set<Author>
        Set<Author> authors = new HashSet<>();
        if (authorsStr != null && !authorsStr.isEmpty()) {
            String[] authorsArray = authorsStr.split(", ");
            for (String authorInfo : authorsArray) {
                // Supposons que chaque auteur est représenté sous la forme "prénom nom email"
                String[] parts = authorInfo.split(" ");
                if (parts.length >= 3) {
                    String firstName = parts[0];
                    String lastName = parts[1];
                    String email = parts[2];
                    authors.add(new Author(firstName, lastName, email));
                } else {
                    throw new SQLException("Les informations de l'auteur sont incomplètes : " + authorInfo);
                }
            }
        }
        book.setAuthors(authors);

        // Convertir les catégories en un Set<Category>
        Set<Category> categories = new HashSet<>();
        if (categoriesStr != null && !categoriesStr.isEmpty()) {
            String[] categoriesArray = categoriesStr.split(", ");
            for (String categoryName : categoriesArray) {
                categories.add(new Category(categoryName)); // Assurez-vous que Category a un constructeur prenant un nom
            }
        }
        book.setCategories(categories);

        book.setAvailable(available);

        return book;
    }


}
