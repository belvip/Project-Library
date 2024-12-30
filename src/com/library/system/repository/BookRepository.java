package com.library.system.repository;

import com.library.system.exception.bookDaoException.*;
import com.library.system.model.Book;
import java.util.List;

public interface BookRepository {
    // Ajouter un livre
    void addBook(Book book) throws BookAddException;

    // Afficher les livres disponibles
    List<Book> displayAvailableBooks() throws BookDisplayException;

    // Mettre à jour un livre
    void updateBook(Book book) throws BookUpdateException;

    // Supprimer un livre
    void removeBook(int bookId) throws BookRemoveException;

    // Vérifier la disponibilité d'un livre
    boolean isAvailable(int bookId) throws BookAvailabilityException;


    //boolean isReturned(int bookId);

    Book getBookById(int bookId) throws BookGetByIdException;

   // Book getBook_id(int bookId) throws BookIsreturnedException;

    // Rechercher un livre par son titre
    List<Book> searchBookByTitle(String title) throws BookSearchByTitleException;

    // Rechercher un livre par catégorie
    List<Book> searchBookByCategory(String category) throws BookSearchByCategoryException;



}
