package com.library.system.repository.impl;

import com.library.system.dao.MemberDAO;
import com.library.system.exception.memberException.MemberDeleteException;
import com.library.system.exception.memberException.MemberRegistrationException;
import com.library.system.model.Member;
import com.library.system.repository.MemberRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class MemberRepositoryImpl implements MemberRepository {

    private final Connection connection;
    private final MemberDAO memberDAO;

    // Constructeur
    public MemberRepositoryImpl(Connection connection, MemberDAO memberDAO) {
        this.connection = connection;
        this.memberDAO = memberDAO;
    }

    @Override
    public boolean isEmailTaken(String email) {
        return memberDAO.isEmailTaken(email); // Délégation au DAO
    }

    @Override
    public void deleteMember(int memberID) throws MemberDeleteException {
        memberDAO.deleteMember(memberID);
    }

    @Override
    public void registerMember(Member member) throws MemberRegistrationException {
        // Vérification si l'email existe déjà via le DAO
        if (isEmailTaken(member.getEmail())) {
            throw new MemberRegistrationException("L'email " + member.getEmail() + " est déjà utilisé.");
        }

        // Délégation de l'insertion au DAO
        memberDAO.registerMember(member);
    }
}
