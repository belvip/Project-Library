package com.library.system.repository.impl;

import com.library.system.dao.BookDAO;
import com.library.system.dao.impl.BookDAOImpl;
import com.library.system.exception.bookDaoException.*;
import com.library.system.model.Book;
import com.library.system.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {
    private final BookDAO bookDAO;
    // Liste de livres pour cet exemple
    private List<Book> books = new ArrayList<>();  // Liste de livres en mémoire

    // Constructeur
    public BookRepositoryImpl() {
        this.bookDAO = new BookDAOImpl();
    }

    @Override
    public void addBook(Book book) {
        try {
            bookDAO.addBook(book);
        } catch (Exception e) {
            throw new BookAddException("Erreur lors de l'ajout du livre : ");
        }
    }

    @Override
    public List<Book> displayAvailableBooks() {
        try {
            return bookDAO.displayAvailableBooks();
        } catch (Exception e) {
            throw new BookDisplayException("Erreur lors de l'affichage des livres disponibles : ");
        }
    }

    @Override
    public void updateBook(Book book) {
        try {
            bookDAO.updateBook(book);
        } catch (Exception e) {
            throw new BookUpdateException("Erreur lors de la mise à jour du livre : ");
        }
    }

    @Override
    public void removeBook(int bookId) {
        try {
            bookDAO.removeBook(bookId);
        } catch (Exception e) {
            throw new BookRemoveException("Erreur lors de la suppression du livre : ");
        }
    }

    @Override
    public boolean isAvailable(int bookId) {
        try {
            return bookDAO.isAvailable(bookId);
        } catch (Exception e) {
            throw new BookAvailabilityException("Erreur lors de la vérification de la disponibilité du livre : ");
        }
    }

    @Override
    public boolean isReturned(int bookId) {
        // Utilisez votre logique pour vérifier si le livre a été retourné
        // Par exemple, vérifier si un livre avec l'ID existe et est retourné
        Book book = getBookById(bookId);  // Suppose que vous avez une méthode getBookById
        if (book != null) {
            return book.isReturned();  // Retourner l'état de retour du livre
        } else {
            return false;  // Ou lancer une exception si le livre n'existe pas
        }
    }


    @Override
    public Book getBookById(int bookId) throws BookIsreturnedException {
        for (Book book : books) { // Supposons que "books" est une liste de livres
            if (book.getBook_id() == bookId) {
                return book;
            }
        }
        return null;
    }

    @Override
    public List<Book> searchBookByTitle(String title) {
        try {
            return bookDAO.searchBookByTitle(title);
        } catch (Exception e) {
            throw new BookSearchByTitleException("Erreur lors de la recherche du livre par titre : ");
        }
    }

    @Override
    public List<Book> searchBookByCategory(String category) {
        try {
            return bookDAO.searchBookByCategory(category);
        } catch (Exception e) {
            throw new BookSearchByCategoryException("Erreur lors de la recherche du livre par catégorie : " );
        }
    }
}
