package com.library.system.util;

import com.library.system.controller.AuthorController;
import com.library.system.exception.authorException.AuthorAlreadyExistException;
import com.library.system.model.Author;
import com.library.system.service.impl.AuthorServiceImpl;

import java.util.Scanner;

public class AuthorHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final AuthorServiceImpl authorService;
    private final AuthorController authorController;

    public AuthorHandler(AuthorServiceImpl authorService, AuthorController authorController) {
        this.authorService = authorService;
        this.authorController = authorController;
    }

    public void authorOperation() {
        boolean running = true;
        while (running) {
            displayAuthorMenu();
            int choice = getChoiceInput();
            switch (choice) {
                case 1:
                    createAuthor();
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
        System.out.println("\n======== Opérations sur les auteurs ========");
        System.out.println("1. Ajouter un auteur");
        System.out.println("2. Modifier un auteur");
        System.out.println("3. Supprimer un auteur");
        System.out.println("4. Lister tous les auteurs");
        System.out.println("5. Rechercher des auteurs par mot-clé");
        System.out.println("6. Retourner au menu principal");
        System.out.print("Entrez votre choix: ");
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
        try {
            authorController.createAuthor(author);
            System.out.println("Auteur ajouté avec succès.");
        } catch (AuthorAlreadyExistException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    /*private void modifyAuthor() {
        System.out.print("Entrez l'ID de l'auteur à modifier: ");
        int authorId = getChoiceInput();
        scanner.nextLine();  // Clear buffer

        System.out.print("Entrez le nouveau prénom de l'auteur: ");
        String newFirstName = scanner.nextLine();

        System.out.print("Entrez le nouveau nom de l'auteur: ");
        String newLastName = scanner.nextLine();

        System.out.print("Entrez le nouvel email de l'auteur: ");
        String newEmail = scanner.nextLine();

        Author author = new Author(newFirstName, newLastName, newEmail);
        author.setId(authorId);  // Assurez-vous que l'auteur a l'ID mis à jour

        try {
            authorController.updateAuthor(author);
            System.out.println("Auteur mis à jour avec succès.");
        } catch (AuthorNotFoundException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void deleteAuthor() {
        System.out.print("Entrez l'ID de l'auteur à supprimer: ");
        int authorId = getChoiceInput();

        try {
            authorController.deleteAuthor(authorId);
            System.out.println("Auteur supprimé avec succès.");
        } catch (AuthorNotFoundException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void listAuthors() {
        try {
            authorController.listAuthors();
        } catch (Exception e) {
            System.out.println("Erreur lors de la liste des auteurs: " + e.getMessage());
        }
    }

    private void searchAuthors() {
        System.out.print("Entrez un mot-clé pour rechercher des auteurs: ");
        scanner.nextLine();  // Clear buffer
        String keyword = scanner.nextLine();

        try {
            authorController.searchAuthorsByKeyword(keyword);
        } catch (Exception e) {
            System.out.println("Erreur lors de la recherche des auteurs: " + e.getMessage());
        }
    }*/
}
