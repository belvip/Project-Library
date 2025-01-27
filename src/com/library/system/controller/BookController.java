package com.library.system.controller;

import com.library.system.dao.BookDAO;
import com.library.system.dao.impl.BookDAOImpl;
import com.library.system.exception.bookDaoException.BookDisplayException;
import com.library.system.exception.bookDaoException.BookSearchByCategoryException;
import com.library.system.repository.impl.BookRepositoryImpl;
import com.library.system.service.BookService;
import com.library.system.service.impl.BookServiceImpl;
import com.library.system.model.Book;
import com.library.system.exception.bookDaoException.BookAddException;
import com.library.system.util.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class BookController {

    private BookService bookService;

    // Constructeur avec injection de connexion et initialisation du BookService
    public BookController(Connection connection) {
        // Créer une instance de BookDAOImpl pour la passer à BookRepositoryImpl
        BookDAO bookDAO = new BookDAOImpl(connection);
        this.bookService = new BookServiceImpl(new BookRepositoryImpl(connection, bookDAO));
    }

    // Méthode pour ajouter un livre
    public void addBook(Book book) {
        try {
            bookService.addBook(book);
        } catch (BookAddException e) {
            Logger.logError("Erreur lors de l'ajout du livre : " + e.getMessage());
        }
    }

    // Méthode pour mettre à jour un livre
    public void updateBook(Book book) {
        try {
            // Utiliser bookRepository pour effectuer la mise à jour
            bookService.updateBook(book);
            Logger.logSuccess("Livre mis à jour avec succès.");
        } catch (Exception e) {
            Logger.logError("Erreur lors de la mise à jour du livre : " + e.getMessage());
        }
    }

    // Supprimer un livre
    public void removeBook(int bookId){
        try{
            bookService.removeBook(bookId);
        }catch (Exception e) {
            Logger.logError("Erreur lors de la suppression du livre : " + e.getMessage());
        }
    }

    // Rechercher un livre par catégorie
    public List<Book> searchBookByCategory(String categoryName) {
        try {
            // Appel au service pour obtenir les livres
            return bookService.searchBookByCategory(categoryName);
        } catch (BookSearchByCategoryException e) {
            // Gestion de l'erreur
            System.out.println("Erreur : " + e.getMessage());
            return Collections.emptyList(); // Retourner une liste vide au lieu de null
        }
    }



    public Book displayBookById(int bookId) {
        try {
            // Appel au service pour obtenir le livre
            return bookService.displayBookById(bookId);
        } catch (BookDisplayException e) {
            // Gestion de l'erreur
            Logger.logError("Erreur : " + e.getMessage());
            return null;
        }
    }


    // Méthode pour afficher les livres disponibles
    public List<Book> displayAvailableBooks() {
        List<Book> books = bookService.getAvailableBooks();  // Appel de la méthode du service
        if (books.isEmpty()) {
            Logger.logError("Aucun livre disponible.");
        } else {
            // Afficher les livres disponibles
            for (Book book : books) {
                //System.out.println("Livre ID: " + book.getBook_id() + ", Titre: " + book.getTitle());
            }
        }
        return books;
    }


}
