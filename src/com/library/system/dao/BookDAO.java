
package com.library.system.dao;

import com.library.system.exception.bookDaoException.BookDisplayException;
import com.library.system.exception.bookDaoException.BookRemoveException;
import com.library.system.exception.bookDaoException.BookSearchByCategoryException;
import com.library.system.exception.bookDaoException.BookUpdateException;
import com.library.system.model.Book;

import java.sql.SQLException;
import java.util.List;

public interface BookDAO {
    Book findBookById(int bookId) throws BookDisplayException;
    void addBook(Book book);  // Ajouter un livre
    List<Book> displayAvailableBooks() throws BookDisplayException;  // Afficher les livres
    Book displayBookById(int bookId) throws BookDisplayException;
    void updateBook(Book book) throws BookUpdateException;

    void removeBook(int bookId) throws BookRemoveException;
    List<Book> searchBookByCategory(String categoryName) throws BookSearchByCategoryException;



    void borrowBook(int bookId) throws BookUpdateException;
}
