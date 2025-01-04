package com.library.system.dao.impl;

import com.library.system.dao.MemberDAO;
import com.library.system.exception.memberException.*;
import com.library.system.model.Member;

import java.sql.*;
import java.text.SimpleDateFormat;
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
        String query = "SELECT 1 FROM member WHERE email = ? LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();  // Si un résultat est trouvé, l'email existe déjà
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la vérification de l'email : " + e.getMessage());
        }
        return false;  // En cas d'erreur SQL, on suppose que l'email n'est pas pris
    }

    @Override
    public void registerMember(Member member) throws MemberRegistrationException {
        // Vérifier si l'email est déjà pris
        if (isEmailTaken(member.getEmail())) {
            throw new MemberRegistrationException("L'email " + member.getEmail() + " est déjà utilisé.");
        }

        // Création du Timestamp à partir de la date actuelle
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        member.setAdhesionDate(timestamp);  // Met à jour la date d'adhésion du membre avec le Timestamp

        String query = "INSERT INTO member (first_name, last_name, email, adhesion_date) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, member.getFirstName());
            stmt.setString(2, member.getLastName());
            stmt.setString(3, member.getEmail());

            // Si member.getAdhesionDate() est de type Date, on doit le convertir en Timestamp
            stmt.setTimestamp(4, new Timestamp(member.getAdhesionDate().getTime()));  // Conversion en Timestamp

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted == 0) {
                throw new MemberRegistrationException("Échec de l'enregistrement du membre, aucune ligne insérée.");
            }
        } catch (SQLException e) {
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
    public List<Member> getLoanHistory() throws MemberLoanHistoryException {
        List<Member> loanHistory = new ArrayList<>();
        String query = "SELECT DISTINCT m.member_id, m.first_name, m.last_name, m.email, m.adhesion_date " +
                "FROM member m " +
                "JOIN loan l ON m.member_id = l.member_id " +
                "ORDER BY l.loan_date DESC"; // Tri par date d'emprunt

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet resultSet = stmt.executeQuery()) {

            while (resultSet.next()) {
                Member member = new Member(
                        resultSet.getInt("member_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getTimestamp("adhesion_date") // Assure-toi que le type de date est bien géré
                );
                loanHistory.add(member);
            }
        } catch (SQLException e) {
            throw new MemberLoanHistoryException("Erreur lors de la récupération de l'historique des emprunts : " + e.getMessage(), e);
        }

        return loanHistory;
    }


}
