package com.company;

public class FileNotFound extends Exception {
    private String file_path;
    public String toString() {
        return "Не найдено: "+file_path;
    }

    public FileNotFound(String message, String pathe) {
        super(message);
        file_path = new String(pathe);
    }
}
