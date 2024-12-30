package com.library.system.service;

import com.library.system.exception.bookDaoException.*;
import com.library.system.model.Book;
//import com.library.system.exception.bookServiceException.*;
import java.util.List;

public interface BookService {

    void addBook(Book book) throws BookAddException;

    void updateBook(Book book) throws BookUpdateException;

    void removeBook(int bookId) throws BookRemoveException;

    Book getBookById(int bookId) throws BookGetByIdServiceException;

    List<Book> displayAvailableBooks() throws BookDisplayException;

    List<Book> searchBookByTitle(String title) throws BookSearchByTitleException;

    List<Book> searchBookByCategory(String category) throws BookSearchByCategoryException;

    boolean isAvailable(int bookId) throws BookAvailabilityException;
}
