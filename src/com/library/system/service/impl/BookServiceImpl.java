package com.library.system.service.impl;

import com.library.system.exception.bookDaoException.BookDisplayException;
import com.library.system.exception.bookDaoException.BookRemoveException;
import com.library.system.exception.bookDaoException.BookUpdateException;
import com.library.system.service.BookService;
import com.library.system.repository.BookRepository;
import com.library.system.model.Book;
import com.library.system.exception.bookDaoException.BookAddException;

import java.util.Collections;
import java.util.List;

public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    // Constructor avec injection de BookRepository
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Implémentation de la méthode displayAvailableBooks()
    @Override
    public List<Book> displayAvailableBooks() throws BookDisplayException {
        try {
            return bookRepository.displayAvailableBooks();
        } catch (BookDisplayException e) {
            System.err.println("Erreur: " + e.getMessage());
            return Collections.emptyList();  // Retourner une liste vide en cas d'erreur
        }
    }

    // Vous pouvez également garder votre méthode getAvailableBooks(), mais assurez-vous qu'elle soit cohérente
    public List<Book> getAvailableBooks() {
        try {
            return displayAvailableBooks();  // Appel de displayAvailableBooks()
        } catch (BookDisplayException e) {
            System.err.println("Erreur: " + e.getMessage());
            return Collections.emptyList();  // Retourner une liste vide en cas d'erreur
        }
    }

    @Override
    public Book displayBookById(int bookId) throws BookDisplayException {
        return bookRepository.displayBookById(bookId); // Délégation au repository
    }

    // Implémentation de la méthode updateBook
    @Override
    public void updateBook(Book book) throws BookUpdateException {
        try {
            // Appeler la méthode updateBook du repository
            bookRepository.updateBook(book);
        } catch (BookUpdateException e) {
            // Si une exception survient, la relancer avec un message plus spécifique
            throw new BookUpdateException("Erreur lors de la mise à jour du livre avec l'ID : " + book.getBook_id(), e);
        }
    }

    @Override
    public void removeBook(int bookId) throws BookRemoveException {
        try{
            bookRepository.removeBook(bookId);
        }catch (Exception e) {
            throw new BookRemoveException("Erreur lors de la suppression du livre.", e);
        }
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

    @Override
    public Book getBookById(int bookId) throws BookDisplayException {
        try {
            return bookRepository.getBookById(bookId);  // Appel au repository pour obtenir un livre par ID
        } catch (Exception e) {
            throw new BookDisplayException("Erreur lors de la récupération du livre.", e);
        }
    }


}
