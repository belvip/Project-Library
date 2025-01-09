package com.library.system.repository.impl;


import com.library.system.dao.PenaltiesDAO;
import com.library.system.model.Loan;
import com.library.system.repository.PenaltiesRepository;
import com.library.system.util.DatabaseConnection;


import java.sql.*;
import java.util.List;


public class PenaltiesRepositoryImpl implements PenaltiesRepository {


    private PenaltiesDAO penaltiesDAO;
    private Connection connection;


    // Constructeur
    public PenaltiesRepositoryImpl(PenaltiesDAO penaltiesDAO) {
        this.penaltiesDAO = penaltiesDAO;
        try {
            this.connection = DatabaseConnection.getConnection(); // Peut lancer SQLException
        } catch (SQLException e) {
            // Gérer l'exception, par exemple en affichant un message d'erreur
            System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
        }
    }


    @Override
    public int calculatePenalty(Loan loan) {
        // Déléguer le calcul de la pénalité au DAO
        return penaltiesDAO.calculatePenalty(loan);
    }


    @Override
    public int getPenaltyRate() {
        // Déléguer la récupération du taux de pénalité au DAO
        return penaltiesDAO.getPenaltyRate();
    }


    @Override
    public List<Loan> getLoansWithDelays() throws SQLException {
        // Déléguer l'appel au DAO pour récupérer les prêts en retard
        return penaltiesDAO.getLoansWithDelays();
    }


    @Override
    public void updatePenalty(int loanId, int penalty) throws SQLException {
        // Connexion à la base de données et mise à jour de la pénalité
        String updatePenaltyQuery = "UPDATE Loan SET penalty = ? WHERE loan_id = ?";


        try (Connection connection = DriverManager.getConnection("library_db", "postgres", "belvi");
             PreparedStatement statement = connection.prepareStatement(updatePenaltyQuery)) {


            statement.setInt(1, penalty);  // Pénalité calculée
            statement.setInt(2, loanId);   // ID du prêt


            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Pénalité mise à jour avec succès pour le prêt ID: " + loanId);
            } else {
                System.out.println("Aucun prêt trouvé avec l'ID: " + loanId);
            }
        }
    }
}

