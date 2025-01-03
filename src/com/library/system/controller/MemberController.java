package com.library.system.controller;

import com.library.system.exception.member.MemberRegistrationException;
import com.library.system.model.Member;
import com.library.system.service.MemberService;

public class MemberController {

    private final MemberService memberService;

    // Injection de dépendance : on passe un service au lieu de le créer en dur
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    public void registerMember(Member member) {
        try {
            memberService.registerMember(member);
            System.out.println("✅ Membre ajouté avec succès : " + member.getEmail());
        } catch (MemberRegistrationException e) {
            System.out.println("❌ Erreur lors de l'ajout du membre : " + e.getMessage());
        }
    }
}
