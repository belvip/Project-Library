package com.library.system.controller;


import com.library.system.model.Member;
import com.library.system.service.PenaltiesService;
import com.library.system.model.Loan;
import java.sql.SQLException;
import java.util.List;


public class PenaltiesController {

    private final PenaltiesService penaltiesService;

    // Constructeur pour injecter PenaltiesService
    public PenaltiesController(PenaltiesService penaltiesService) {
        this.penaltiesService = penaltiesService;
    }


    // Exemple de méthode pour afficher les prêts en retard
    public void displayDelayedLoans() {
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_CYAN = "\u001B[36m";


        try {
            List<Loan> delayedLoans = penaltiesService.getLoansWithDelays();
            if (delayedLoans.isEmpty()) {
                System.out.println("Aucun emprunt en retard.");
            } else {
                // Affichage de l'en-tête en couleur
                System.out.println(ANSI_YELLOW + "+------------+---------------------+-------------+-------------------+---------------------+--------------+" + ANSI_RESET);
                System.out.printf(ANSI_CYAN + "| %-10s | %-19s | %-11s | %-17s | %-19s | %-12s |\n" + ANSI_RESET,
                        "Emprunt ID", "Nom du Membre", "Membre ID", "Date d'échéance", "Date de Retour", "Pénalité");
                System.out.println(ANSI_YELLOW + "+------------+---------------------+-------------+-------------------+---------------------+--------------+" + ANSI_RESET);


                // Affichage des données
                for (Loan loan : delayedLoans) {
                    Member member = loan.getMember();
                    int penaltyAmount = penaltiesService.calculatePenalty(loan);


                    System.out.printf("| %-10d | %-19s | %-11d | %-17s | %-19s | %-12d |\n",
                            loan.getLoanId(),
                            member.getFirstName() + " " + member.getLastName(),
                            member.getMember_id(),
                            loan.getFormattedDueDate(),
                            loan.getFormattedReturnedDate(),
                            penaltyAmount);
                }
                System.out.println(ANSI_YELLOW + "+------------+---------------------+-------------+-------------------+---------------------+--------------+" + ANSI_RESET);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'affichage des emprunts en retard : " + e.getMessage());
        }
    }


    public void applyPenaltiesToDelayedLoans() {
        try {
            penaltiesService.handleDelaysAndPenalties();
        } catch (Exception e) {  // Ou une autre exception si nécessaire
            System.err.println("Erreur lors de l'application des pénalités : " + e.getMessage());
        }
    }


    // Méthode pour afficher le taux de pénalité
    public void displayPenaltyRate() {
        int penaltyRate = penaltiesService.getPenaltyRate();
        System.out.println("Le taux de pénalité est : " + penaltyRate + " F CFA par jour.");
    }
}



