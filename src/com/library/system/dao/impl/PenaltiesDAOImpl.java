package com.library.system.dao.impl;


import com.library.system.dao.PenaltiesDAO;
import com.library.system.model.Loan;
import com.library.system.model.Member;
import com.library.system.util.DatabaseConnection;


import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


public class PenaltiesDAOImpl implements PenaltiesDAO {


    @Override
    public int calculatePenalty(Loan loan) {
        // Vérifier si la date d'échéance est dépassée et que le livre n'a pas encore été retourné
        if (loan.getDueDate().isBefore(ZonedDateTime.now()) && loan.getReturnedDate() == null) {
            // Calculer la durée en jours entre la date d'échéance et maintenant
            long daysOverdue = java.time.Duration.between(loan.getDueDate(), ZonedDateTime.now()).toDays();

            // Récupérer le taux de pénalité en F CFA par jour
            int penaltyRatePerDay = 1000;  // Exemple : 1000 F CFA par jour de retard

            // Calculer la pénalité totale
            return (int) (daysOverdue * penaltyRatePerDay);
        }
        return 0; // Pas de pénalité si le livre est retourné à temps ou s'il a déjà été retourné
    }


    @Override
    public void updatePenaltyInDatabase(Loan loan) {
        String query = "UPDATE Loan SET penalty = ? WHERE loan_id = ?";


        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Calculer la pénalité
            int penalty = calculatePenalty(loan);


            // Définir les paramètres
            preparedStatement.setInt(1, penalty); // Pénalité calculée
            preparedStatement.setInt(2, loan.getLoanId()); // ID du prêt


            // Exécuter la mise à jour
            preparedStatement.executeUpdate();
            //System.out.println("Pénalité mise à jour pour l'emprunt ID " + loan.getLoanId());
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la pénalité: " + e.getMessage());
        }
    }


    @Override
    public int getPenaltyRate() {
        // Exemple de récupération du taux de pénalité (ici un taux fixe, mais pourrait être dynamique)
        return 100;  // Taux de pénalité (100 FCFA par jour)
    }


    @Override
    public List<Loan> getLoansWithDelays() throws SQLException {
        List<Loan> delayedLoans = new ArrayList<>();


        // Ajouter "m.email" dans la requête pour récupérer l'email
        String query = "SELECT l.loan_id, l.loanDate, l.dueDate, l.returnDate, l.member_id, " +
                "m.first_name, m.last_name, m.email " +
                "FROM Loan l JOIN Member m ON l.member_id = m.member_id " +
                "WHERE l.returnDate IS NULL AND l.dueDate < CURRENT_TIMESTAMP";


        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library_system", "postgres", "belvi");
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {


            while (resultSet.next()) {
                // Extraire les informations du prêt
                int loanId = resultSet.getInt("loan_id");
                ZonedDateTime loanDate = resultSet.getTimestamp("loanDate").toLocalDateTime().atZone(ZoneId.systemDefault());
                ZonedDateTime dueDate = resultSet.getTimestamp("dueDate").toLocalDateTime().atZone(ZoneId.systemDefault());
                ZonedDateTime returnDate = resultSet.getTimestamp("returnDate") == null ? null : resultSet.getTimestamp("returnDate").toLocalDateTime().atZone(ZoneId.systemDefault());


                // Extraire les informations du membre
                int memberId = resultSet.getInt("member_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");


                // Créer l'objet Member avec l'email
                java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis()); // Date actuelle
                Member member = new Member(memberId, firstName, lastName, email, currentDate);


                // Créer l'objet Loan avec Member
                Loan loan = new Loan(loanId, loanDate, dueDate, returnDate, member);


                // Mettre à jour la pénalité dans la base de données
                updatePenaltyInDatabase(loan);


                // Ajouter le prêt à la liste des prêts en retard
                delayedLoans.add(loan);
            }
        }


        return delayedLoans;
    }



}

