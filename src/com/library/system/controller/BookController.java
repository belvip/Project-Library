package com.library.system.controller;

import com.library.system.model.Book;
import com.library.system.service.BookService;
import com.library.system.service.impl.BookServiceImpl;
import com.library.system.exception.bookDaoException.*;

import java.sql.Connection;
import java.util.List;

public class BookController {
    private BookService bookService;

    // Constructeur : Initialiser le BookService
    public BookController(Connection connection) {
        this.bookService = new BookServiceImpl(); // Vous pouvez aussi passer connection si nécessaire pour les DAO
    }

    // Créer un livre
    public void addBook(String title, int number_Of_Copies) {
        Book book = new Book(title, number_Of_Copies);  // Créer un livre avec les informations fournies
        try {
            bookService.addBook(book);  // Appeler la méthode du service pour ajouter le livre
            System.out.println("Livre ajouté avec succès : " + book.getTitle());
        } catch (BookAlreadyExistsException e) {
            System.err.println("Erreur : Le livre existe déjà. " + e.getMessage());
        } catch (BookAddException e) {
            System.err.println("Erreur lors de l'ajout du livre : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur inconnue lors de l'ajout du livre : " + e.getMessage());
        }
    }

    // Afficher les livres disponibles
    public void displayAvailableBooks() {
        try {
            List<Book> books = bookService.displayAvailableBooks();
            if (books.isEmpty()) {
                System.out.println("Aucun livre disponible.");
            } else {
                for (Book book : books) {
                    System.out.println(book.getTitle() + " - " + book.getNumber_Of_Copies() + " copies disponibles.");
                }
            }
        } catch (BookDisplayException e) {
            System.err.println("Erreur lors de l'affichage des livres disponibles : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur inconnue lors de l'affichage des livres disponibles : " + e.getMessage());
        }
    }

    // Rechercher un livre par son titre
    public void searchBookByTitle(String title) {
        try {
            List<Book> books = bookService.searchBookByTitle(title);
            if (books.isEmpty()) {
                System.out.println("Aucun livre trouvé avec le titre : " + title);
            } else {
                for (Book book : books) {
                    System.out.println(book.getTitle() + " - " + book.getNumber_Of_Copies() + " copies disponibles.");
                }
            }
        } catch (BookSearchByTitleException e) {
            System.err.println("Erreur lors de la recherche du livre par titre : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur inconnue lors de la recherche du livre par titre : " + e.getMessage());
        }
    }

    // Rechercher un livre par sa catégorie
    public void searchBookByCategory(String category) {
        try {
            List<Book> books = bookService.searchBookByCategory(category);
            if (books.isEmpty()) {
                System.out.println("Aucun livre trouvé dans la catégorie : " + category);
            } else {
                for (Book book : books) {
                    System.out.println(book.getTitle() + " - " + book.getNumber_Of_Copies() + " copies disponibles.");
                }
            }
        } catch (BookSearchByCategoryException e) {
            System.err.println("Erreur lors de la recherche du livre par catégorie : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur inconnue lors de la recherche du livre par catégorie : " + e.getMessage());
        }
    }

    // Vérifier si un livre est retourné
    public void checkIfReturned(int bookId) {
        try {
            boolean isReturned = bookService.isReturned(bookId);
            System.out.println("Le livre avec l'ID " + bookId + " a-t-il été retourné ? " + (isReturned ? "Oui" : "Non"));
        } catch (BookIsreturnedException e) {
            System.err.println("Erreur lors de la vérification du retour du livre : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur inconnue lors de la vérification du retour du livre : " + e.getMessage());
        }
    }

    // Vérifier la disponibilité d'un livre
    public void checkAvailability(int bookId) {
        try {
            boolean isAvailable = bookService.isAvailable(bookId);
            System.out.println("Le livre avec l'ID " + bookId + " est " + (isAvailable ? "disponible" : "non disponible"));
        } catch (BookAvailabilityException e) {
            System.err.println("Erreur lors de la vérification de la disponibilité du livre : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur inconnue lors de la vérification de la disponibilité du livre : " + e.getMessage());
        }
    }

    // Mettre à jour un livre
    public void updateBook(Book book) {
        try {
            bookService.updateBook(book);
            System.out.println("Livre mis à jour avec succès : " + book.getTitle());
        } catch (BookUpdateException e) {
            System.err.println("Erreur lors de la mise à jour du livre : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur inconnue lors de la mise à jour du livre : " + e.getMessage());
        }
    }

    // Supprimer un livre
    public void removeBook(int bookId) {
        try {
            bookService.removeBook(bookId);
            System.out.println("Livre supprimé avec succès avec l'ID : " + bookId);
        } catch (BookRemoveException e) {
            System.err.println("Erreur lors de la suppression du livre : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur inconnue lors de la suppression du livre : " + e.getMessage());
        }
    }
}
