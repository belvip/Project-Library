package com.library.system.dao.impl;


import com.library.system.dao.PenaltiesDAO;
import com.library.system.model.Loan;
import com.library.system.model.Member;


import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


public class PenaltiesDAOImpl implements PenaltiesDAO {


   /*@Override
   public int calculatePenalty(Loan loan) {
       // Logique de calcul de la pénalité
       // Par exemple, multiplier le nombre de jours de retard par le taux de pénalité
       int daysOverdue = loan.getDueDate().isBefore(java.time.ZonedDateTime.now()) ?
               (int) java.time.Duration.between(loan.getDueDate(), java.time.ZonedDateTime.now()).toDays() : 0;
       int penaltyRate = 100;  // Exemple de taux de pénalité
       return daysOverdue * penaltyRate;
   } */


    @Override
    public int calculatePenalty(Loan loan) {
        // Vérifier si la date d'échéance est dépassée
        if (loan.getDueDate().isBefore(java.time.ZonedDateTime.now())) {
            // Calculer la durée en minutes entre la date d'échéance et maintenant
            long minutesOverdue = java.time.Duration.between(loan.getDueDate(), java.time.ZonedDateTime.now()).toMinutes();


            // Récupérer le taux de pénalité
            int penaltyRate = 100;  // Exemple de taux de pénalité en F CFA par minute


            // Calculer la pénalité totale
            return (int) (minutesOverdue * penaltyRate);
        }


        // Si aucune pénalité n'est applicable (pas de retard)
        return 0;
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


        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library_db", "postgres", "belvi");
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {


            while (resultSet.next()) {
                int loanId = resultSet.getInt("loan_id");
                ZonedDateTime loanDate = resultSet.getTimestamp("loanDate").toLocalDateTime().atZone(ZoneId.systemDefault());
                ZonedDateTime dueDate = resultSet.getTimestamp("dueDate").toLocalDateTime().atZone(ZoneId.systemDefault());
                ZonedDateTime returnDate = resultSet.getTimestamp("returnDate") == null ? null : resultSet.getTimestamp("returnDate").toLocalDateTime().atZone(ZoneId.systemDefault());


                // Récupérer les informations du membre
                int memberId = resultSet.getInt("member_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");  // Récupérer l'email du membre


                // Utilisation correcte de java.sql.Date pour obtenir la date actuelle
                java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis()); // Date actuelle


                // Créer l'objet Member avec l'email
                Member member = new Member(memberId, firstName, lastName, email, currentDate);


                // Créer l'objet Loan avec Member
                Loan loan = new Loan(loanId, loanDate, dueDate, returnDate, member);
                delayedLoans.add(loan);
            }
        }


        return delayedLoans;
    }




}

