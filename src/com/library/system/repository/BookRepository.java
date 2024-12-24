package com.library.system.repository;

import com.library.system.exception.bookDaoException.BookIsreturnedException;
import com.library.system.model.Book;
import java.util.List;

public interface BookRepository {
    // Ajouter un livre
    void addBook(Book book);

    // Afficher les livres disponibles
    List<Book> displayAvailableBooks();

    // Mettre à jour un livre
    void updateBook(Book book);

    // Supprimer un livre
    void removeBook(int bookId);

    // Vérifier la disponibilité d'un livre
    boolean isAvailable(int bookId);


    boolean isReturned(int bookId);

    Book getBookById(int bookId) throws BookIsreturnedException;

   // Book getBook_id(int bookId) throws BookIsreturnedException;

    // Rechercher un livre par son titre
    List<Book> searchBookByTitle(String title);

    // Rechercher un livre par catégorie
    List<Book> searchBookByCategory(String category);

}
