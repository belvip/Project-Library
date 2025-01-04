package com.library.system.dao.impl;

import com.library.system.dao.LoanDAO;

import com.library.system.exception.loanException.RegisterLoanException;
import com.library.system.model.Book;
import com.library.system.model.Loan;
import com.library.system.model.Member;

import java.sql.*;
import java.time.ZonedDateTime;
import java.util.List;

public class LoanDAOImpl implements LoanDAO {

    private Connection connection;  // Connection à la base de données

    // Constructeur pour initialiser la connexion à la base de données
    public LoanDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void registerLoan(Member member, List<Book> books) throws RegisterLoanException {
        // Vérifier que les informations sont valides
        if (member == null || books == null || books.isEmpty()) {
            throw new RegisterLoanException("Le membre ou la liste des livres est invalide.");
        }

        // Créer un prêt avec la date actuelle et la date d'échéance (par exemple, 2 semaines plus tard)
        ZonedDateTime loanDate = ZonedDateTime.now();
        ZonedDateTime dueDate = loanDate.plusWeeks(2); // Par exemple, la date d'échéance est dans 2 semaines

        // Créer un objet Loan
        Loan loan = new Loan(0, loanDate, dueDate, null, member); // L'ID du prêt sera généré automatiquement par la DB

        // Démarrer une transaction
        try {
            // Démarrer une transaction
            connection.setAutoCommit(false);

            // Insérer le prêt dans la base de données
            String loanQuery = "INSERT INTO Loan (loandate, duedate, member_id) VALUES (?, ?, ?)";
            try (PreparedStatement loanStatement = connection.prepareStatement(loanQuery, Statement.RETURN_GENERATED_KEYS)) {
                loanStatement.setTimestamp(1, Timestamp.from(loanDate.toInstant()));
                loanStatement.setTimestamp(2, Timestamp.from(dueDate.toInstant()));
                loanStatement.setInt(3, member.getId());

                int affectedRows = loanStatement.executeUpdate();
                if (affectedRows > 0) {
                    ResultSet generatedKeys = loanStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        loan.setLoanId(generatedKeys.getInt(1));  // Récupérer l'ID du prêt généré
                    }
                }
            }

            // Insérer les livres empruntés dans la table de jointure Book_Loan
            String bookLoanQuery = "INSERT INTO Book_Loan (book_id, loan_id) VALUES (?, ?)";
            try (PreparedStatement bookLoanStatement = connection.prepareStatement(bookLoanQuery)) {
                for (Book book : books) {
                    bookLoanStatement.setInt(1, book.getBook_id());
                    bookLoanStatement.setInt(2, loan.getLoanId());
                    bookLoanStatement.addBatch();  // Ajouter l'opération dans un lot
                }
                bookLoanStatement.executeBatch();  // Exécuter toutes les insertions en une seule fois
            }

            // Commit de la transaction
            connection.commit();

        } catch (SQLException e) {
            // En cas d'erreur, rollback de la transaction
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new RegisterLoanException("Erreur lors du rollback de la transaction", rollbackEx);
            }
            throw new RegisterLoanException("Erreur lors de l'enregistrement du prêt");

        } finally {
            try {
                connection.setAutoCommit(true);  // Réinitialiser l'autocommit à true
            } catch (SQLException e) {
                throw new RegisterLoanException("Erreur lors de la réinitialisation de l'autocommit");
            }
        }
    }
}
