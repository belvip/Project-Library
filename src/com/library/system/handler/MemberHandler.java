package com.library.system.handler;

import com.library.system.exception.memberException.FindMemberByNameException;
import com.library.system.model.Member;
import com.library.system.service.impl.MemberServiceImpl;
import com.library.system.controller.MemberController; // Ajout de l'importation

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static java.awt.Color.red;

public class MemberHandler {

    private final Scanner scanner = new Scanner(System.in);
    private final MemberServiceImpl memberService;
    private final MemberController memberController;  // Ajout du controller

    // Modification du constructeur pour accepter MemberController en plus de MemberService
    public MemberHandler(MemberServiceImpl memberService, MemberController memberController) {
        this.memberService = memberService;
        this.memberController = memberController;  // Initialisation du controller
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
                    deleteMember();
                    break;
                case 3:
                    searchMember();
                    break;
                case 4:
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
        System.out.printf("| %-2s | %-40s |\n", "2", "\u001B[32mSupprimer un membre\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "3", "\u001B[32mRechercher les membres par mot clé\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "4", "\u001B[31mQuitter\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.print("\u001B[33mEntrez votre choix: \u001B[0m");
    }

    // Méthode pour enregistrer un nouveau membre
    // Méthode pour enregistrer un nouveau membre
    private void registerMember() {
        // Affichage pour séparer les étapes
        System.out.println("\n\u001B[34m===== Enregistrement d'un Membre =====\u001B[0m");

        // Demander le prénom
        String firstName = getInput("Entrez le prénom du membre : ");
        // Validation du prénom (lettres uniquement)
        if (!firstName.matches("^[A-Za-z]+$")) {
            System.out.println("⚠️ Le prénom ne doit contenir que des lettres.");
            return;
        }

        // Demander le nom
        String lastName = getInput("Entrez le nom du membre : ");
        // Validation du nom (lettres uniquement)
        if (!lastName.matches("^[A-Za-z]+$")) {
            System.out.println("⚠️ Le nom ne doit contenir que des lettres.");
            return;
        }

        // Demander l'email
        String email = getInput("Entrez l'email du membre : ");
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

    private void deleteMember() {
        System.out.print("Entrez l'ID du membre à supprimer : ");
        int memberID = scanner.nextInt();
        memberController.deleteMember(memberID);  // Utiliser MemberController pour supprimer le membre
    }

    // Méthode pour rechercher un membre
    private void searchMember() {
        System.out.print("Entrez le nom du membre à rechercher : ");
        scanner.nextLine();  // Consommer le newline restant
        String memberName = scanner.nextLine();

        try {
            List<Member> members = memberController.findMemberByName(memberName);  // Appel de la méthode dans MemberController

            if (members.isEmpty()) {
                System.out.println("❌ Aucun membre trouvé avec ce nom.");
            } else {
                // Afficher les membres trouvés sous forme de tableau
                displayMemberTable(members);
            }
        } catch (FindMemberByNameException e) {
            System.out.println("❌ Erreur : " + e.getMessage());
        }
    }


    private void displayMemberTable(List<Member> members) {
        // Code ANSI pour la couleur bleue
        String blue = "\u001B[34m";
        String reset = "\u001B[0m";

        // En-tête du tableau avec couleur bleue
        System.out.println(blue + "\n╔════════════╦════════════════════╦════════════════════╦════════════════════════════╦══════════════════╗" + reset);
        System.out.println(blue + "║   ID       ║   Prénom           ║   Nom             ║   Email                  ║   Date d'adhésion ║" + reset);
        System.out.println(blue + "╠════════════╬════════════════════╬════════════════════╬════════════════════════════╬══════════════════╣" + reset);

        // Création du format pour la date et l'heure
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Affichage des données de chaque membre
        for (Member member : members) {
            // Formater la date d'adhésion pour inclure l'heure
            String formattedAdhesionDate = sdf.format(member.getAdhesionDate());

            // Affichage du membre avec la date formatée
            System.out.printf("║ %-10d ║ %-18s ║ %-18s ║ %-24s ║ %-16s ║\n",
                    member.getId(), member.getFirstName(), member.getLastName(), member.getEmail(), formattedAdhesionDate);
        }

        // Footer du tableau
        System.out.println("╚════════════╩════════════════════╩════════════════════╩════════════════════════════╩══════════════════╝");
    }

    // Méthode pour récupérer l'entrée de l'utilisateur
    private String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }





}
