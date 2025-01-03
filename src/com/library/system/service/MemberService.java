package com.library.system.service;

import com.library.system.exception.member.MemberRegistrationException;
import com.library.system.model.Member;

public interface MemberService {
    void registerMember(Member member) throws MemberRegistrationException;
}
