package com.library.system.controller;

import com.library.system.exception.bookDaoException.BookDisplayException;
import com.library.system.model.Book;
import com.library.system.model.Member;
import com.library.system.service.impl.BookServiceImpl;
import com.library.system.service.impl.LoanServiceImpl;
import com.library.system.exception.loanException.RegisterLoanException;
import com.library.system.service.impl.MemberServiceImpl;

import java.util.List;

public class LoanController {

    private final LoanServiceImpl loanService;  // Dépendance vers le service LoanServiceImpl
    private final MemberServiceImpl memberService;
    private final BookServiceImpl bookService;

    // Constructeur pour l'injection de LoanServiceImpl
    public LoanController(LoanServiceImpl loanService, MemberServiceImpl memberService, BookServiceImpl bookService) {
        this.loanService = loanService;
        this.memberService = memberService;
        this.bookService = bookService;
    }

    // Méthode pour enregistrer un prêt
    public void registerLoan(Member member, List<Book> books) {
        try {
            // Appel à la méthode registerLoan du service LoanServiceImpl
            loanService.registerLoan(member, books);
            System.out.println("Prêt enregistré avec succès.");
        } catch (RegisterLoanException e) {
            // Gestion des exceptions si un problème survient lors de l'enregistrement du prêt
            System.err.println("Erreur lors de l'enregistrement du prêt : " + e.getMessage());
        }
    }

    public Member getMemberById(int memberId) {
        return memberService.findMemberById(memberId); // Utiliser le service pour obtenir le membre
    }


    public Book getBookById(int bookId) {
        try {
            return bookService.getBookById(bookId);  // Appel au service pour récupérer un livre par son ID
        } catch (BookDisplayException e) {
            System.err.println("Erreur lors de la récupération du livre : " + e.getMessage());
            return null;  // Ou retourner une valeur par défaut ou traiter l'erreur autrement
        }
    }
}
