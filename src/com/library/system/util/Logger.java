package com.library.system.util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe utilitaire pour gérer les logs avec horodatage, stockage en fichier et rotation automatique.
 */
public class Logger {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String LOG_FILE_PATH = System.getProperty("user.home") + "/library_logs.log";
    private static final long MAX_FILE_SIZE = 5_000_000; // 5 Mo

    private static boolean silentMode = false; // Mode silencieux

    // Couleurs ANSI
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";

    private static String getTimeStamp() {
        return "[" + LocalDateTime.now().format(FORMATTER) + "]";
    }

    public static void setSilentMode(boolean silent) {
        silentMode = silent;
    }

    private static void writeToFile(String message) {
        try {
            rotateLogFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
                writer.write(message);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println(RED + getTimeStamp() + " ❌ Error writing to log file: " + e.getMessage() + RESET);
        }
    }

    private static void rotateLogFile() {
        File logFile = new File(LOG_FILE_PATH);
        if (logFile.exists() && logFile.length() > MAX_FILE_SIZE) {
            File backupFile = new File(LOG_FILE_PATH.replace(".log", "_" + System.currentTimeMillis() + ".log"));
            if (logFile.renameTo(backupFile)) {
                if (!silentMode) {
                    System.out.println(YELLOW + getTimeStamp() + " 🔄 Log file rotated: " + backupFile.getName() + RESET);
                }
            } else {
                System.err.println(RED + getTimeStamp() + " ❌ Error renaming log file for rotation" + RESET);
            }
        }
    }

    public static void logError(String action, Exception e) {
        String message = RED + getTimeStamp() + " ❌ ERROR during " + action + " : " + e.getMessage() + RESET;

        if (!silentMode) {
            System.err.println(message);
            e.printStackTrace();
        }

        writeToFile(message);

        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw)) {
            e.printStackTrace(pw);
            writeToFile(sw.toString());
        } catch (IOException ex) {
            System.err.println(RED + getTimeStamp() + " ❌ Error writing stack trace to log file: " + ex.getMessage() + RESET);
        }
    }

    public static void logSuccess(String action) {
        String message = GREEN + getTimeStamp() + " ✅ " + action + " successfully completed!" + RESET;
        if (!silentMode) System.out.println(message);
        writeToFile(message);
    }

    public static void logInfo(String message) {
        String formattedMessage = BLUE + getTimeStamp() + " ℹ️ " + message + RESET;
        if (!silentMode) System.out.println(formattedMessage);
        writeToFile(formattedMessage);
    }

    public static void logWarn(String action, String warningMessage) {
        String message = YELLOW + getTimeStamp() + " ⚠️ WARNING during " + action + " : " + warningMessage + RESET;
        if (!silentMode) System.out.println(message);
        writeToFile(message);
    }

    public static void logDebug(String message) {
        String formattedMessage = PURPLE + getTimeStamp() + " 🐞 DEBUG: " + message + RESET;
        if (!silentMode) System.out.println(formattedMessage);
        writeToFile(formattedMessage);
    }
}
