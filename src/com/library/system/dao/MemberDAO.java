package com.library.system.dao;


import com.library.system.exception.memberException.*;
import com.library.system.model.Loan;
import com.library.system.model.Member;


import java.util.List;


public interface MemberDAO {
    // Méthode pour vérifier si l'email est déjà pris
    boolean isEmailTaken(String email);


    /**
     * Enregistre un nouveau membre dans la base de données.
     *
     * @param member Le membre à enregistrer.
     * @throws MemberRegistrationException Si l'enregistrement échoue.
     */
    void registerMember(Member member) throws MemberRegistrationException;
    void deleteMember(int memberID) throws MemberDeleteException;


    List<Member> findMemberByName(String memberName) throws FindMemberByNameException;


    Member findMemberById(int memberID) throws FindMemberByIdException;


    List<Loan> getLoanHistory(int memberId) throws MemberLoanHistoryException;


}

