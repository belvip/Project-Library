package com.library.system.controller;

import com.library.system.dao.BookDAO;
import com.library.system.dao.impl.BookDAOImpl;
import com.library.system.exception.bookDaoException.BookDisplayException;
import com.library.system.repository.impl.BookRepositoryImpl;
import com.library.system.service.BookService;
import com.library.system.service.impl.BookServiceImpl;
import com.library.system.model.Book;
import com.library.system.exception.bookDaoException.BookAddException;
import java.sql.Connection;
import java.util.List;

public class BookController {

    private BookService bookService;

    // Constructeur avec injection de connexion et initialisation du BookService
    public BookController(Connection connection) {
        // Créer une instance de BookDAOImpl pour la passer à BookRepositoryImpl
        BookDAO bookDAO = new BookDAOImpl(connection);
        this.bookService = new BookServiceImpl(new BookRepositoryImpl(connection, bookDAO));
    }

    // Méthode pour ajouter un livre
    public void addBook(Book book) {
        try {
            bookService.addBook(book);
        } catch (BookAddException e) {
            System.out.println("Erreur lors de l'ajout du livre : " + e.getMessage());
        }
    }

    // Méthode pour afficher un livre spécifique par son ID
    public Book displayAvailableBooks(int bookId) throws BookDisplayException {
        return bookService.getBookById(bookId); // Appel au service pour obtenir un livre par ID
    }

    // Méthode pour afficher les livres disponibles

    public List<Book> displayAvailableBooks() throws BookDisplayException {
        try {
            return bookService.displayAvailableBooks();  // Appel au service pour obtenir tous les livres
        } catch (BookDisplayException e) {
            throw new BookDisplayException("Erreur lors de l'affichage des livres disponibles.");
        }
    }



}
