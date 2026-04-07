package com.calchoras.util.logger;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String path = "logs";
    private static final int keepDays = 7;

    public static void info(String msg) { write("INFO", msg, null); }
    public static void warn(String msg) { write("WARN", msg, null); }
    public static void error(String msg, Throwable e) { write("ERROR", msg, e); }

    private static synchronized void write(String level, String msg, Throwable e) {
        File folder = new File(path);
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                System.out.println("[INFO] Diretório de logs criado: " + folder.getName());
            }
        }

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String fileName = path + File.separator + "log_" + date.substring(0, 10) + ".log";
        File fileOfTheDay = new File(fileName);

        if (!fileOfTheDay.exists()) {
            cleanLog();
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(fileOfTheDay, true))) {
            pw.printf("[%s] [%s] %s%n", date, level, msg);
            if (e != null) {
                e.printStackTrace(pw);
            }
        } catch (IOException ioException) {
            System.err.println("CUIDADO: Logger falhou ao gravar no disco: " + ioException.getMessage());
        }
    }

    private static void cleanLog() {
        File folder = new File(path);
        if (!folder.exists()) return;

        long cut = System.currentTimeMillis() - (keepDays * 24L * 60 * 60 * 1000);

        File[] files = folder.listFiles((dir, nome) -> nome.startsWith("log_") && nome.endsWith(".log"));

        if (files != null) {
            for (File f : files) {
                if (f.lastModified() < cut) {
                    if (f.delete()) {
                        System.out.println("[INFO] Log antigo removido: " + f.getName());
                    }
                }
            }
        }
    }
}