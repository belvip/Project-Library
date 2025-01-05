package com.library.system.controller;

import com.library.system.exception.bookDaoException.BookDisplayException;
import com.library.system.model.Book;
import com.library.system.model.Member;
import com.library.system.service.BookService;
import com.library.system.service.LoanService;
import com.library.system.service.MemberService;

import java.util.List;

public class LoanController {

    private LoanService loanService;
    private final BookService bookService;
    private final MemberService memberService;

    public LoanController(LoanService loanService, BookService bookService, MemberService memberService) {
        this.loanService = loanService;
        this.bookService = bookService;
        this.memberService = memberService;
    }

    public void registerLoan(Member member, List<Book> books) {
        try {
            loanService.registerLoan(member, books);
            System.out.println("✅ Emprunt enregistré avec succès !");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'enregistrement du prêt : " + e.getMessage());
        }
    }

    public Book getBookById(int bookId) {
        try {
            return bookService.getBookById(bookId);  // Appel au service pour récupérer un livre par son ID
        } catch (BookDisplayException e) {
            System.err.println("Erreur lors de la récupération du livre : " + e.getMessage());
            return null;  // Ou retourner une valeur par défaut ou traiter l'erreur autrement
        }
    }

    public Member getMemberById(int memberId) {
        return memberService.findMemberById(memberId); // Utiliser le service pour obtenir le membre
    }
}
