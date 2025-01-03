package com.library.system.controller;

import com.library.system.dao.impl.MemberDAOImpl;
import com.library.system.model.Member;
import com.library.system.repository.impl.MemberRepositoryImpl;
import com.library.system.service.MemberService;
import com.library.system.service.impl.MemberServiceImpl;

import java.sql.Connection;

public class MemberController {

    private MemberService memberService;

    public MemberController(Connection connection) {
        // Création de MemberDAOImpl pour le passer à MemberRepositoryImpl
        MemberDAOImpl memberDAO = new MemberDAOImpl(connection);
        this.memberService = new MemberServiceImpl(new MemberRepositoryImpl(connection, memberDAO));
    }

    public void registerMember(Member member) {
        try {
            memberService.registerMember(member);
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout du membre : " + e.getMessage());
        }
    }
}
