package com.library.system.repository;


import com.library.system.model.Loan;


import java.sql.SQLException;
import java.util.List;


public interface PenaltiesRepository {


    int calculatePenalty(Loan loan);  // Calcul de la pénalité


    int getPenaltyRate();  // Récupérer le taux de pénalité


    List<Loan> getLoansWithDelays() throws SQLException;  // Récupérer les prêts en retard


    void updatePenalty(int loanId, int penalty) throws SQLException;  // Mettre à jour la pénalité
}

