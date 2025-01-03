package com.library.system.repository;

import com.library.system.exception.memberException.MemberDeleteException;
import com.library.system.exception.memberException.MemberRegistrationException;
import com.library.system.model.Member;

public interface MemberRepository {
    void registerMember(Member member) throws MemberRegistrationException;

    boolean isEmailTaken(String email);

    void deleteMember(int memberID) throws MemberDeleteException;
}
