package com.library.system.model;


import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;


public class Loan {
    private int loanId;
    private ZonedDateTime loanDate;  // Date d'emprunt avec fuseau horaire
    private ZonedDateTime dueDate;   // Date limite de retour avec fuseau horaire
    private ZonedDateTime returnedDate;  // Date de retour réelle (si applicable)
    private Member member;  // Relation avec un membre


    // Relations : un prêt peut contenir plusieurs livres
    private Set<Book> books = new HashSet<>();


    // Format de date pour la représentation sous forme de chaîne
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    // Constructeur
    public Loan(int loanId, ZonedDateTime loanDate, ZonedDateTime dueDate, ZonedDateTime returnedDate, Member member) {
        this.loanId = loanId;
        this.loanDate = loanDate;
        this.setDueDate(dueDate);  // Validation intégrée
        this.setReturnedDate(returnedDate); // Validation intégrée
        this.member = member;
    }


    // Getters et Setters
    public int getLoanId() {
        return loanId;
    }


    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }


    public ZonedDateTime getLoanDate() {
        return loanDate;
    }


    public void setLoanDate(ZonedDateTime loanDate) {
        this.loanDate = loanDate;
    }


    public String getFormattedLoanDate() {
        return loanDate.format(DATE_FORMATTER);
    }


    public ZonedDateTime getDueDate() {
        return dueDate;
    }


    public void setDueDate(ZonedDateTime dueDate) {
        if (dueDate.isBefore(loanDate)) {
            throw new IllegalArgumentException("La date de retour prévue ne peut pas être avant la date d'emprunt.");
        }
        this.dueDate = dueDate;
    }


    public String getFormattedDueDate() {
        return dueDate.format(DATE_FORMATTER);
    }


    public ZonedDateTime getReturnedDate() {
        return returnedDate;
    }


    public void setReturnedDate(ZonedDateTime returnedDate) {
        if (returnedDate != null && returnedDate.isBefore(loanDate)) {
            throw new IllegalArgumentException("La date de retour ne peut pas être avant la date d'emprunt.");
        }
        this.returnedDate = returnedDate;
    }


    public String getFormattedReturnedDate() {
        return returnedDate != null ? returnedDate.format(DATE_FORMATTER) : "Pas encore retourné";
    }


    public Member getMember() {
        return member;
    }


    public void setMember(Member member) {
        this.member = member;
    }


    public Set<Book> getBooks() {
        return books;
    }


    public void setBooks(Set<Book> books) {
        this.books = books;
    }


    // Méthode pour ajouter un livre au prêt
    public void addBook(Book book) {
        books.add(book);
    }


    // Vérifier si le prêt est en retard
    public boolean isOverdue() {
        return returnedDate == null && ZonedDateTime.now().isAfter(dueDate);
    }


    @Override
    public String toString() {
        return "Loan{" +
                "loanId=" + loanId +
                ", loanDate=" + getFormattedLoanDate() +
                ", dueDate=" + getFormattedDueDate() +
                ", returnedDate=" + getFormattedReturnedDate() +
                ", member=" + (member != null ? member.getFirstName() + " " + member.getLastName() : "Aucun membre") +
                ", books=" + books +
                '}';
    }
}

