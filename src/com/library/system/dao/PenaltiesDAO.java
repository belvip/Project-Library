package com.library.system.dao;


import com.library.system.model.Loan;


import java.sql.SQLException;
import java.util.List;


public interface PenaltiesDAO {


    int calculatePenalty(Loan loan);


    void updatePenaltyInDatabase(Loan loan);

    int getPenaltyRate();


    List<Loan> getLoansWithDelays() throws SQLException;  // Ajout de SQLException ici
}

