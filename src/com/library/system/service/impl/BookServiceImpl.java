package com.library.system.service.impl;

import com.library.system.model.Book;
import com.library.system.repository.BookRepository;
import com.library.system.service.BookService;
import com.library.system.exception.bookDaoException.*;

import java.util.List;

public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    // Constructeur pour injecter le référentiel
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void addBook(Book book) throws BookAddException {
        if (book == null) {
            throw new BookAddException("Le livre à ajouter ne peut pas être null.");
        }
        bookRepository.addBook(book);
    }

    @Override
    public void updateBook(Book book) throws BookUpdateException {
        if (book == null) {
            throw new BookUpdateException("Le livre à mettre à jour ne peut pas être null.");
        }
        bookRepository.updateBook(book);
    }

    @Override
    public void removeBook(int bookId) throws BookRemoveException {
        if (bookId <= 0) {
            throw new BookRemoveException("L'ID du livre à supprimer doit être supérieur à zéro.");
        }
        bookRepository.removeBook(bookId);
    }

    @Override
    public Book getBookById(int bookId) throws BookGetByIdServiceException {
        if (bookId <= 0) {
            throw new BookGetByIdServiceException("L'ID du livre doit être supérieur à zéro.");
        }
        return bookRepository.getBookById(bookId);
    }

    @Override
    public List<Book> displayAvailableBooks() throws BookDisplayException {
        List<Book> books = bookRepository.displayAvailableBooks();
        if (books == null || books.isEmpty()) {
            throw new BookDisplayException("Aucun livre disponible trouvé.");
        }
        return books;
    }

    @Override
    public List<Book> searchBookByTitle(String title) throws BookSearchByTitleException {
        if (title == null || title.trim().isEmpty()) {
            throw new BookSearchByTitleException("Le titre à rechercher ne peut pas être vide.");
        }
        List<Book> books = bookRepository.searchBookByTitle(title);
        if (books == null || books.isEmpty()) {
            throw new BookSearchByTitleException("Aucun livre trouvé pour le titre : " + title);
        }
        return books;
    }

    @Override
    public List<Book> searchBookByCategory(String category) throws BookSearchByCategoryException {
        if (category == null || category.trim().isEmpty()) {
            throw new BookSearchByCategoryException("La catégorie à rechercher ne peut pas être vide.", null);
        }
        List<Book> books = bookRepository.searchBookByCategory(category);
        if (books == null || books.isEmpty()) {
            throw new BookSearchByCategoryException("Aucun livre trouvé pour la catégorie : " + category, null);
        }
        return books;
    }


    @Override
    public boolean isAvailable(int bookId) throws BookAvailabilityException {
        if (bookId <= 0) {
            throw new BookAvailabilityException("L'ID du livre doit être supérieur à zéro.");
        }
        return bookRepository.isAvailable(bookId);
    }
}
