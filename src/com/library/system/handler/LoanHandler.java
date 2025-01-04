package com.library.system.handler;

import com.library.system.exception.loanException.RegisterLoanException;
import com.library.system.exception.memberException.FindMemberByIdException;
import com.library.system.model.Book;
import com.library.system.model.Member;
import com.library.system.service.impl.LoanServiceImpl;
import com.library.system.controller.LoanController;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoanHandler {

    private final Scanner scanner = new Scanner(System.in);
    private final LoanServiceImpl loanService;
    private final LoanController loanController;

    public LoanHandler(LoanServiceImpl loanService, LoanController loanController) {
        this.loanService = loanService;
        this.loanController = loanController;
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
                case 6:
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
        System.out.println("\n\u001B[34m======== Opérations sur les emprunts ========\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.printf("| %-2s | %-40s |\n", "1", "\u001B[32mEnregister un emprunt\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "6", "\u001B[31mQuitter\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.print("\u001B[33mEntrez votre choix: \u001B[0m");
    }

    // Méthode pour enregistrer un emprunt
    public void registerLoan() {
        // Étape 1 : Obtenir les informations du membre
        System.out.print("Entrez l'ID du membre qui souhaite emprunter des livres : ");
        int memberId = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne après nextInt()

        Member member;
        try {
            member = loanController.getMemberById(memberId); // 🔍 Récupérer le membre par ID
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

        // Parcourir chaque ID et récupérer le livre correspondant
        for (String bookIdStr : bookIds) {
            try {
                int bookId = Integer.parseInt(bookIdStr); // Convertir String en int
                Book book = loanController.getBookById(bookId); // Récupérer le livre par ID

                if (book != null) {
                    books.add(book);
                } else {
                    System.out.println("⚠️ Livre avec ID " + bookId + " introuvable.");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ ID invalide : " + bookIdStr);
            }
        }

        // Vérifier si au moins un livre a été trouvé
        if (books.isEmpty()) {
            System.out.println("❌ Aucun livre valide n'a été trouvé avec les IDs fournis.");
            return;
        }

        // Étape 3 : Enregistrer l'emprunt via le LoanService
        try {
            loanService.registerLoan(member, books);  // Appeler le service pour enregistrer l'emprunt
            System.out.println("\u001B[32m✅ Emprunt enregistré avec succès !\u001B[0m");
        } catch (RegisterLoanException e) {
            System.out.println("❌ Erreur lors de l'enregistrement du prêt : " + e.getMessage());
        }
    }

}
