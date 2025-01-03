package com.library.system.dao.impl;

import com.library.system.dao.MemberDAO;
import com.library.system.exception.member.MemberRegistrationException;
import com.library.system.model.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
