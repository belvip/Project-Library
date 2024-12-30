package com.library.system.handler;

import com.library.system.controller.AuthorController;
import com.library.system.controller.BookController;
import com.library.system.exception.bookDaoException.BookDisplayException;
import com.library.system.model.Author;
import com.library.system.model.Book;
import com.library.system.model.Category;
import com.library.system.service.impl.AuthorServiceImpl;
import com.library.system.service.impl.BookServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class BookHandler {

    private final Scanner scanner = new Scanner(System.in);
    private final BookServiceImpl bookService; // Service pour gérer les livres
    private final BookController bookController; // Contrôleur pour gérer les requêtes des livres

    // Constructeur qui accepte BookServiceImpl et BookController
    public BookHandler(BookServiceImpl bookService, BookController bookController) {
        this.bookService = bookService;  // Initialisation correcte
        this.bookController = bookController;
    }

    // Méthode pour traiter les opérations sur les livres
    public void handleBookOperations() {
        boolean running = true;
        while (running) {
            displayBookMenu();
            int choice = getChoiceInput();
            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    displayAvailableBooks();
                    break;
                case 3: // Afficher tous les livres
                    displayAllAvailableBooks();
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    System.out.println("Choix invalide. Essayez encore.");
            }
        }
    }

    // Méthode pour afficher le menu des opérations
    private void displayBookMenu() {
        System.out.println("\n\u001B[34m======== Opérations sur les livres ========\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.printf("| %-2s | %-40s |\n", "1", "\u001B[32mAjouter un livre\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "2", "\u001B[33mAfficher un livre\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "3", "\u001B[36mAfficher tous les livres\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "4", "\u001B[31mQuitter\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.print("\u001B[33mEntrez votre choix: \u001B[0m");
    }


    // Méthode pour obtenir l'entrée de l'utilisateur
    private int getChoiceInput() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine(); // Nettoie le buffer
            return -1;
        }
    }

    private void addBook() {
        scanner.nextLine(); // Nettoyer le buffer du scanner

        // Demander les informations du livre
        System.out.print("Entrez le titre du livre : ");
        String title = scanner.nextLine();
        System.out.print("Entrez le nombre d'exemplaires : ");
        int numberOfCopies = scanner.nextInt();

        // Créer le set des auteurs
        Set<Author> authors = new HashSet<>();
        System.out.print("Entrez l'ID de l'auteur : ");
        int authorId = scanner.nextInt();
        Author author = new Author(); // Récupérer l'auteur via un service
        author.setAuthor_id(authorId);
        authors.add(author);

        // Créer le set des catégories
        Set<Category> categories = new HashSet<>();
        System.out.print("Entrez l'ID de la catégorie : ");
        int categoryId = scanner.nextInt();
        Category category = new Category(); // Récupérer la catégorie via un service
        category.setCategory_id(categoryId);
        categories.add(category);

        // Créer l'objet Book sans ID
        Book book = new Book();
        book.setTitle(title);
        book.setAuthors(authors);
        book.setCategories(categories);
        book.setNumber_Of_Copies(numberOfCopies);

        // Ajouter le livre via le contrôleur
        bookController.addBook(book);

        // Afficher l'ID généré
        System.out.println("Livre ajouté avec succès. L'ID du livre est : " + book.getBook_id());
    }

    // Méthode pour afficher un livre
    private void displayAvailableBooks() {
        System.out.print("Entrez l'ID du livre à afficher: ");
        int bookId = scanner.nextInt(); // Demander l'ID du livre à afficher

        try {
            Book book = bookController.displayAvailableBooks(bookId); // Appel du contrôleur pour afficher le livre
            if (book != null) {
                System.out.println("\n\u001B[34m======== Détails du Livre ========\u001B[0m");
                System.out.println("+------------+-----------------------------------+---------------------+---------------------------+-----------------------------------------+");
                System.out.printf("| %-10s | %-30s | %-19s | %-25s | %-35s |\n", "ID", "Titre", "Nb Copies", "Catégorie", "Email Auteur");
                System.out.println("+------------+-----------------------------------+---------------------+---------------------------+-----------------------------------------+");

                // Récupérer les catégories du livre
                String categories = book.getCategories().stream()
                        .map(Category::getCategory_name)
                        .reduce((cat1, cat2) -> cat1 + ", " + cat2)
                        .orElse("Aucune catégorie");

                // Récupérer l'email de l'auteur
                String authorEmail = book.getAuthors().stream()
                        .findFirst()
                        .map(Author::getAuthor_email)
                        .orElse("Email non disponible");

                // Afficher les informations du livre
                System.out.printf("| %-10d | %-30s | %-19d | %-25s | %-35s |\n",
                        book.getBook_id(),
                        book.getTitle(),
                        book.getNumber_Of_Copies(),
                        categories,
                        authorEmail);

                System.out.println("+------------+-----------------------------------+---------------------+---------------------------+-----------------------------------------+");
            } else {
                System.out.println("Livre non trouvé.");
            }
        } catch (BookDisplayException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }

    // Méthode pour afficher tous les livres
    private void displayAllAvailableBooks() {
        try {
            List<Book> books = bookController.displayAvailableBooks(); // Appeler la méthode sans argument

            if (books.isEmpty()) {
                System.out.println("Aucun livre disponible.");
            } else {
                System.out.println("\n\u001B[34m======== Liste des Livres ========\u001B[0m");
                System.out.println("+------------+-----------------------------------+---------------------+---------------------------+-----------------------------------------+");
                System.out.printf("| %-10s | %-30s | %-19s | %-25s | %-35s |\n", "ID", "Titre", "Nb Copies", "Catégorie", "Email Auteur");
                System.out.println("+------------+-----------------------------------+---------------------+---------------------------+-----------------------------------------+");

                for (Book book : books) {
                    // Récupérer les catégories du livre
                    String categories = book.getCategories().stream()
                            .map(Category::getCategory_name)
                            .reduce((cat1, cat2) -> cat1 + ", " + cat2)
                            .orElse("Aucune catégorie");

                    // Récupérer l'email de l'auteur
                    String authorEmail = book.getAuthors().stream()
                            .findFirst()
                            .map(Author::getAuthor_email)
                            .orElse("Email non disponible");

                    // Afficher les informations du livre
                    System.out.printf("| %-10d | %-30s | %-19d | %-25s | %-35s |\n",
                            book.getBook_id(),
                            book.getTitle(),
                            book.getNumber_Of_Copies(),
                            categories,
                            authorEmail);
                }

                System.out.println("+------------+-----------------------------------+---------------------+---------------------------+-----------------------------------------+");
            }
        } catch (BookDisplayException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }


}
