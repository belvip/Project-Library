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

        try {
            // Enregistrement du membre dans la base de données
            memberRepository.registerMember(member);
        } catch (MemberRegistrationException e) {
            // Relancer l'exception avec des informations détaillées
            throw new MemberRegistrationException("Erreur lors de l'enregistrement du membre : " + e.getMessage(), e);
        } catch (Exception e) {
            // En cas d'erreur générale, lancer une exception spécifique
            throw new MemberRegistrationException("Erreur imprévue lors de l'enregistrement du membre.", e);
        }
    }

    // Ajout de la méthode addMember
    public void addMember(Member member) throws MemberRegistrationException {
        // Appel de la méthode registerMember pour enregistrer un membre
        registerMember(member);
    }

    // Méthode de validation des données du membre (si nécessaire)
    private void validateMemberData(Member member) throws MemberRegistrationException {
        if (member.getFirstName() == null || member.getFirstName().trim().isEmpty()) {
            throw new MemberRegistrationException("Le prénom du membre est obligatoire.");
        }

        if (member.getLastName() == null || member.getLastName().trim().isEmpty()) {
            throw new MemberRegistrationException("Le nom du membre est obligatoire.");
        }

        if (member.getEmail() == null || member.getEmail().trim().isEmpty()) {
            throw new MemberRegistrationException("L'email du membre est obligatoire.");
        }
    }
}
