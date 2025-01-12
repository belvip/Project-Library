# Système de Gestion de Bibliothèque

---

## Table des matières

1. [Introduction](#1-introduction)
2. [Technologies Utilisées](#2-technologies-utilisées)
3. [Fonctionnalités Principales](#3-fonctionnalités-principales)
4. [Diagrammes UML](#4-diagrammes-uml)
5. [Structure du Projet](#5-structure-du-projet)
6. [Démarrage du Projet](#6-démarrage-du-projet)
7. [Utilisation de l'Application](#7-utilisation-de-lapplication)
8. [Modèle de Données](#8-modèle-de-données)
9. [Contributions](#9-contributions)
10. [Licence](#10-licence)
11. [Contact](#11-contact)

---

## 1. Introduction

Le **Système de Gestion de Bibliothèque** est une application conçue pour faciliter la gestion des livres, auteurs, membres, et prêts dans une bibliothèque. L'application permet d'emprunter et de retourner des livres, de suivre les retards et d'appliquer des pénalités.

Ce projet est principalement destiné à démontrer l'utilisation des concepts de programmation orientée objet et de gestion des données dans une application pratique.

---

## 2. Technologies Utilisées

- **Langage principal** : Java (version 8 ou supérieure)
- **Base de données** : PostgreSQL
- **Diagrammes UML** : Créés avec PlantUML
- **Système de versionnage** : Git/GitHub

---

## 3. Fonctionnalités Principales

### Gestion des livres
- Ajouter, mettre à jour et supprimer des livres.
- Rechercher un livre par ID ou par catégorie.
- Afficher tous les livres disponibles.

### Gestion des membres
- Enregistrer de nouveaux membres.
- Rechercher des membres par nom ou par ID.
- Afficher l'historique des prêts d'un membre.

### Gestion des prêts
- Enregistrer des prêts pour un membre.
- Gérer le retour des livres.
- Consulter tous les prêts en cours.

### Gestion des pénalités
- Calculer les pénalités pour les retards.
- Consulter les prêts en retard.

### Recherche avancée
- Trouver des livres par auteur, catégorie ou mot-clé.

---

## 4. Diagrammes UML

Les diagrammes UML suivants illustrent la structure et les relations du système :

- [Diagramme des packages](https://drive.google.com/file/d/1ppy98LV8LuEiQKki9Qrn4SlcZj7US5dA/view?usp=sharing)
- [Diagramme des classes](https://drive.google.com/file/d/1Vt4PyNjTFUn35WpCoqiiAQYpzAvwJcEK/view?usp=sharing)

---

## 5. Structure du Projet

La structure du projet est organisée comme suit :
- `src/` : Contient les fichiers source Java.
  - `com/library/model/` : Contient les classes représentant les entités principales du système.
    - `Book.java` : Classe représentant un livre.
    - `Member.java` : Classe représentant un membre.
    - `Loan.java` : Classe représentant un prêt.
    - `Author.java` : Classe représentant un auteur.
    - `Category.java` : Classe représentant une catégorie de livre.
 
  - `com/library/controller/` : Contient les classes de contrôleur pour gérer les actions du système.
    - `BookController.java` : Contrôleur pour la gestion des livres.
    - `MemberController.java` : Contrôleur pour la gestion des membres.
    - `LoanController.java` : Contrôleur pour la gestion des prêts.
    - `AuthorController.java` : Contrôleur pour la gestion des auteurs.
    - `PenaltiesController.java` : Contrôleur pour la gestion des pénalités.
    - `CategoryController.java` : Contrôleur pour la gestion des catégories.
 
  - `com/library/service/` : Contient les interfaces des services pour chaque entité.
    - `BookService.java` : Interface pour la gestion des services liés aux livres.
    - `AuthorService.java` : Interface pour la gestion des services liés aux auteurs.
    - `CategoryService.java` : Interface pour la gestion des services liés aux catégories.
    - `LoanService.java` : Interface pour la gestion des services liés aux prêts.
    - `MemberService.java` : Interface pour la gestion des services liés aux membres.
    - `PenaltiesService.java` : Interface pour la gestion des services liés aux pénalités.
 
  - `com/library/service/impl/` : Contient les classes d'implémentation des services.
    - `BookServiceImpl.java` : Implémentation des services liés aux livres.
    - `AuthorServiceImpl.java` : Implémentation des services liés aux auteurs.
    - `CategoryServiceImpl.java` : Implémentation des services liés aux catégories.
    - `LoanServiceImpl.java` : Implémentation des services liés aux prêts.
    - `MemberServiceImpl.java` : Implémentation des services liés aux membres.
    - `PenaltiesServiceImpl.java` : Implémentation des services liés aux pénalités.
 
  - `com/library/system/dao/` : Contient les interfaces pour l'accès aux données des entités.
    - `BookDAO.java` : Interface pour l'accès aux données des livres.
    - `MemberDAO.java` : Interface pour l'accès aux données des membres.
    - `LoanDAO.java` : Interface pour l'accès aux données des prêts.
    - `PenaltiesDAO.java` : Interface pour l'accès aux données des pénalités.
 
  - `com/library/system/dao/impl/` : Contient les implémentations des DAO pour l'accès aux données.
    - `BookDAOImpl.java` : Implémentation du DAO pour les livres.
    - `MemberDAOImpl.java` : Implémentation du DAO pour les membres.
    - `LoanDAOImpl.java` : Implémentation du DAO pour les prêts.
    - `PenaltiesDAOImpl.java` : Implémentation du DAO pour les pénalités.
 
  - `com/library/system/repository/` : Contient les interfaces pour les repositories.
    - `AuthorRepository.java` : Interface pour la gestion des auteurs dans le repository.
    - `BookRepository.java` : Interface pour la gestion des livres dans le repository.
    - `CategoryRepository.java` : Interface pour la gestion des catégories dans le repository.
    - `LoanRepository.java` : Interface pour la gestion des prêts dans le repository.
    - `MemberRepository.java` : Interface pour la gestion des membres dans le repository.
    - `PenaltiesRepository.java` : Interface pour la gestion des pénalités dans le repository.
 
  - `com/library/system/repository/impl/` : Contient les implémentations des repositories.
    - `AuthorRepositoryImpl.java` : Implémentation du repository pour les auteurs.
    - `BookRepositoryImpl.java` : Implémentation du repository pour les livres.
    - `CategoryRepositoryImpl.java` : Implémentation du repository pour les catégories.
    - `LoanRepositoryImpl.java` : Implémentation du repository pour les prêts.
    - `MemberRepositoryImpl.java` : Implémentation du repository pour les membres.
    - `PenaltiesRepositoryImpl.java` : Implémentation du repository pour les pénalités.
 
  - `com/library/utils/` : Contient les classes utilitaires.
    - `ConsoleHandler.java` : Classe pour la gestion de la console.
    - `DatabaseConnection.java` : Classe pour la connexion à la base de données.
    - `DatabaseTableCreator.java` : Classe pour la création des tables de la base de données.
    - `EmailValidator.java` : Classe pour valider les emails.
    - `Logger.java` : Classe pour la gestion des logs.
 
  - `com/library/exception/` : Contient les classes pour la gestion des exceptions.
    - `BookNotFoundException.java` : Exception levée lorsqu'un livre n'est pas trouvé.
    - `MemberNotFoundException.java` : Exception levée lorsqu'un membre n'est pas trouvé.
    - `LoanNotFoundException.java` : Exception levée lorsqu'un prêt n'est pas trouvé.
 
- `resources/` : Dossier contenant les ressources nécessaires au projet (par exemple, fichiers de configuration, SQL, etc.).


    

---

## 6. Démarrage du Projet

### Prérequis
- Installer PostgreSQL
- Cloner le projet à partir de GitHub

### Instructions

1. **Cloner le dépôt :**
   ```bash
   git clone https://github.com/belvip/Project-Library.git

### Configurer la base de données :

1. Créer une base de données PostgreSQL nommée `library_management`.
2. Mettre à jour les paramètres de connexion dans le fichier `DatabaseConnection.java`.
3. Si la base de données bibli n'existe pas, elle sera automatiquement créée lors du lancement de l'application. Le système vérifie la présence de la base de données et crée celle-ci si nécessaire avant de se connecter. Une fois la base de données créée, l'application procède à la création des tables nécessaires pour le bon fonctionnement du système de gestion de la bibliothèque.

### Compiler et exécuter :

```bash
javac -d bin src/com/belvinard/libraryManagementSystem/*.java
java -cp bin com.belvinard.libraryManagementSystem.Main
```

---

## 7. Utilisation de l'Application

- Cette section décrit les différentes commandes que vous pouvez utiliser pour interagir avec l'application via la ligne de commande. Chaque commande correspond à une méthode d'une classe du projet.

### Exemples de commandes

#### Ajouter un livre :
```bash
addBook <title> <author>
```

#### Emprunter un livre :
```bash
borrowBook <bookId> <memberId>
```

#### Retourner un livre :
```bash
returnBook <loanId>
```

## Méthodes principales par classe

### Classe `Book`
- `addBook(Book book)`: Ajouter un livre.
- `findBookById(int bookId)`: Trouver un livre par ID.
- `displayAvailableBooks()`: Afficher les livres disponibles.
- `searchBookByCategory(String categoryName)`: Rechercher par catégorie.

### Classe `Member`
- `registerMember(Member member)`: Ajouter un membre.
- `findMemberById(int memberId)`: Trouver un membre par ID.
- `getLoanHistory()`: Afficher l'historique des prêts.

### Classe `Loan`
- `registerLoan(Member member, List<Book> books)`: Enregistrer un prêt.
- `returnBook(int loanId)`: Retourner un livre.

### Interface `PenaltiesDAO`
- `calculatePenalty(Loan loan)`: Calculer une pénalité.
- `getLoansWithDelays()`: Obtenir les prêts en retard.

## 8. Modèle de Données

Les entités principales sont :

- **Book** : Livres (titre, auteur, copies disponibles).
- **Member** : Membres (nom, email, date d'adhésion).
- **Loan** : Prêts (date de prêt, date d’échéance, pénalités).
- **Category** : Catégories de livres.
- **Author** : Informations sur les auteurs.


---

## 9. Contributions

Si tu souhaites contribuer au projet, voici les étapes à suivre :

1. **Forkez le projet** sur GitHub.
2. **Clonez** votre fork localement :  
   `git clone <url_du_fork>`
3. **Créez une nouvelle branche** pour vos modifications :  
   `git checkout -b feature/nouvelle-fonctionnalité`
4. **Faites vos modifications**, puis ajoutez et validez vos changements :
   - `git add .`
   - `git commit -m "Ajout de la nouvelle fonctionnalité"`
5. **Poussez les modifications** sur votre fork :  
   `git push origin feature/nouvelle-fonctionnalité`
6. **Soumettez une pull request** pour proposer vos modifications.

---

## 10. Licence
- Ce projet est sous licence MIT. Voir le fichier LICENCE pour plus de details

---

## 11. Contact

Si vous avez des questions, des suggestions ou des demandes, vous pouvez me contacter :

- **Email** : `pouadjeubelvi@gmail.com`


