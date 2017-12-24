package com.company;


public class ExeptionOnCommand extends Exception {
    private String file_path;

    public String toString() {
        return "Произошла ошибка в команде: "+file_path;
    }

    public ExeptionOnCommand(String command) {
        super("Ошибка");
        file_path = new String(command);
    }
}
