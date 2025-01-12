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

---

## 6. Démarrage du Projet

### Prérequis
- Installer PostgreSQL
- Cloner le projet à partir de GitHub

### Instructions

1. **Cloner le dépôt :**
   ```bash
   git clone https://github.com/belvip/Project-Library.git
