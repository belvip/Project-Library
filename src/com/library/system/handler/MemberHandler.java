package com.library.system.handler;

import com.library.system.model.Member;
import com.library.system.service.impl.MemberServiceImpl;
import com.library.system.controller.MemberController; // Ajout de l'importation

import java.sql.Date;
import java.time.LocalDate;
import java.util.Scanner;

public class MemberHandler {

    private final Scanner scanner = new Scanner(System.in);
    private final MemberServiceImpl memberService;

    // Si tu veux utiliser MemberController, tu peux l'ajouter ici
    public MemberHandler(MemberServiceImpl memberService, MemberController memberController) {
        this.memberService = memberService;
    }

    // Méthode pour traiter les opérations sur les membres
    public void handleMemberOperations() {
        boolean running = true;
        while (running) {
            displayMemberMenu();
            int choice = getChoiceInput();

            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerMember();
                    break;
                case 2:
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
            scanner.nextLine(); // Nettoie le buffer
            return -1;
        }
    }

    // Méthode pour afficher le menu des opérations
    private void displayMemberMenu() {
        System.out.println("\n\u001B[34m======== Opérations sur les livres ========\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.printf("| %-2s | %-40s |\n", "1", "\u001B[32mEnregister un membre\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "2", "\u001B[31mQuitter\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.print("\u001B[33mEntrez votre choix: \u001B[0m");
    }


    // Méthode pour enregistrer un nouveau membre
    private void registerMember() {
        // Affichage pour séparer les étapes
        System.out.println("\n\u001B[34m===== Enregistrement d'un Membre =====\u001B[0m");

        // Demander le prénom
        System.out.print("Entrez le prénom du membre : ");
        String firstName = scanner.nextLine().trim();  // Utilisation de nextLine pour lire le prénom

        // Demander le nom
        System.out.print("Entrez le nom du membre : ");
        String lastName = scanner.nextLine().trim();  // Utilisation de nextLine pour lire le nom

        // Demander l'email
        System.out.print("Entrez l'email du membre : ");
        String email = scanner.nextLine().trim();  // Utilisation de nextLine pour lire l'email

        // Validation de l'email
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            System.out.println("⚠️ Email invalide. Veuillez entrer un email valide.");
            return;
        }

        // Date d'adhésion (date actuelle)
        Date adhesionDate = Date.valueOf(LocalDate.now());

        // Création du membre
        Member newMember = new Member(firstName, lastName, email, adhesionDate);

        // Ajout en base de données
        try {
            memberService.addMember(newMember);
            System.out.println("\u001B[32m✅ Membre ajouté avec succès !\u001B[0m");
        } catch (Exception e) {
            System.out.println("\u001B[31m❌ Erreur lors de l'ajout du membre : " + e.getMessage() + "\u001B[0m");
        }
    }

}
