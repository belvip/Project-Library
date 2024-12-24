package com.library.system.repository;

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

    // Rechercher un livre par son titre
    List<Book> searchBookByTitle(String title);

    // Rechercher un livre par catégorie
    List<Book> searchBookByCategory(String category);
}
