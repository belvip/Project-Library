package com.library.system.service;

import com.library.system.exception.memberException.FindMemberByIdException;
import com.library.system.exception.memberException.FindMemberByNameException;
import com.library.system.exception.memberException.MemberDeleteException;
import com.library.system.exception.memberException.MemberRegistrationException;
import com.library.system.model.Member;

import java.util.List;

public interface MemberService {
    void registerMember(Member member) throws MemberRegistrationException;

    void deleteMember(int memberID) throws MemberDeleteException;

    List<Member> findMemberByName(String memberName) throws FindMemberByNameException;

    Member findMemberById(int memberID) throws FindMemberByIdException;
}
