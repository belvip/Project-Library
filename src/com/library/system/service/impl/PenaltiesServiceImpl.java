package com.library.system.service.impl;


import com.library.system.model.Loan;
import com.library.system.repository.PenaltiesRepository;
import com.library.system.service.PenaltiesService;


import java.sql.SQLException;
import java.util.List;


public class PenaltiesServiceImpl implements PenaltiesService {


    private final PenaltiesRepository penaltiesRepository;


    // Constructeur qui prend le PenaltiesRepository
    public PenaltiesServiceImpl(PenaltiesRepository penaltiesRepository) {
        this.penaltiesRepository = penaltiesRepository;
    }


    @Override
    public int calculatePenalty(Loan loan) {
        // Appel à la méthode du repository pour calculer la pénalité
        return penaltiesRepository.calculatePenalty(loan);
    }


    @Override
    public void handleDelaysAndPenalties() {
        try {
            // Logique pour gérer les emprunts en retard et appliquer les pénalités
            List<Loan> delayedLoans = penaltiesRepository.getLoansWithDelays();


            if (delayedLoans.isEmpty()) {
                System.out.println("✅ Aucun emprunt en retard.");
            } else {
                System.out.println("⚠️ Emprunts en retard :");


                for (Loan loan : delayedLoans) {
                    // Calcul de la pénalité pour chaque emprunt
                    int penalty = calculatePenalty(loan);


                    // Mise à jour de la pénalité dans la base de données
                    penaltiesRepository.updatePenalty(loan.getLoanId(), penalty);


                    // Affichage de l'emprunt et de la pénalité appliquée
                    System.out.println(String.format("Emprunt ID: %d, Pénalité appliquée: %d F CFA", loan.getLoanId(), penalty));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la gestion des retards et des pénalités : " + e.getMessage());
        }
    }


    @Override
    public void updatePenalty(int loanId, int penalty) throws SQLException {
        // Appel au repository pour mettre à jour la pénalité dans la base de données
        penaltiesRepository.updatePenalty(loanId, penalty);
    }


    @Override
    public int getPenaltyRate() {
        // Appel au repository pour récupérer le taux de pénalité
        return penaltiesRepository.getPenaltyRate();
    }


    @Override
    public List<Loan> getLoansWithDelays() throws SQLException {
        // Appel au repository pour récupérer les prêts en retard
        return penaltiesRepository.getLoansWithDelays();
    }
}



