package com.library.system.util;


import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Classe utilitaire pour gérer les logs avec horodatage, stockage en fichier et couleurs en console.
 */
public class Logger {


    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String LOG_FILE_PATH = System.getProperty("user.home") + "/library_logs.log"; // Stockage dans le répertoire utilisateur
    private static final long MAX_FILE_SIZE = 5_000_000; // 5 Mo


    // Couleurs ANSI pour la console
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";   // Erreur
    private static final String GREEN = "\u001B[32m"; // Succès
    private static final String YELLOW = "\u001B[33m"; // Avertissement
    private static final String BLUE = "\u001B[34m";  // Info
    private static final String PURPLE = "\u001B[35m"; // Debug


    /**
     * Obtient le timestamp formaté pour les logs.
     */
    private static String getTimeStamp() {
        return "[" + LocalDateTime.now().format(FORMATTER) + "]";
    }


    /**
     * Écrit un message dans le fichier de log.
     */
    private static void writeToFile(String message) {
        try {
            rotateLogFile(); // Vérifier la taille avant d'écrire
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
                writer.write(message);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println(RED + getTimeStamp() + " ❌ Error writing to log file: " + e.getMessage() + RESET);
        }
    }


    /**
     * Vérifie et gère la rotation des fichiers de log.
     */
    private static void rotateLogFile() {
        File logFile = new File(LOG_FILE_PATH);
        if (logFile.exists() && logFile.length() > MAX_FILE_SIZE) {
            File backupFile = new File(LOG_FILE_PATH.replace(".log", "_" + System.currentTimeMillis() + ".log"));
            if (!logFile.renameTo(backupFile)) {
                System.err.println(RED + getTimeStamp() + " ❌ Error renaming log file for rotation" + RESET);
            }
        }
    }


    /**
     * Enregistre un message générique avec un niveau de log spécifié.
     */
    public static void logMessage(String level, String action, String message) {
        String formattedMessage;
        switch (level.toUpperCase()) {
            case "ERROR":
                formattedMessage = RED + getTimeStamp() + " ❌ ERROR " + action + " : " + message + RESET;
                break;
            case "SUCCESS":
                formattedMessage = GREEN + getTimeStamp() + " ✅ " + action + " effectué avec succès!" + RESET;
                break;
            case "INFO":
                formattedMessage = BLUE + getTimeStamp() + " ℹ️ " + message + RESET;
                break;
            case "WARNING":
                formattedMessage = YELLOW + getTimeStamp() + " ⚠️ WARNING " + action + " : " + message + RESET;
                break;
            case "DEBUG":
                formattedMessage = PURPLE + getTimeStamp() + " 🐞 DEBUG: " + message + RESET;
                break;
            default:
                formattedMessage = BLUE + getTimeStamp() + " ℹ️ " + message + RESET;
        }
        System.out.println(formattedMessage);
        writeToFile(formattedMessage);
    }


    /**
     * Enregistre un message d'erreur avec une action spécifique et une exception.
     */
    public static void logError(String action, Exception e) {
        String message = RED + getTimeStamp() + " ❌ ERROR during " + action + " : " + e.getMessage() + RESET;
        System.err.println(message);
        e.printStackTrace(); // Afficher la pile d'exécution
        writeToFile(message);
        // Écrire la stack trace dans le fichier de log
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
            e.printStackTrace(writer);
        } catch (IOException ex) {
            System.err.println(RED + getTimeStamp() + " ❌ Error writing stack trace to log file: " + ex.getMessage() + RESET);
        }
    }


    /**
     * Enregistre un message d'erreur avec un simple message et une exception.
     */
    public static void logErrorWithMessage(String message, Exception e) {
        String timestamp = getTimeStamp();
        String formattedMessage = RED + timestamp + " ❌ ERROR: " + message + RESET;


        System.err.println(formattedMessage);
        if (e != null) {
            e.printStackTrace();
        }
        writeToFile(formattedMessage);


        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
            if (e != null) {
                e.printStackTrace(writer);
            }
        } catch (IOException ioException) {
            System.err.println(RED + getTimeStamp() + " ❌ Error writing stack trace to log file: " + ioException.getMessage() + RESET);
        }
    }


    /**
     * Enregistre un message de succès.
     */
    public static void logSuccess(String action) {
        logMessage("SUCCESS", action, action + " successfully completed!");
    }


    /**
     * Enregistre un message d'information.
     */
    public static void logInfo(String message) {
        logMessage("INFO", "", message);
    }


    /**
     * Enregistre un message d'avertissement.
     */
    public static void logWarn(String action, String warningMessage) {
        logMessage("WARNING", action, warningMessage);
    }


    /**
     * Enregistre un message de débogage.
     */
    public static void logDebug(String message) {
        logMessage("DEBUG", "", message);
    }


    /**
     * Enregistre une erreur avec un simple message.
     */
    public static void logError(String message) {
        logErrorWithMessage(message, null);
    }
}


