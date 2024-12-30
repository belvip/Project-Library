package com.library.system.controller;

import com.library.system.repository.impl.BookRepositoryImpl;
import com.library.system.service.BookService;
import com.library.system.service.impl.BookServiceImpl;
import com.library.system.model.Book;
import com.library.system.exception.bookDaoException.BookAddException;
import java.sql.Connection;

public class BookController {

    private BookService bookService;

    // Constructeur avec injection de connexion et initialisation du BookService
    public BookController(Connection connection) {
        this.bookService = new BookServiceImpl(new BookRepositoryImpl(connection));
    }

    // Méthode pour ajouter un livre
    public void addBook(Book book) {
        try {
            bookService.addBook(book);
        } catch (BookAddException e) {
            System.out.println("Erreur lors de l'ajout du livre : " + e.getMessage());
        }
    }
}
