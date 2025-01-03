package com.library.system.repository;

import com.library.system.exception.member.MemberRegistrationException;
import com.library.system.model.Member;

public interface MemberRepository {
    void registerMember(Member member) throws MemberRegistrationException;
}
