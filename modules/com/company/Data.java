package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import com.company.MyStreams.*;

public class Data {
    public MyFile directory;
    private Scanner input;
    public ArrayList<ABSCommand> exec_commands;
    public MyRead Input;
    public MyWritte Output;
    public MyWritte ShellOutput;
    public boolean execute;

    public Data(String main_path, Scanner inp) {
        try {
            directory = new MyFile(main_path);
        }
        catch (FileNotFound fileNotFound) {
            System.out.println(fileNotFound.toString());
        }
        input = inp;
        exec_commands = new ArrayList<ABSCommand>();
        execute = true;
    }

    public Scanner GetInput() {return input;}
    public ABSCommand FindCommandByName(String name) {
        ABSCommand exe = null;
        try {
            exe = exec_commands.stream()
                    .filter((ABSCommand x) -> name.equals(x.GetName()))
                    .findFirst()
                    .get();
        }
        catch (NoSuchElementException err) {
            throw err;
        }
        return exe;
    }

    public void command_shifre(ArrayList<String> commands) {
        try {
            FindCommandByName(commands.get(0)).ExecCommand(commands);
        }
        catch (NoSuchElementException err) {
            System.out.println("command "+commands.get(0)+" not found");
        } catch (FileNotFound fileNotFound) {
            System.out.println(fileNotFound.toString());
        } catch (IOException e) {
            System.out.println(e.toString());
        } catch (ExeptionOnCommand exeptionOnCommand) {
            System.out.println(exeptionOnCommand);
        }
    }
}
