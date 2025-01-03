package com.library.system.dao.impl;

import com.library.system.dao.MemberDAO;
import com.library.system.exception.member.MemberRegistrationException;
import com.library.system.model.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MemberDAOImpl implements MemberDAO {

    private final Connection connection;

    // Constructor to inject the database connection
    public MemberDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void registerMember(Member member) throws MemberRegistrationException {
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
            throw new MemberRegistrationException("Erreur lors de l'enregistrement du membre : " + e.getMessage(), e);
        }
    }
}
