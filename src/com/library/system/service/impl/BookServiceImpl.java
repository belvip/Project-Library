package com.library.system.service.impl;

import com.library.system.model.Book;
import com.library.system.repository.BookRepository;
import com.library.system.repository.impl.BookRepositoryImpl;
import com.library.system.service.BookService;

import java.util.List;

public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    // Constructeur
    public BookServiceImpl() {
        this.bookRepository = new BookRepositoryImpl(); // Instanciation du repository
    }

    @Override
    public void addBook(Book book) {
        bookRepository.addBook(book);
    }

    @Override
    public List<Book> displayAvailableBooks() {
        return bookRepository.displayAvailableBooks();
    }

    @Override
    public void updateBook(Book book) {
        bookRepository.updateBook(book);
    }

    @Override
    public void removeBook(int bookId) {
        bookRepository.removeBook(bookId);
    }

    @Override
    public boolean isAvailable(int bookId) {
        return bookRepository.isAvailable(bookId);
    }

    @Override
    public List<Book> searchBookByTitle(String title) {
        return bookRepository.searchBookByTitle(title);
    }

    @Override
    public List<Book> searchBookByCategory(String category) {
        return bookRepository.searchBookByCategory(category);
    }
}
