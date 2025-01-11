package com.library.system.repository.impl;

import com.library.system.dao.MemberDAO;
import com.library.system.exception.memberException.*;
import com.library.system.model.Loan;
import com.library.system.model.Member;
import com.library.system.repository.MemberRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
    public List<Member> findMemberByName(String memberName) throws FindMemberByNameException {
        try {
            return memberDAO.findMemberByName(memberName);  // Retourner la liste des membres trouvés par le DAO
        } catch (Exception e) {
            throw new FindMemberByNameException("Erreur lors de la recherche du membre");
        }
    }

    @Override
    public Member findMemberById(int memberID) throws FindMemberByIdException {
        return memberDAO.findMemberById(memberID);
    }

    @Override
    public List<Loan> getLoanHistory(int memberId) throws MemberLoanHistoryException {
        try{
            return memberDAO.getLoanHistory(memberId);
        }catch (Exception e){
            throw new MemberLoanHistoryException(e.getMessage());
        }
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
