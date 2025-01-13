package com.library.system.handler;


import com.library.system.exception.memberException.FindMemberByIdException;
import com.library.system.exception.memberException.FindMemberByNameException;
import com.library.system.model.Loan;
import com.library.system.model.Member;
import com.library.system.service.impl.MemberServiceImpl;
import com.library.system.controller.MemberController; // Ajout de l'importation
import com.library.system.util.Logger;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


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
                    searchMemberById();
                    break;
                case 5:
                    displayLoanHistory();
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
            scanner.nextLine(); // Nettoie le buffer
            return -1;
        }
    }


    // Méthode pour afficher le menu des opérations
    private void displayMemberMenu() {
        //System.out.println("\n\u001B[34m======== Opérations sur les membres ========\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.printf("| %-2s | %-40s |\n", "1", "\u001B[32mEnregister un membre\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "2", "\u001B[32mSupprimer un membre\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "3", "\u001B[32mRechercher les membres par mot clé\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "4", "\u001B[32mRechercher un membre par ID\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "5", "\u001B[32mAfficher emprunts d'un memebre\u001B[0m");
        System.out.printf("| %-2s | %-40s |\n", "6", "\u001B[31mRetourner au menu principal\u001B[0m");
        System.out.println("+--------------------------------------------+");
        System.out.print("\u001B[33mEntrez votre choix: \u001B[0m");
    }


    // Méthode pour enregistrer un nouveau membre
    private void registerMember() {
        Logger.logInfo("------------------ Enregistrement d'un Membre ------------------ ");

        // Demander le prénom
        String firstName = getInput("Entrez le prénom du membre : ");
        if (!firstName.matches("^[\\p{L}&\\s-]+$")) {
            Logger.logWarn("Prénom du membre ", "Le prénom ne peut contenir que des lettres, des espaces, des tirets ou le caractère '&'.");
            return;
        }

        // Demander le nom
        String lastName = getInput("Entrez le nom du membre : ");
        if (!lastName.matches("^[\\p{L}&\\s-]+$")) {
            Logger.logWarn("Nom du membre ", "Le nom ne peut contenir que des lettres, des espaces, des tirets ou le caractère '&'.");
            return;
        }

        // Demander l'email
        String email = getInput("Entrez l'email du membre : ").trim().toLowerCase(); // Normalisation
        if (!email.matches("^[\\p{L}0-9._%+-]+@[\\p{L}0-9.-]+\\.[A-Za-z]{2,10}$")) {
            Logger.logWarn("Email du membre ", "L'email doit être valide.");
            return;
        }

        // Vérifier si l'email est déjà utilisé avant de tenter l'ajout
        if (memberService.isEmailTaken(email)) {
            Logger.logWarn("Email du membre", "L'email " + email + " est déjà pris. Veuillez en choisir un autre.");
            return;
        }

        // Date d'adhésion
        Date adhesionDate = Date.valueOf(LocalDate.now());

        // Création du membre
        Member newMember = new Member(firstName, lastName, email, adhesionDate);

        // Ajout en base de données
        try {
            memberService.addMember(newMember);
            Logger.logSuccess("✅ Membre ajouté avec succès !");
        } catch (Exception e) {
            Logger.logError("Ajout du membre", e);
        }
    }

    private void deleteMember() {
        Logger.logInfo("Supprimer un membre");
        System.out.print("Entrez l'ID du membre à supprimer : ");
        int memberID = scanner.nextInt();
        memberController.deleteMember(memberID);  // Utiliser MemberController pour supprimer le membre
    }


    // Méthode pour rechercher un membre
    private void searchMember() {
        Logger.logInfo("Rechercher un membre");
        System.out.print("Entrez un mot ou une lettre  : ");
        scanner.nextLine();  // Consommer le newline restant
        String memberName = scanner.nextLine();

        try {
            List<Member> members = memberController.findMemberByName(memberName);  // Appel de la méthode dans MemberController

            if (members.isEmpty()) {
                Logger.logInfo("Aucun membre trouvé avec ce nom.");
            } else {
                // Afficher les membres trouvés sous forme de tableau
                displayMemberTable(members);
            }
        } catch (FindMemberByNameException e) {
            Logger.logError("Erreur lors de la recherche du membre par nom", e);
        }
    }

    private void displayMemberTable(List<Member> members) {
        Logger.logInfo("Affichage des membres");

        // Codes ANSI pour les couleurs
        String BLUE = "\u001B[34m";
        String RESET = "\u001B[0m";

        // Vérification si la liste des membres est vide
        if (members == null || members.isEmpty()) {
            System.out.println("\nAucun membre trouvé.");
            return;
        }


        // Détermination des largeurs maximales pour chaque colonne
        int idWidth = "ID".length();
        int firstNameWidth = "Prénom".length();
        int lastNameWidth = "Nom".length();
        int emailWidth = "Email".length();
        int adhesionDateWidth = "Date d'adhésion".length();


        for (Member member : members) {
            idWidth = Math.max(idWidth, String.valueOf(member.getMember_id()).length());
            firstNameWidth = Math.max(firstNameWidth, member.getFirstName().length());
            lastNameWidth = Math.max(lastNameWidth, member.getLastName().length());
            emailWidth = Math.max(emailWidth, member.getEmail().length());
        }


        // Création du format pour la date et l'heure
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        // Ligne de séparation
        String horizontalLine = BLUE + "╔" + "═".repeat(idWidth + 2) + "╦" +
                "═".repeat(firstNameWidth + 2) + "╦" +
                "═".repeat(lastNameWidth + 2) + "╦" +
                "═".repeat(emailWidth + 2) + "╦" +
                "═".repeat(adhesionDateWidth + 2) + "╗" + RESET;


        String separatorLine = BLUE + "╠" + "═".repeat(idWidth + 2) + "╬" +
                "═".repeat(firstNameWidth + 2) + "╬" +
                "═".repeat(lastNameWidth + 2) + "╬" +
                "═".repeat(emailWidth + 2) + "╬" +
                "═".repeat(adhesionDateWidth + 2) + "╣" + RESET;


        String footerLine = BLUE + "╚" + "═".repeat(idWidth + 2) + "╩" +
                "═".repeat(firstNameWidth + 2) + "╩" +
                "═".repeat(lastNameWidth + 2) + "╩" +
                "═".repeat(emailWidth + 2) + "╩" +
                "═".repeat(adhesionDateWidth + 2) + "╝" + RESET;


        // Format d'affichage
        String format = "║ %-"+idWidth+"s ║ %-"+firstNameWidth+"s ║ %-"+lastNameWidth+"s ║ %-"+emailWidth+"s ║ %-"+adhesionDateWidth+"s ║\n";


        // Affichage de l'en-tête
        System.out.println(horizontalLine);
        System.out.printf(BLUE + format + RESET, "ID", "Prénom", "Nom", "Email", "Date d'adhésion");
        System.out.println(separatorLine);


        // Affichage des membres
        for (Member member : members) {
            String formattedAdhesionDate = sdf.format(member.getAdhesionDate());
            System.out.printf(format,
                    member.getMember_id(),
                    member.getFirstName(),
                    member.getLastName(),
                    member.getEmail(),
                    formattedAdhesionDate);
        }


        // Affichage du footer
        System.out.println(footerLine);
    }

    // Méthode pour récupérer l'entrée de l'utilisateur
    private String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }


    public void searchMemberById() {
        System.out.print("Entrez l'ID du membre à rechercher : ");
        int memberID = scanner.nextInt();


        try {
            Member member = memberController.findMemberById(memberID);


            // Créer une liste contenant un seul élément
            List<Member> memberList = new ArrayList<>();
            memberList.add(member);


            // Afficher le membre sous forme de tableau
            displayMemberTable(memberList);
        } catch (FindMemberByIdException e) {
            Logger.logError("Erreur : ", e);
        }
    }

    public void displayLoanHistory() {
        Logger.logInfo("Historique des emprunts pour un membre");
        Scanner scanner = new Scanner(System.in);
        try {
            // Demander l'ID du membre à l'utilisateur
            System.out.print("Entrez l'ID du membre : ");
            int memberId = scanner.nextInt();

            // Récupérer l'historique des emprunts
            List<Loan> loansHistory = memberController.getLoanHistory(memberId);


            // Vérifier si des emprunts existent
            if (loansHistory.isEmpty()) {
                Logger.logInfo("Aucun emprunt trouvé pour ce membre.");
            } else {
                // Codes ANSI pour la couleur du texte (ici en bleu pour l'entête)
                String blue = "\033[34m"; // Code ANSI pour la couleur bleue
                String reset = "\033[0m"; // Code pour réinitialiser la couleur à la couleur par défaut

                // Afficher l'entête du tableau avec une couleur
                System.out.println(blue + String.format("%-15s%-20s%-25s%-25s", "ID Emprunt", "Date Emprunt", "Date Retour prévue", "Date Retour effective") + reset);
                System.out.println("---------------------------------------------------------------------------------------------------");

                // Afficher les lignes du tableau sans couleur (par défaut)
                for (Loan loan : loansHistory) {
                    System.out.println(String.format("%-15d%-20s%-25s%-25s",
                            loan.getLoanId(),
                            loan.getFormattedLoanDate(),
                            loan.getFormattedDueDate(),
                            loan.getFormattedReturnedDate()));
                }
            }
        } catch (Exception e) {
            Logger.logError("Erreur lors de la récupération de l'historique des emprunts : ", e);
        }
    }



}


