package com.library.system.util;

/*public class MemberDAOTest {
    public static void main(String[] args) {
        try {
            // Charger explicitement le pilote PostgreSQL
            Class.forName("org.postgresql.Driver");

            // Connexion à la base de données
            try (Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/library_db", "postgres", "belvi")) {

                // Créer l'instance de MemberDAO
                MemberDAO memberDAO = new MemberDAOImpl(connection);

                // Créer l'instance de MemberRepository
                MemberRepository memberRepository = new MemberRepositoryImpl(memberDAO, connection);

                // Création d'un nouveau membre
                Member newMember = new Member(2,"Belvi", "Pachinco", "belvi.dupont@example.com",
                        new Date(System.currentTimeMillis()));

                // Enregistrement du membre
                memberRepository.registerMember(newMember);
                System.out.println("Membre enregistré avec succès !");
            }
        } catch (MemberRegistrationException e) {
            System.err.println("Erreur : " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : Pilote PostgreSQL introuvable.");
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
        }
    }
}*/
