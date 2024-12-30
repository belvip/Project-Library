package com.library.system.service.impl;

import com.library.system.service.BookService;
import com.library.system.repository.BookRepository;
import com.library.system.model.Book;
import com.library.system.exception.bookDaoException.BookAddException;

public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    // Constructor avec injection de BookRepository
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void addBook(Book book) {
        try {
            bookRepository.addBook(book);  // Délégation à BookRepositoryImpl
        } catch (BookAddException e) {
            throw new BookAddException("Erreur lors de l'ajout du livre dans le service : " + e.getMessage());
        }
    }
}
