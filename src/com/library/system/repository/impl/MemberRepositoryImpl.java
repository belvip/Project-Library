package com.library.system.repository.impl;

import com.library.system.dao.MemberDAO;
import com.library.system.repository.MemberRepository;
import com.library.system.model.Member;
import com.library.system.exception.member.MemberRegistrationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MemberRepositoryImpl implements MemberRepository {

    private final Connection connection;
    private final MemberDAO memberDAO;

    // Constructeur qui prend uniquement la connexion
    public MemberRepositoryImpl(Connection connection, MemberDAO memberDAO) {
        this.connection = connection;
        this.memberDAO = memberDAO;
    }

    @Override
    public void registerMember(Member member) {
        String query = "INSERT INTO member (first_name, last_name, email, adhesion_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, member.getFirstName());
            stmt.setString(2, member.getLastName());
            stmt.setString(3, member.getEmail());

            // Conversion explicite de java.util.Date à java.sql.Date
            stmt.setDate(4, new java.sql.Date(member.getAdhesionDate().getTime()));

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted == 0) {
                throw new MemberRegistrationException("Échec de l'enregistrement du membre, aucune ligne insérée.");
            }

            memberDAO.registerMember(member);
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'enregistrement du membre : " + e.getMessage());
            throw new MemberRegistrationException("Erreur lors de l'enregistrement du membre.", e);
        }
    }

}
