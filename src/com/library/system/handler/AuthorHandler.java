package com.library.system.handler;

import com.library.system.controller.AuthorController;
import com.library.system.model.Author;
import com.library.system.service.impl.AuthorServiceImpl;
import com.library.system.exception.authorException.AuthorNotFoundException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AuthorHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final AuthorServiceImpl authorService;
    private final AuthorController authorController;

    public AuthorHandler(AuthorServiceImpl authorService) throws SQLException {
        this.authorService = authorService;

        // Connexion à la base de données
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library_db", "postgres", "belvi");

        // Initialisation des services et contrôleurs
        AuthorServiceImpl authorServiceImpl = new AuthorServiceImpl(connection);

        // Initialize authorController
        if (connection != null) {
            this.authorController = new AuthorController(authorServiceImpl);
        } else {
            throw new SQLException("Failed to connect to the database and initialize AuthorController.");
        }
    }


    public void handleAuthorOperations() {
        boolean running = true;
        while (running) {
            displayAuthorMenu();
            int choice = getChoiceInput();
            switch (choice) {
                case 1:
                    createAuthor();
                    break;
                case 2:
                    showAuthors();
                    break;
                case 3:
                    removeAuthor();
                    break;
                case 4:
                    System.out.print("Entrez l'email de l'auteur: ");
                    String email = scanner.nextLine();  // Get the email from user input
                    findAuthorByEmail(email);  // Pass the email to the method
                    break;

                case 5:
                    System.out.print("Entrez l'ID de l'auteur: ");
                    int authorId = scanner.nextInt();  // Get the author ID from user input
                    findAuthorById(authorId);  // Pass the author ID to the method
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Choix invalide. Essayez encore.");
            }
        }
    }

    private void displayAuthorMenu() {
        System.out.println("\n\u001B[34m======== Opérations sur l'auteur ========\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.printf("| %-2s | %-40s |\n", "1", "\u001B[32mAjouter un auteur\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "2", "\u001B[32mLister toutes les auteurs\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "3", "\u001B[32mSupprimer un auteur\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "4", "\u001B[32mRechercher un auteur par email\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "5", "\u001B[32mRechercher des auteurs par ID\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "6", "\u001B[31mRetourner au menu principal\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.print("\u001B[33mEntrez votre choix: \u001B[0m");
    }

    private int getChoiceInput() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }

    private void createAuthor() {
        System.out.print("Entrez le prénom de l'auteur: ");
        scanner.nextLine();  // Clear buffer
        String firstName = scanner.nextLine();

        System.out.print("Entrez le nom de l'auteur: ");
        String lastName = scanner.nextLine();

        System.out.print("Entrez l'email de l'auteur: ");
        String email = scanner.nextLine();

        Author author = new Author(firstName, lastName, email);
        authorController.addAuthor(author);
        System.out.println("Auteur ajouté avec succès.");
    }

    public void findAuthorById(int authorId) {
        try {
            authorService.findAuthorById(authorId);
        } catch (SQLException | AuthorNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void findAuthorByEmail(String email) {
        try {
            authorService.findByEmail(email);
        } catch (SQLException | AuthorNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showAuthors() {
        List<Author> authors = authorController.showAuthors();
        if (authors.isEmpty()) {
            System.out.println("Aucun auteur trouvé.");
        } else {
            authors.forEach(author -> System.out.println(author.getFirst_name() + " " + author.getLast_name()));
        }
    }

    private void removeAuthor() {
        System.out.print("Entrez l'ID de l'auteur à supprimer: ");
        int authorId = scanner.nextInt();

        authorController.removeAuthor(authorId);
        System.out.println("Auteur supprimé avec succès.");
    }
}
