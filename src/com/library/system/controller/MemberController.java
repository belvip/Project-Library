package com.library.system.controller;

import com.library.system.exception.memberException.FindMemberByIdException;
import com.library.system.exception.memberException.FindMemberByNameException;
import com.library.system.exception.memberException.MemberDeleteException;
import com.library.system.exception.memberException.MemberRegistrationException;
import com.library.system.model.Member;
import com.library.system.service.MemberService;

import java.util.List;

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

    // Méthode pour supprimer un membre
    public void deleteMember(int memberID) throws MemberDeleteException {
        try {
            // Appel au service pour supprimer le membre
            memberService.deleteMember(memberID);
            System.out.println("✅ Le membre a été supprimé avec succès.");
        } catch (MemberDeleteException e) {
            // Gestion de l'exception et affichage de l'erreur
            System.out.println("❌ Erreur : " + e.getMessage());
        }
    }

    // Méthode pour rechercher un membre par son nom
    public List<Member> findMemberByName(String memberName) throws FindMemberByNameException {
        return memberService.findMemberByName(memberName);  // Appel à la méthode du service qui retourne la liste des membres
    }

    public Member findMemberById(int memberID) throws FindMemberByIdException {
        return memberService.findMemberById(memberID);
    }
}
