package com.library.system.service;

import com.library.system.exception.memberException.*;
import com.library.system.model.Loan;
import com.library.system.model.Member;

import java.util.List;

public interface MemberService {
    void registerMember(Member member) throws MemberRegistrationException;

    void deleteMember(int memberID) throws MemberDeleteException;

    List<Member> findMemberByName(String memberName) throws FindMemberByNameException;

    Member findMemberById(int memberID) throws FindMemberByIdException;

    List<Loan> getLoanHistory(int memberId) throws MemberLoanHistoryException;


}
