package com.library.system.service.impl;

import com.library.system.exception.bookDaoException.BookDisplayException;
import com.library.system.service.BookService;
import com.library.system.repository.BookRepository;
import com.library.system.model.Book;
import com.library.system.exception.bookDaoException.BookAddException;

import java.util.List;

public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    // Constructor avec injection de BookRepository
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /* =========================== AJOUTER UN LIVRE ======================== */
    @Override
    public void addBook(Book book) {
        try {
            bookRepository.addBook(book);  // Délégation à BookRepositoryImpl
        } catch (BookAddException e) {
            throw new BookAddException("Erreur lors de l'ajout du livre dans le service : " + e.getMessage());
        }
    }

    /* =========================== AFFICHER UN LIVRE ======================== */
    public List<Book> displayAvailableBooks() throws BookDisplayException {
        try {
            List<Book> books = bookRepository.findAll(); // Assurez-vous que findAll() fonctionne
            return books;
        } catch (Exception e) {
            throw new BookDisplayException("Erreur lors de l'affichage des livres disponibles.");
        }
    }

    @Override
    public Book getBookById(int bookId) throws BookDisplayException {
        try {
            return bookRepository.getBookById(bookId);  // Appel au repository pour obtenir un livre par ID
        } catch (Exception e) {
            throw new BookDisplayException("Erreur lors de la récupération du livre.", e);
        }
    }
}
