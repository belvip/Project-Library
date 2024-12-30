package com.library.system.dao;

import com.library.system.exception.bookDaoException.BookDisplayException;
import com.library.system.model.Book;

import java.util.List;

public interface BookDAO {
    void addBook(Book book);  // Ajouter un livre
    List<Book> displayAvailableBooks() throws BookDisplayException;  // Afficher les livres
    Book displayBookById(int bookId) throws BookDisplayException;
}
