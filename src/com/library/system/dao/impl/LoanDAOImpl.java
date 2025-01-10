package com.library.system.dao.impl;


import com.library.system.dao.LoanDAO;
import com.library.system.exception.loanException.RegisterLoanException;
import com.library.system.model.*;


import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class LoanDAOImpl implements LoanDAO {
    private final Connection connection;
    // Constructeur pour initialiser la connexion à la base de données
    public LoanDAOImpl(Connection connection) {
        this.connection = connection;
    }
    public Connection getConnection() {
        return this.connection;
    }


    @Override
    public void registerLoan(Member member, List<Book> books) throws RegisterLoanException {
        // Vérifier que les informations sont valides
        if (member == null || books == null || books.isEmpty()) {
            throw new RegisterLoanException("Le membre ou la liste des livres est invalide.");
        }


        // Créer un prêt avec la date actuelle et la date d'échéance (par exemple, 2 semaines plus tard)
        ZonedDateTime loanDate = ZonedDateTime.now();
        ZonedDateTime dueDate = loanDate.plusWeeks(7); // Par exemple, la date d'échéance est dans 1 semaine
        //ZonedDateTime dueDate = loanDate.plusMinutes(2);


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
                loanStatement.setInt(3, member.getMember_id());


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


            // Mettre à jour le nombre de copies des livres
            for (Book book : books) {
                if (!isBookAvailable(book.getBook_id())) {
                    throw new RegisterLoanException("Le livre " + book.getBook_id() + " n'est plus disponible.");
                }
                updateBookCopies(book.getBook_id());
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
            throw new RegisterLoanException("❌ Erreur lors de l'enregistrement du prêt");


        } finally {
            try {
                connection.setAutoCommit(true);  // Réinitialiser l'autocommit à true
            } catch (SQLException e) {
                throw new RegisterLoanException("❌ Erreur lors de la réinitialisation de l'autocommit");
            }
        }
    }


    @Override
    public void returnBook(int loanId) throws SQLException {
        try {
            // Démarrer une transaction
            connection.setAutoCommit(false);


            // Récupérer les informations du prêt, y compris la dueDate
            String selectQuery = "SELECT dueDate FROM Loan WHERE loan_id = ?";
            ZonedDateTime returnDate = ZonedDateTime.now();  // Date actuelle de retour
            int penalty = 0;  // Initialiser la pénalité à 0


            try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery)) {
                selectStmt.setInt(1, loanId);
                ResultSet resultSet = selectStmt.executeQuery();


                if (resultSet.next()) {
                    // Récupérer la dueDate du prêt
                    Timestamp dueDateTimestamp = resultSet.getTimestamp("dueDate");
                    ZonedDateTime dueDate = dueDateTimestamp.toInstant().atZone(ZoneId.systemDefault());


                    // Vérifier si le retour est en retard et calculer la pénalité si nécessaire
                    if (returnDate.isAfter(dueDate)) {
                        long lateMinutes = java.time.Duration.between(dueDate, returnDate).toMinutes();
                        // Exemple de calcul de pénalité : 1 unité de pénalité pour chaque minute de retard
                        penalty = (int) lateMinutes;  // La pénalité peut être ajustée en fonction des règles


                        // Mettre à jour la pénalité dans la base de données
                        String updatePenaltyQuery = "UPDATE Loan SET penalty = ? WHERE loan_id = ?";
                        try (PreparedStatement updatePenaltyStmt = connection.prepareStatement(updatePenaltyQuery)) {
                            updatePenaltyStmt.setInt(1, penalty);
                            updatePenaltyStmt.setInt(2, loanId);
                            updatePenaltyStmt.executeUpdate();
                        }
                    }
                } else {
                    //throw new SQLException("Prêt introuvable avec l'ID " + loanId);
                    throw new SQLException("\u001B[31m❌ Prêt introuvable avec l'ID " + loanId + "\u001B[0m");

                }
            }

            // Récupérer les livres liés à ce prêt et incrémenter le nombre de copies
            String selectBooksQuery = "SELECT book_id FROM Book_Loan WHERE loan_id = ?";
            try (PreparedStatement selectBooksStmt = connection.prepareStatement(selectBooksQuery)) {
                selectBooksStmt.setInt(1, loanId);
                ResultSet resultSet = selectBooksStmt.executeQuery();


                while (resultSet.next()) {
                    int bookId = resultSet.getInt("book_id");
                    incrementBookCopies(bookId);
                }
            }


            // Mettre à jour la date de retour
            String updateQuery = "UPDATE Loan SET returndate = ? WHERE loan_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
                pstmt.setTimestamp(1, Timestamp.from(returnDate.toInstant()));  // Date actuelle
                pstmt.setInt(2, loanId);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("❌ Impossible de mettre à jour la date de retour pour le prêt avec l'ID " + loanId);
                }
            }


            // Commit de la transaction
            connection.commit();
            System.out.println("📌 Prêt " + loanId + " traité avec succès. Pénalité appliquée: " + penalty + " unités.");


        } catch (SQLException e) {
            // En cas d'erreur, rollback
            connection.rollback();
            throw new SQLException("❌ Erreur lors du traitement du retour pour le prêt avec l'ID " + loanId, e);


        } finally {
            // Réinitialiser l'autocommit
            connection.setAutoCommit(true);
        }
    }


    @Override
    public void getAllLoans(List<Loan> loans) throws SQLException {
        String query = "SELECT l.loan_id, m.first_name, m.last_name, m.email, l.loanDate, l.dueDate, l.returnDate, " +
                "bl.book_id, b.title, b.number_of_copies, " +
                "STRING_AGG(a.first_name || ' ' || a.last_name, ', ') AS authors, " +
                "STRING_AGG(c.category_name, ', ') AS categories " +
                "FROM Loan l " +
                "JOIN Member m ON l.member_id = m.member_id " +
                "JOIN Book_Loan bl ON l.loan_id = bl.loan_id " +
                "JOIN Book b ON bl.book_id = b.book_id " +
                "LEFT JOIN Book_Author ba ON b.book_id = ba.book_id " +
                "LEFT JOIN Author a ON ba.author_id = a.author_id " +
                "LEFT JOIN Books_Category bc ON b.book_id = bc.book_id " +
                "LEFT JOIN Category c ON bc.category_id = c.category_id " +
                "GROUP BY l.loan_id, m.first_name, m.last_name, m.email, l.loanDate, l.dueDate, l.returnDate, bl.book_id, b.title, b.number_of_copies " +
                "ORDER BY l.loanDate DESC";


        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {


            // Parcours des résultats de la requête
            while (rs.next()) {
                int loanId = rs.getInt("loan_id");
                String memberFirstName = rs.getString("first_name");
                String memberLastName = rs.getString("last_name");
                String memberEmail = rs.getString("email");


                // Conversion explicite des résultats SQL en ZonedDateTime
                ZonedDateTime loanDate = rs.getTimestamp("loanDate").toInstant().atZone(ZoneId.systemDefault());
                ZonedDateTime dueDate = rs.getTimestamp("dueDate").toInstant().atZone(ZoneId.systemDefault());
                ZonedDateTime returnedDate = null;
                Timestamp returnDateTimestamp = rs.getTimestamp("returnDate");
                if (returnDateTimestamp != null) {
                    returnedDate = returnDateTimestamp.toInstant().atZone(ZoneId.systemDefault());
                }


                // Formatage des dates au format "yyyy-MM-dd"
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedLoanDate = loanDate.format(formatter);
                String formattedDueDate = dueDate.format(formatter);
                String formattedReturnDate = returnedDate != null ? returnedDate.format(formatter) : "Pas encore retourné";


                int bookId = rs.getInt("book_id");
                String bookTitle = rs.getString("title");
                int numberOfCopies = rs.getInt("number_of_copies");


                // Récupérer les auteurs et les catégories
                String authors = rs.getString("authors"); // Liste des auteurs concaténée
                String categories = rs.getString("categories"); // Liste des catégories concaténée


                // Création d'un membre
                Member member = new Member(memberFirstName, memberLastName, memberEmail);


                // Création d'un livre
                Book book = new Book(bookId, bookTitle, numberOfCopies);


                // Ajouter les auteurs
                if (authors != null && !authors.isEmpty()) {
                    for (String authorName : authors.split(", ")) {
                        String[] nameParts = authorName.split(" ");
                        Author author = new Author();
                        if (nameParts.length > 1) {
                            author.setFirst_name(nameParts[0]);
                            author.setLast_name(nameParts[1]);
                        }
                        book.addAuthor(author);
                    }
                }


                // Ajouter les catégories
                if (categories != null && !categories.isEmpty()) {
                    for (String categoryName : categories.split(", ")) {
                        Category category = new Category(categoryName);
                        book.addCategory(category);
                    }
                }

                // Création d'un emprunt
                Loan loan = new Loan(loanId, loanDate, dueDate, returnedDate, member);
                loan.addBook(book);


                // Ajouter le prêt à la liste
                loans.add(loan);


                // Affichage formaté pour la console (ou une autre interface)
                System.out.printf("Emprunt ID: %d, Membre: %s %s, Date d'Emprunt: %s, Date de Retour Prévue: %s, Date de retour réel: %s, Livre: %s%n",
                        loanId, memberFirstName, memberLastName, formattedLoanDate, formattedDueDate, formattedReturnDate, bookTitle);
            }
        } catch (SQLException e) {
            throw new SQLException("❌ Erreur lors de l'affichage des emprunts", e);
        }
    }


    // Vérifie la disponibilité du livre avant l'emprunt
    private boolean isBookAvailable(int bookId) throws SQLException {
        String checkQuery = "SELECT number_of_copies FROM Book WHERE book_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(checkQuery)) {
            pstmt.setInt(1, bookId);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("number_of_copies") > 0;
            }
        }
        return false;
    }

    // Méthode pour mettre à jour le nombre de copies d'un livre
    private void updateBookCopies(int bookId) throws SQLException {
        String updateQuery = "UPDATE Book SET number_of_copies = number_of_copies - 1 WHERE book_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
            pstmt.setInt(1, bookId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("❌ Impossible de mettre à jour le nombre de copies du livre avec l'ID " + bookId);
            } else {
                System.out.println("📌 Nombre de copies du livre " + bookId + " mis à jour !");
            }
        }
    }


    private void incrementBookCopies(int bookId) throws SQLException {
        String updateQuery = "UPDATE Book SET number_of_copies = number_of_copies + 1 WHERE book_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
            pstmt.setInt(1, bookId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("❌ Impossible de mettre à jour le nombre de copies pour le livre avec l'ID " + bookId);
            } else {
                System.out.println("📌 Nombre de copies du livre " + bookId + " incrémenté !");
            }
        }
    }


}

