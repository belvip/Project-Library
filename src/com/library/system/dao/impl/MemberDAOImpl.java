package com.library.system.dao.impl;

import com.library.system.dao.MemberDAO;
import com.library.system.exception.memberException.*;
import com.library.system.model.Loan;
import com.library.system.model.Member;
import com.library.system.util.Logger;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class MemberDAOImpl implements MemberDAO {

    private final Connection connection;

    public MemberDAOImpl(Connection connection) {
        this.connection = connection;
    }

    // Méthode pour vérifier si l'email est déjà pris

    @Override
    public boolean isEmailTaken(String email) {
        String query = "SELECT 1 FROM member WHERE email = ? LIMIT 1"; // Vérifie l'existence de l'email
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email.trim().toLowerCase()); // Normalise l'e-mail en minuscule
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();  // Retourne `true` si un résultat est trouvé
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la vérification de l'e-mail : " + e.getMessage());
            e.printStackTrace();
        }
        return false;  // En cas d'erreur SQL, considère que l'e-mail n'est pas pris
    }





    @Override
    public void registerMember(Member member) throws MemberRegistrationException {
        // Normalise l'e-mail pour éviter les doublons (e-mails insensibles à la casse)
        String email = member.getEmail().trim().toLowerCase();
        member.setEmail(email);


        // Vérifie si l'e-mail est déjà utilisé
        if (isEmailTaken(email)) {
            throw new MemberRegistrationException("L'e-mail " + email + " est déjà utilisé.");
        }


        // Ajoute un timestamp pour la date d'adhésion
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        member.setAdhesionDate(timestamp);


        String query = "INSERT INTO member (first_name, last_name, email, adhesion_date) VALUES (?, ?, ?, ?)";


        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, member.getFirstName());
            stmt.setString(2, member.getLastName());
            stmt.setString(3, email);
            stmt.setTimestamp(4, timestamp);


            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted == 0) {
                throw new MemberRegistrationException("Échec de l'enregistrement du membre, aucune ligne insérée.");
            }


            // Affiche un message de succès
            System.out.println("✅ Membre enregistré avec succès !");
        } catch (SQLException e) {
            // Gère une violation de contrainte unique si elle est définie en base
            if ("23505".equals(e.getSQLState())) { // Code SQL pour contrainte unique (PostgreSQL)
                throw new MemberRegistrationException("L'e-mail " + email + " est déjà utilisé.", e);
            }
            throw new MemberRegistrationException("Erreur SQL lors de l'enregistrement du membre : " + e.getMessage(), e);
        }
    }


    @Override
    public void deleteMember(int memberID) throws MemberDeleteException {
        String query = "DELETE FROM member WHERE member_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, memberID);  // On définit le memberID pour la requête SQL

            int rowsAffected = stmt.executeUpdate();  // Exécution de la requête de suppression
            if (rowsAffected == 0) {
                throw new MemberDeleteException("Aucun membre trouvé avec l'ID : " + memberID);
            }
            System.out.println("Membre avec ID " + memberID + " supprimé avec succès.");
        } catch (SQLException e) {
            // En cas d'erreur SQL, on lance l'exception personnalisée
            throw new MemberDeleteException("Erreur lors de la suppression du membre : " + e.getMessage());
        }
    }

    // Implémentation de la méthode findMemberByName
    @Override
    public List<Member> findMemberByName(String memberName) throws FindMemberByNameException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT member_id, first_name, last_name, email, adhesion_date FROM member WHERE first_name LIKE ? OR last_name LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + memberName + "%");
            preparedStatement.setString(2, "%" + memberName + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Utilisation de getTimestamp pour récupérer la date et l'heure
                Timestamp adhesionDate = resultSet.getTimestamp("adhesion_date");

                // Utiliser le constructeur avec les arguments nécessaires
                Member member = new Member(
                        resultSet.getInt("member_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        adhesionDate // Passer le Timestamp directement
                );

                members.add(member);
            }
        } catch (SQLException e) {
            throw new FindMemberByNameException("Erreur lors de la récupération des membres par nom");
        }

        return members;
    }

    @Override
    public Member findMemberById(int memberID) throws FindMemberByIdException {
        String sql = "SELECT member_id, first_name, last_name, email, adhesion_date FROM member WHERE member_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, memberID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Member(
                        resultSet.getInt("member_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getTimestamp("adhesion_date") // Prend en compte l'heure
                );
            } else {
                throw new FindMemberByIdException("Aucun membre trouvé avec l'ID : " + memberID);
            }
        } catch (SQLException e) {
            throw new FindMemberByIdException("Erreur SQL lors de la récupération du membre : " + e.getMessage());
        }
    }

    @Override
    public List<Loan> getLoanHistory(int memberId) throws MemberLoanHistoryException {
        List<Loan> loanHistory = new ArrayList<>();
        String query = "SELECT l.loan_id, l.loandate, l.duedate, l.returndate, " +
                "m.member_id, m.first_name, m.last_name, m.email, m.adhesion_date " +
                "FROM loan l " +
                "JOIN member m ON l.member_id = m.member_id " +
                "WHERE m.member_id = ? " +
                "ORDER BY l.loandate DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, memberId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                boolean found = false;
                while (resultSet.next()) {
                    found = true;

                    // Convertir l'adhésion en Date
                    Date adhesionDate = new Date(resultSet.getTimestamp("adhesion_date").getTime());

                    // Création de l'objet Loan avec les données récupérées
                    Loan loan = new Loan(
                            resultSet.getInt("loan_id"),  // Maintenant disponible grâce à la correction SQL
                            resultSet.getTimestamp("loandate").toInstant().atZone(ZoneId.systemDefault()),
                            resultSet.getTimestamp("duedate").toInstant().atZone(ZoneId.systemDefault()),
                            resultSet.getTimestamp("returndate") != null ?
                                    resultSet.getTimestamp("returndate").toInstant().atZone(ZoneId.systemDefault())
                                    : null,
                            new Member(
                                    resultSet.getInt("member_id"),
                                    resultSet.getString("first_name"),
                                    resultSet.getString("last_name"),
                                    resultSet.getString("email"),
                                    adhesionDate
                            )
                    );

                    loanHistory.add(loan);
                }

                if (!found) {
                    throw new MemberLoanHistoryException("Aucun emprunt trouvé pour ce membre.");
                }
            }
        } catch (SQLException e) {
            throw new MemberLoanHistoryException("Erreur lors de la récupération de l'historique des emprunts : " + e.getMessage(), e);
        }

        return loanHistory;
    }






}
