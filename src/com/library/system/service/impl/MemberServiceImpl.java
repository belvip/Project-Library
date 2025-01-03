package com.library.system.service.impl;

import com.library.system.exception.member.MemberRegistrationException;
import com.library.system.model.Member;
import com.library.system.repository.MemberRepository;
import com.library.system.service.MemberService;

public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    // Constructeur pour injecter le repository
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void registerMember(Member member) throws MemberRegistrationException {
        // Validation des données avant l'enregistrement
        validateMemberData(member);

        // Vérification si l'email existe déjà
        if (memberRepository.isEmailTaken(member.getEmail())) {
            throw new MemberRegistrationException("L'email " + member.getEmail() + " est déjà utilisé.");
        }

        // Enregistrement du membre
        memberRepository.registerMember(member);
    }

    // Méthode de validation des données du membre
    private void validateMemberData(Member member) throws MemberRegistrationException {
        if (member.getFirstName() == null || member.getFirstName().trim().isEmpty()) {
            throw new MemberRegistrationException("Le prénom du membre est obligatoire.");
        }

        if (member.getLastName() == null || member.getLastName().trim().isEmpty()) {
            throw new MemberRegistrationException("Le nom du membre est obligatoire.");
        }

        if (member.getEmail() == null || member.getEmail().trim().isEmpty() ||
                !member.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new MemberRegistrationException("L'email du membre est invalide.");
        }
    }

    // Ajout de la méthode addMember
    public void addMember(Member member) throws MemberRegistrationException {
        // Appel de la méthode registerMember pour enregistrer un membre
        registerMember(member);
    }


}
