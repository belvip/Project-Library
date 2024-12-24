package com.library.system.service.impl;

import com.library.system.exception.bookDaoException.*;
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
        try {
            // Appelle la méthode du repository pour ajouter le livre
            bookRepository.addBook(book);
        } catch (BookAlreadyExistsException e) {
            // Gère l'exception si le livre existe déjà
            throw new BookAlreadyExistsException("Le livre existe déjà : " + e.getMessage());
        } catch (Exception e) {
            // Gère toute autre exception
            throw new RuntimeException("Erreur lors de l'ajout du livre : " + e.getMessage());
        }
    }


    @Override
    public List<Book> displayAvailableBooks() throws BookDisplayException {
        return bookRepository.displayAvailableBooks();
    }

    @Override
    public void updateBook(Book book) throws BookUpdateException {
        bookRepository.updateBook(book);
    }

    @Override
    public void removeBook(int bookId) throws BookRemoveException {
        bookRepository.removeBook(bookId);
    }

    @Override
    public boolean isAvailable(int bookId) throws BookAvailabilityException {
        return bookRepository.isAvailable(bookId);
    }

    @Override
    public boolean isReturned(int bookId) throws BookIsreturnedException {
        try {
            // Chercher le livre par son ID à partir du BookRepository
            Book book = bookRepository.getBookById(bookId); // Suppose que vous avez une méthode getBookById dans BookRepository

            // Vérifier si le livre existe et retourner l'état isReturned
            if (book != null) {
                return book.isReturned(); // Retourner si le livre a été retourné ou non
            } else {
                throw new BookIsreturnedException("Le livre avec l'ID " + bookId + " n'a pas été trouvé.");
            }
        } catch (Exception e) {
            throw new BookIsreturnedException("Erreur lors de la vérification du retour du livre : " + e.getMessage());
        }
    }

    @Override
    public List<Book> searchBookByTitle(String title) throws BookSearchByTitleException {
        return bookRepository.searchBookByTitle(title);
    }

    @Override
    public List<Book> searchBookByCategory(String category) throws BookSearchByCategoryException  {
        return bookRepository.searchBookByCategory(category);
    }
}
