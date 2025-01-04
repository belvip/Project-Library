package com.library.system.repository;

import com.library.system.exception.memberException.*;
import com.library.system.model.Member;

import java.util.List;

public interface MemberRepository {
    void registerMember(Member member) throws MemberRegistrationException;

    boolean isEmailTaken(String email);

    void deleteMember(int memberID) throws MemberDeleteException;

    List<Member> findMemberByName(String memberName) throws FindMemberByNameException;

    Member findMemberById(int memberID) throws FindMemberByIdException;

    List<Member> getLoanHistory() throws MemberLoanHistoryException;
}
