package com.library.system.service;

import com.library.system.exception.bookDaoException.BookDisplayException;
import com.library.system.model.Book;

import java.util.List;

public interface BookService {
    void addBook(Book book);  // Ajouter un livre
    // Méthode pour afficher les livres disponibles
    List<Book> displayAvailableBooks() throws BookDisplayException;

    Book getBookById(int bookId) throws BookDisplayException;
    List<Book> getAvailableBooks();
    Book displayBookById(int bookId) throws BookDisplayException;

}
