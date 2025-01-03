package com.library.system.dao.impl;

import com.library.system.dao.MemberDAO;
import com.library.system.exception.memberException.FindMemberByNameException;
import com.library.system.exception.memberException.MemberDeleteException;
import com.library.system.exception.memberException.MemberRegistrationException;
import com.library.system.model.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        if (isEmailTaken(member.getEmail())) {
            throw new MemberRegistrationException("L'email " + member.getEmail() + " est déjà utilisé.");
        }

        String query = "INSERT INTO member (first_name, last_name, email, adhesion_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, member.getFirstName());
            stmt.setString(2, member.getLastName());
            stmt.setString(3, member.getEmail());
            stmt.setDate(4, member.getSqlAdhesionDate());

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
        String query = "SELECT * FROM member WHERE first_name LIKE ? OR last_name LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            String searchPattern = "%" + memberName + "%"; // Utilisation de % pour la recherche partielle
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Création d'un objet Member à partir du ResultSet
                    Member member = new Member(
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getDate("adhesion_date")
                    );
                    members.add(member);
                }
            }
        } catch (SQLException e) {
            throw new FindMemberByNameException("Erreur lors de la recherche des membres par nom : " + e.getMessage());
        }
        return members;
    }


}
