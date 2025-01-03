package com.library.system.service;

import com.library.system.exception.memberException.MemberDeleteException;
import com.library.system.exception.memberException.MemberRegistrationException;
import com.library.system.model.Member;

public interface MemberService {
    void registerMember(Member member) throws MemberRegistrationException;

    void deleteMember(int memberID) throws MemberDeleteException;
}
