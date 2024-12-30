package com.library.system.dao.impl;

import com.library.system.dao.BookDAO;
import com.library.system.model.Book;
import com.library.system.exception.bookDaoException.*;
import com.library.system.model.Category;
import com.library.system.model.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    /* ========================== AFFICHER LES LIVRES ========================= */
    @Override
    public List<Book> displayAvailableBooks() throws BookDisplayException {
        List<Book> availableBooks = new ArrayList<>();
        String query = "SELECT b.book_id, b.title, b.number_of_copies, a.author_id, a.first_name, a.last_name, a.author_email, c.category_id, c.category_name " +
                "FROM Book b " +
                "LEFT JOIN Book_Author ba ON b.book_id = ba.book_id " +
                "LEFT JOIN Author a ON ba.author_id = a.author_id " +
                "LEFT JOIN Books_Category bc ON b.book_id = bc.book_id " +
                "LEFT JOIN Category c ON bc.category_id = c.category_id " +
                "WHERE b.number_of_copies > 0";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                Book book = new Book();
                book.setBook_id(bookId);
                book.setTitle(rs.getString("title"));
                book.setNumber_Of_Copies(rs.getInt("number_of_copies"));

                // Récupérer les auteurs (vérifier si l'auteur est NULL)
                Set<Author> authors = new HashSet<>();
                if (rs.getInt("author_id") != 0) {  // Vérifiez que l'ID de l'auteur est valide
                    Author author = new Author();
                    author.setAuthor_id(rs.getInt("author_id"));
                    author.setFirst_name(rs.getString("first_name"));
                    author.setLast_name(rs.getString("last_name"));
                    author.setAuthor_email(rs.getString("author_email") != null ? rs.getString("author_email") : "Email non disponible");
                    authors.add(author);
                }
                book.setAuthors(authors);

                // Ajouter le nom complet de l'auteur dans l'objet Book, si nécessaire
                String authorFullName = authors.stream()
                        .map(author -> author.getFirst_name() + " " + author.getLast_name())
                        .collect(Collectors.joining(", "));
                book.setAuthorFullName(authorFullName);  // Assurez-vous que vous avez cette méthode dans Book

                // Récupérer les catégories (vérifier si la catégorie est NULL)
                Set<Category> categories = new HashSet<>();
                if (rs.getInt("category_id") != 0) {  // Vérifiez que l'ID de la catégorie est valide
                    Category category = new Category();
                    category.setCategory_id(rs.getInt("category_id"));
                    category.setCategory_name(rs.getString("category_name") != null ? rs.getString("category_name") : "Aucune catégorie");
                    categories.add(category);
                }
                book.setCategories(categories);

                availableBooks.add(book);
            }
        } catch (SQLException e) {
            throw new BookDisplayException("Erreur lors de l'affichage des livres disponibles : " + e.getMessage());
        }

        return availableBooks;
    }



    // Afficher un livre par son ID;
    @Override
    public Book displayBookById(int bookId) throws BookDisplayException {
        Book book = null;  // Créer un objet Book pour stocker les informations
        String query = "SELECT " +
                "b.book_id AS id_du_livre, " +
                "b.title AS titre_du_livre, " +
                "b.number_of_copies AS nombre_de_copies, " +
                "CONCAT(a.first_name, ' ', a.last_name) AS nom_auteur, " +
                "a.author_email AS author_email, " +
                "c.category_name AS categorie " +
                "FROM Book b " +
                "LEFT JOIN Book_Author ba ON b.book_id = ba.book_id " +
                "LEFT JOIN Author a ON ba.author_id = a.author_id " +
                "LEFT JOIN Books_Category bc ON b.book_id = bc.book_id " +
                "LEFT JOIN Category c ON bc.category_id = c.category_id " +
                "WHERE b.book_id = ?";  // Utiliser un paramètre pour l'ID du livre

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Remplir le paramètre de la requête avec l'ID du livre
            stmt.setInt(1, bookId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Mapper les résultats de la requête à un objet Book
                    book = new Book();
                    book.setBook_id(rs.getInt("id_du_livre"));
                    book.setTitle(rs.getString("titre_du_livre"));
                    book.setNumber_Of_Copies(rs.getInt("nombre_de_copies"));

                    // Récupérer l'auteur et son email
                    Author author = new Author();
                    author.setFirst_name(rs.getString("nom_auteur").split(" ")[0]);
                    author.setLast_name(rs.getString("nom_auteur").split(" ")[1]);
                    author.setAuthor_email(rs.getString("author_email"));

                    // Ajouter l'auteur à l'objet Book
                    Set<Author> authors = new HashSet<>();
                    authors.add(author);
                    book.setAuthors(authors);

                    // Récupérer la catégorie
                    Category category = new Category();
                    category.setCategory_name(rs.getString("categorie"));
                    Set<Category> categories = new HashSet<>();
                    categories.add(category);
                    book.setCategories(categories);
                }
            }
        } catch (SQLException e) {
            throw new BookDisplayException("Erreur lors de l'affichage du livre: " + e.getMessage());
        }

        return book;
    }






}
