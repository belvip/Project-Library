package com.library.system.service;

import com.library.system.model.Book;

import java.util.List;

public interface BookService {
    void addBook(Book book); // Ajouter un livre

    List<Book> displayAvailableBooks(); // Afficher les livres disponibles

    void updateBook(Book book); // Mettre à jour un livre

    void removeBook(int bookId); // Supprimer un livre

    boolean isAvailable(int bookId); // Vérifier la disponibilité d'un livre
    boolean isReturned(int bookId); // Verifier si un livre est retourne

    List<Book> searchBookByTitle(String title); // Rechercher des livres par titre

    List<Book> searchBookByCategory(String category); // Rechercher des livres par catégorie
}
