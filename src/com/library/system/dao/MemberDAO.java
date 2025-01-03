package com.library.system.dao;

import com.library.system.exception.member.MemberRegistrationException;
import com.library.system.model.Member;

public interface MemberDAO {
    /**
     * Enregistre un nouveau membre dans la base de données.
     *
     * @param member Le membre à enregistrer.
     * @throws MemberRegistrationException Si l'enregistrement échoue.
     */
    void registerMember(Member member) throws MemberRegistrationException;
}
