

package com.library.system.handler;


import com.library.system.exception.memberException.FindMemberByIdException;
import com.library.system.model.*;
import com.library.system.service.impl.LoanServiceImpl;
import com.library.system.controller.LoanController;
import com.library.system.util.DatabaseConnection;
import com.library.system.util.Logger;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class LoanHandler {


    private final Scanner scanner = new Scanner(System.in);
    private final LoanServiceImpl loanService;
    private final LoanController loanController;
    private Connection connection;


    public LoanHandler(LoanServiceImpl loanService, LoanController loanController) {
        this.loanService = loanService;
        this.loanController = loanController;


        try {
            // Initialisation de la connexion à la base de données
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }


    }


    // Méthode pour traiter les opérations sur les emprunts
    public void handleLoanOperations() {
        boolean running = true;
        while (running) {
            displayLoanMenu();
            int choice = getChoiceInput();


            scanner.nextLine();


            switch (choice) {
                case 1:
                    registerLoan();  // Appel de la méthode pour enregistrer un emprunt
                    break;
                case 2:
                    returnBook();  // Appel de la méthode pour retourner un livre
                    break;
                case 3:
                    getAllLoans();
                    break;
                case 4:
                    deleteLoan();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Choix invalide. Essayez encore.");
            }
        }
    }


    // Méthode pour obtenir l'entrée de l'utilisateur
    private int getChoiceInput() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine();  // Nettoie le buffer
            return -1;
        }
    }


    // Méthode pour afficher le menu des opérations
    private void displayLoanMenu() {
        //System.out.println("\n\u001B[34m======== Opérations sur les emprunts ========\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.printf("| %-2s | %-40s |\n", "1", "\u001B[32mEnregister un emprunt\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "2", "\u001B[32mRetouner un emprunt\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "3", "\u001B[32mAfficher tous les emprunts\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "4", "\u001B[32mSupprimer un emprunt\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "5", "\u001B[31mQuitter\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.print("\u001B[33mEntrez votre choix: \u001B[0m");
    }


    public void deleteLoan() {
        System.out.print("Entrez l'ID de l'emprunt à supprimer : ");
        int loanId = scanner.nextInt(); // Demande l'ID de l'emprunt à supprimer
        scanner.nextLine();  // Consommer le saut de ligne restant


        try {
            loanController.deleteLoan(loanId);  // Appel du contrôleur pour supprimer le prêt
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression du prêt: " + e.getMessage());
            Logger.logError("Erreur lors de la suppression du prêt avec l'ID " + loanId, e); // Log de l'erreur
        }
    }


    // Méthode pour enregistrer un emprunt via LoanController
    public void registerLoan() {
        // Étape 1 : Obtenir les informations du membre
        System.out.print("Entrez l'ID du membre qui souhaite emprunter des livres : ");
        int memberId = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne après nextInt()


        Member member;
        try {
            member = loanController.getMemberById(memberId); // Récupérer le membre par ID
        } catch (FindMemberByIdException e) {
            System.out.println("❌ Erreur : " + e.getMessage()); // Afficher un message clair
            return; // Arrêter le processus d'emprunt
        }


        if (member == null) {
            System.out.println("❌ Membre introuvable. Veuillez vérifier l'ID.");
            return;
        }


        // Étape 2 : Obtenir les livres à emprunter
        System.out.print("Entrez les IDs des livres à emprunter (séparés par des espaces) : ");
        String[] bookIds = scanner.nextLine().split(" ");


        List<Book> books = new ArrayList<>(); // Liste pour stocker les livres trouvés
        List<Book> booksAlreadyLoaned = new ArrayList<>(); // Liste des livres déjà empruntés par le membre


        // Parcourir chaque ID et récupérer le livre correspondant
        for (String bookIdStr : bookIds) {
            try {
                int bookId = Integer.parseInt(bookIdStr); // Convertir String en int
                Book book = loanController.getBookById(bookId); // Récupérer le livre par ID


                if (book != null) {
                    // Vérifier si le membre a déjà emprunté ce livre
                    if (loanController.hasMemberAlreadyLoanedBook(memberId, bookId)) {
                        booksAlreadyLoaned.add(book); // Ajouter à la liste des livres déjà empruntés
                    } else {
                        books.add(book); // Ajouter à la liste des livres à emprunter
                    }
                } else {
                    System.out.println("⚠️ Livre avec ID " + bookId + " introuvable.");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ ID invalide : " + bookIdStr);
            }
        }


        // Vérifier si au moins un livre a été trouvé
        if (books.isEmpty() && booksAlreadyLoaned.isEmpty()) {
            System.out.println("❌ Aucun livre valide n'a été trouvé avec les IDs fournis.");
            return;
        }


        // Étape 3 : Afficher les livres déjà empruntés
        if (!booksAlreadyLoaned.isEmpty()) {
            System.out.println("❌ Ce livres ont déjà été empruntés par ce membre :");
            for (Book book : booksAlreadyLoaned) {
                System.out.println("Livre ID " + book.getBook_id() + ": " + book.getTitle());
            }
        }


        // Étape 4 : Enregistrer les livres qui ne sont pas déjà empruntés
        if (!books.isEmpty()) {
            // Enregistrer les emprunts valides
            loanController.registerLoan(member, books);  // Appeler la méthode de LoanController pour enregistrer les livres non empruntés
            System.out.println("✅ L'emprunt a été enregistré pour les livres suivants :");
            for (Book book : books) {
                System.out.println("Livre ID " + book.getBook_id() + ": " + book.getTitle());
            }
        }
    }


    public void returnBook() {
        System.out.print("Entrez l'ID du prêt à retourner : ");
        int loanId = scanner.nextInt();


        try {
            loanController.returnBook(loanId);  // Appel de la méthode returnBook dans LoanController
            System.out.println("✅ Le livre a été retourné avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors du retour du livre : " + e.getMessage());
        }
    }


    public void getAllLoans() {
        try {
            List<Loan> loans = new ArrayList<>();
            loanController.getAllLoans(loans);


            if (loans.isEmpty()) {
                System.out.println("\nAucun emprunt trouvé.");
                return;
            }


            // Détermination des largeurs maximales
            int loanIdWidth = "Emprunt ID".length();
            int memberWidth = "Membre".length();
            int loanDateWidth = "Date d'Emprunt".length();
            int dueDateWidth = "Date d'Échéance".length();
            int returnDateWidth = "Date de Retour".length();
            int bookIdWidth = "Livre ID".length();
            int bookTitleWidth = "Titre".length();
            int copiesWidth = "Nb Copies".length();
            int authorsWidth = "Auteurs".length();
            int categoriesWidth = "Catégories".length();


            for (Loan loan : loans) {
                loanIdWidth = Math.max(loanIdWidth, String.valueOf(loan.getLoanId()).length());
                memberWidth = Math.max(memberWidth, (loan.getMember().getFirstName() + " " + loan.getMember().getLastName()).length());
                loanDateWidth = Math.max(loanDateWidth, loan.getFormattedLoanDate().length());
                dueDateWidth = Math.max(dueDateWidth, loan.getFormattedDueDate().length());
                returnDateWidth = Math.max(returnDateWidth, loan.getFormattedReturnedDate().length());


                for (Book book : loan.getBooks()) {
                    bookIdWidth = Math.max(bookIdWidth, String.valueOf(book.getBook_id()).length());
                    bookTitleWidth = Math.max(bookTitleWidth, book.getTitle().length());
                    copiesWidth = Math.max(copiesWidth, String.valueOf(book.getNumber_Of_Copies()).length());


                    authorsWidth = Math.max(authorsWidth, String.join(", ", book.getAuthors().stream()
                            .map(author -> author.getFirst_name() + " " + author.getLast_name())
                            .toList()).length());


                    categoriesWidth = Math.max(categoriesWidth, String.join(", ", book.getCategories().stream()
                            .map(Category::getCategory_name)
                            .toList()).length());
                }
            }


            // Définition des couleurs ANSI
            String BLUE = "\u001B[34m";
            String GREEN = "\u001B[32m";
            String RESET = "\u001B[0m";


            // Ligne de séparation en bleu
            String horizontalLine = BLUE + "-".repeat(
                    loanIdWidth + memberWidth + loanDateWidth + dueDateWidth + returnDateWidth + bookIdWidth +
                            bookTitleWidth + copiesWidth + authorsWidth + categoriesWidth + 29) + RESET;


            // Format d'affichage
            String format = "| %-" + loanIdWidth + "s | %-" + memberWidth + "s | %-" + loanDateWidth + "s | %-" + dueDateWidth +
                    "s | %-" + returnDateWidth + "s | %-" + bookIdWidth + "s | %-" + bookTitleWidth + "s | %-" +
                    copiesWidth + "s | %-" + authorsWidth + "s | %-" + categoriesWidth + "s |\n";


            // Affichage de l'en-tête
            System.out.println("\n\u001B[34m========= Liste des Emprunts ========\u001B[0m");
            System.out.println(horizontalLine);
            System.out.printf(BLUE + format + RESET,
                    "Emprunt ID", "Membre", "Date d'Emprunt", "Date d'Échéance", "Date de Retour",
                    "Livre ID", "Titre", "Nb Copies", "Auteurs", "Catégories");
            System.out.println(horizontalLine);


            // Affichage des données
            for (Loan loan : loans) {
                for (Book book : loan.getBooks()) {
                    List<String> authorNames = book.getAuthors().stream()
                            .map(author -> author.getFirst_name() + " " + author.getLast_name())
                            .toList();
                    List<String> categoryNames = book.getCategories().stream()
                            .map(Category::getCategory_name)
                            .toList();


                    System.out.printf(format,
                            String.valueOf(loan.getLoanId()),
                            loan.getMember().getFirstName() + " " + loan.getMember().getLastName(),
                            loan.getFormattedLoanDate(),
                            loan.getFormattedDueDate(),
                            loan.getFormattedReturnedDate(),
                            String.valueOf(book.getBook_id()),
                            book.getTitle(),
                            String.valueOf(book.getNumber_Of_Copies()),
                            String.join(", ", authorNames),
                            String.join(", ", categoryNames));
                }
            }


            System.out.println(horizontalLine);


        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'affichage des emprunts : " + e.getMessage());
        }
    }










}



