package com.library.system.handler;

import com.library.system.controller.AuthorController;
import com.library.system.exception.authorException.InvalidAuthorEmailException;
import com.library.system.model.Author;
import com.library.system.exception.authorException.AuthorNotFoundException;
import com.library.system.exception.authorException.AuthorEmailAlreadyExistsException;
import com.library.system.service.impl.AuthorServiceImpl;
import com.library.system.util.Logger;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AuthorHandler {
    private final Scanner scanner = new Scanner(System.in);
    private final AuthorServiceImpl authorService; // Service pour gérer les auteurs
    private final AuthorController authorController; // Contrôleur pour gérer les requêtes d'auteurs

    // Constructeur qui accepte AuthorServiceImpl et AuthorController
    public AuthorHandler(AuthorServiceImpl authorService, AuthorController authorController) {
        this.authorService = authorService;
        this.authorController = authorController;
    }
    // Méthode pour traiter les opérations sur les auteurs
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
                    displayAuthors();
                    break;
                case 3:
                    deleteAuthor();
                    break;
                case 4:
                    getAuthorByEmail();
                    break;
                case 5:
                    getAuthorById();
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Choix invalide. Essayez encore.");
            }
        }
    }

    // Méthode pour afficher le menu des opérations
    private void displayAuthorMenu() {
        //System.out.println("\n\u001B[34m======== Opérations sur l'auteur ========\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.printf("| %-2s | %-40s |\n", "1", "\u001B[32mAjouter un auteur\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "2", "\u001B[32mLister tous les auteurs\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "3", "\u001B[32mSupprimer un auteur\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "4", "\u001B[32mRechercher un auteur par email\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "5", "\u001B[32mRechercher un auteur par ID\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "6", "\u001B[31mRetourner au menu principal\u001B[0m");
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

    // Ajouter un auteur
    private void createAuthor() {
        Logger.logInfo("Ajouter un auteur");
        scanner.nextLine(); // Nettoyer le buffer du scanner
        System.out.print("Entrez le prénom de l'auteur : ");
        String firstName = scanner.nextLine();
        System.out.print("Entrez le nom de l'auteur : ");
        String lastName = scanner.nextLine();
        System.out.print("Entrez l'email de l'auteur : ");
        String email = scanner.nextLine();

        // Validation du prénom et du nom
        if (!isValidName(firstName)) {
            // System.out.println("⚠️ Erreur : Le prénom ne doit contenir que des lettres.");
            Logger.logWarn("Erreur", " Le prénom ne doit contenir que des lettres.");
            return; // Retourner sans créer l'auteur si la validation échoue
        }

        if (!isValidName(lastName)) {
            //System.out.println("Erreur : Le nom ne doit contenir que des lettres.");
            Logger.logWarn("Erreur", " Le nom ne doit contenir que des lettres.");
            return; // Retourner sans créer l'auteur si la validation échoue
        }
        // Logger.logWarn("Ajout de la catégorie", "Le n tirets ou le caractère '&'.");

        try {
            Author author = authorController.createAuthor(firstName, lastName, email);
            if (author != null) {
                Logger.logSuccess("Auteur ajouté : " + author.getFirst_name() + " " + author.getLast_name());
            }
        } catch (AuthorEmailAlreadyExistsException e) {
            //System.out.println("️⚠️ ️️ ️Erreur : L'email existe déjà.");
            Logger.logWarn("Erreur", " L'email existe déjà.");
        } catch (InvalidAuthorEmailException e) {
            //System.out.println("❌ Erreur : L'email n'est pas valide. Veuillez entrer un email valide.");
            Logger.logWarn("Erreur : ", " L'email n'est pas valide. Veuillez entrer un email valide.");
        }
    }

    // Méthode pour valider si le nom contient uniquement des lettres
    private boolean isValidName(String name) {
        return name.trim().matches("^[\\p{L}&-]+(\\s[\\p{L}&-]+)*$");
    }




    // AuthorHandler.java
    private void displayAuthors() {
        Logger.logInfo("Lister les auteurs");
        List<Author> authors = authorService.displayAuthors(); // Appel à la méthode du service
        if (authors.isEmpty()) {
            Logger.logError("Aucun auteur trouvé.");
            return;
        }

        // Calcul des largeurs dynamiques pour chaque colonne
        int idWidth = "ID".length();
        int firstNameWidth = "Nom".length();
        int lastNameWidth = "Prenom".length();
        int emailWidth = "Email".length();


        for (Author author : authors) {
            idWidth = Math.max(idWidth, String.valueOf(author.getAuthor_id()).length());
            firstNameWidth = Math.max(firstNameWidth, author.getFirst_name().length());
            lastNameWidth = Math.max(lastNameWidth, author.getLast_name().length());
            emailWidth = Math.max(emailWidth, author.getAuthor_email().length());
        }


        // Couleurs ANSI pour l'en-tête et les lignes
        String CYAN = "\u001B[36m";
        String RESET = "\u001B[0m";


        // Ligne de séparation
        String horizontalLine = CYAN + "+-" + "-".repeat(idWidth) + "-+-" + "-".repeat(firstNameWidth) + "-+-" +
                "-".repeat(lastNameWidth) + "-+-" + "-".repeat(emailWidth) + "-+" + RESET;


        // Format d'affichage
        String format = "| %-" + idWidth + "s | %-" + firstNameWidth + "s | %-" + lastNameWidth + "s | %-" + emailWidth + "s |\n";


        // Affichage du tableau
        //System.out.println("\n\u001B[34m======== Liste des Auteurs ========\u001B[0m");
        Logger.logInfo("======== Liste des Auteurs ========");
        System.out.println(horizontalLine);
        System.out.printf(CYAN + format + RESET, "ID", "Nom", "Prenom", "Email");
        System.out.println(horizontalLine);


        for (Author author : authors) {
            System.out.printf(format,
                    author.getAuthor_id(),
                    author.getFirst_name(),
                    author.getLast_name(),
                    author.getAuthor_email());
        }


        System.out.println(horizontalLine);
    }


    // Méthode pour supprimer un auteur par ID
    private void deleteAuthor() {
        Logger.logInfo("Supprimer un auteur par ID ");
        System.out.print("Entrez l'ID de l'auteur à supprimer : ");

        int authorId;
        while (true) {
            try {
                if (!scanner.hasNextInt()) { // Vérifie si la saisie est un nombre
                    Logger.logWarn("Suppression de l'auteur", "L'ID doit être un nombre entier.");
                    scanner.next(); // Vide l'entrée incorrecte
                    System.out.print("Veuillez entrer un ID valide : ");
                    continue; // Redemande l'entrée
                }
                authorId = scanner.nextInt();
                scanner.nextLine(); // Consomme le retour à la ligne
                break; // Sort de la boucle si l'entrée est correcte
            } catch (InputMismatchException e) {
                Logger.logWarn("Suppression de l'auteur", "Veuillez entrer un ID valide.");
                scanner.nextLine(); // Évite une boucle infinie en nettoyant l'entrée
            }
        }

        boolean isDeleted = authorController.deleteAuthor(authorId);
        if (isDeleted) {
            Logger.logSuccess("Auteur supprimé avec succès.");
        } else {
            Logger.logError("Auteur non trouvé.");
        }
    }


    // Méthode pour rechercher un auteur par email
    private void getAuthorByEmail() {
        Logger.logInfo("Rechercher un auteur par email");
        scanner.nextLine(); // Nettoyer le buffer du scanner
        System.out.print("Entrez l'email de l'auteur : ");
        String email = scanner.nextLine();

        try {
            Author author = authorController.getAuthorByEmail(email);
            if (author != null) {
                Logger.logSuccess("Recherche par email réussie");
                Logger.logInfo("Auteur trouvé : " + author.getFirst_name() + " " + author.getLast_name());
            }
        } catch (AuthorNotFoundException e) {
            Logger.logError("Recherche d'auteur par email", e);
            Logger.logError("Auteur non trouvé.");
        }
    }

    // Méthode pour rechercher un auteur par ID
    private void getAuthorById() {
        Logger.logInfo("Rechercher un auteur par ID ");
        System.out.print("Entrez l'ID de l'auteur : ");

        int authorId;

        try {
            if (!scanner.hasNextInt()) {  // Vérifie si l'entrée est un nombre
                Logger.logWarn("Recherche d'auteur", "L'ID doit être un nombre entier.");
                scanner.next();  // Nettoie l'entrée incorrecte
                return;
            }

            authorId = scanner.nextInt();
            scanner.nextLine(); // Nettoie la ligne après nextInt()

            Author author = authorController.getAuthorById(authorId);
            if (author != null) {
                Logger.logSuccess("Auteur trouvé : " + author.getFirst_name() + " " + author.getLast_name());
            } else {
                Logger.logWarn("Recherche d'auteur", "Aucun auteur trouvé avec cet ID.");
            }

        } catch (InputMismatchException e) {
            Logger.logWarn("Recherche d'auteur", "Entrée invalide. Veuillez saisir un nombre entier.");
            scanner.nextLine(); // Nettoie l'entrée erronée
        } catch (AuthorNotFoundException e) {
            Logger.logError("Auteur non trouvé.");
        }
    }


}
