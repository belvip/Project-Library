package com.library.system.repository;

import com.library.system.exception.bookDaoException.BookDisplayException;
import com.library.system.model.Book;

import java.util.List;

public interface BookRepository {
    void addBook(Book book);  // Ajouter un livre

    List<Book> displayAvailableBooks() throws BookDisplayException; // Afficher les livres

    Book getBookById(int bookId) throws Exception;

    List<Book> findAll();

    Book displayBookById(int bookId) throws BookDisplayException;

}
