package com.library.system.util;


import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;


public class DatabaseTableCreator {


    public static void createTables(Connection connection) {
        try (Statement statement = connection.createStatement()) {


            // Création de la table Category
            String createCategoryTable = "CREATE TABLE IF NOT EXISTS Category (" +
                    "category_id SERIAL PRIMARY KEY, " +
                    "category_name VARCHAR(100) NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ");";
            statement.executeUpdate(createCategoryTable);


            // Supprimer la contrainte existante (si elle existe)
            String dropCategoryConstraint = "ALTER TABLE Category DROP CONSTRAINT IF EXISTS unique_category_name;";
            statement.executeUpdate(dropCategoryConstraint);


            // Ajouter la contrainte d'unicité pour category_name
            String addCategoryUniqueConstraint = "ALTER TABLE Category " +
                    "ADD CONSTRAINT unique_category_name UNIQUE (category_name);";
            statement.executeUpdate(addCategoryUniqueConstraint);


            // Création de la table Author
            String createAuthorTable = "CREATE TABLE IF NOT EXISTS Author (" +
                    "author_id SERIAL PRIMARY KEY, " +
                    "first_name VARCHAR(50) NOT NULL, " +
                    "last_name VARCHAR(50) NOT NULL, " +
                    "author_email VARCHAR(100) NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ");";
            statement.executeUpdate(createAuthorTable);


            // Supprimer la contrainte existante (si elle existe)
            String dropAuthorEmailConstraint = "ALTER TABLE Author DROP CONSTRAINT IF EXISTS unique_author_email;";
            statement.executeUpdate(dropAuthorEmailConstraint);


            // Ajouter la contrainte d'unicité pour author_email
            String addAuthorUniqueConstraint = "ALTER TABLE Author " +
                    "ADD CONSTRAINT unique_author_email UNIQUE (author_email);";
            statement.executeUpdate(addAuthorUniqueConstraint);


            // Création de la table Book
            String createBookTable = "CREATE TABLE IF NOT EXISTS Book (" +
                    "book_id SERIAL PRIMARY KEY, " +
                    "title VARCHAR(200) NOT NULL, " +
                    "number_of_copies INT NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ");";
            statement.executeUpdate(createBookTable);


            // Création de la table Member
            String createMemberTable = "CREATE TABLE IF NOT EXISTS Member (" +
                    "member_id SERIAL PRIMARY KEY, " +
                    "first_name VARCHAR(50) NOT NULL, " +
                    "last_name VARCHAR(50) NOT NULL, " +
                    "email VARCHAR(100) NOT NULL , " +
                    "adhesion_date TIMESTAMP WITH TIME ZONE NOT NULL, " +
                    "created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP " +
                    ");";
            statement.executeUpdate(createMemberTable);


            // Création de la table Loan
            String createLoanTable = "CREATE TABLE IF NOT EXISTS Loan (" +
                    "loan_id SERIAL PRIMARY KEY, " +
                    "member_id INT NOT NULL, " +  // Clé étrangère vers Member
                    "loanDate TIMESTAMP WITH TIME ZONE NOT NULL, " +
                    "dueDate TIMESTAMP WITH TIME ZONE NOT NULL, " +
                    "returnDate TIMESTAMP WITH TIME ZONE, " +
                    "created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (member_id) REFERENCES Member(member_id) ON DELETE CASCADE" +
                    ");";
            statement.executeUpdate(createLoanTable);


            // Ajout de la colonne penalty
            // Modifier la colonne penalty (si elle existe déjà) pour la mettre à jour ou garantir le bon état
            String alterTableQuery = "ALTER TABLE Loan " +
                    "ADD COLUMN IF NOT EXISTS penalty INT DEFAULT 0;";
            statement.executeUpdate(alterTableQuery);
            //System.out.println("Colonne 'penalty' ajoutée ou déjà existante dans la table Loan.");


            // Création de la table de jointure Book_Loan
            String createBookLoanTable = "CREATE TABLE IF NOT EXISTS Book_Loan (" +
                    "book_id INT NOT NULL, " +
                    "loan_id INT NOT NULL, " +
                    "created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, " +
                    "PRIMARY KEY (book_id, loan_id), " +
                    "FOREIGN KEY (book_id) REFERENCES Book(book_id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (loan_id) REFERENCES Loan(loan_id) ON DELETE CASCADE" +
                    ");";
            statement.executeUpdate(createBookLoanTable);


            // Création de la table Loan_Penalties
            String createLoanPenaltiesTable = "CREATE TABLE IF NOT EXISTS Loan_Penalties (" +
                    "penalty_id SERIAL PRIMARY KEY, " +
                    "loan_id INT NOT NULL, " +
                    "penalty_amount INT NOT NULL, " +
                    "penalty_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (loan_id) REFERENCES Loan(loan_id) ON DELETE CASCADE" +
                    ");";
            statement.executeUpdate(createLoanPenaltiesTable);


            // Création de la table de jointure Book_Author
            String createBookAuthorTable = "CREATE TABLE IF NOT EXISTS Book_Author (" +
                    "book_id INT NOT NULL, " +
                    "author_id INT NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "PRIMARY KEY (book_id, author_id), " +
                    "FOREIGN KEY (book_id) REFERENCES Book(book_id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (author_id) REFERENCES Author(author_id) ON DELETE CASCADE" +
                    ");";
            statement.executeUpdate(createBookAuthorTable);


            // Création de la table de jointure Books_Category
            String createBooksCategoryTable = "CREATE TABLE IF NOT EXISTS Books_Category (" +
                    "book_id INT NOT NULL, " +
                    "category_id INT NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "PRIMARY KEY (book_id, category_id), " +
                    "FOREIGN KEY (book_id) REFERENCES Book(book_id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (category_id) REFERENCES Category(category_id) ON DELETE CASCADE" +
                    ");";
            statement.executeUpdate(createBooksCategoryTable);


            //System.out.println("Tables créées avec succès.");
            Logger.logSuccess("Tables créées avec succès.");


        } catch (SQLException e) {
            e.printStackTrace();


        }
    }
}



