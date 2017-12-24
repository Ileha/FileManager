package com.company;

import com.company.MyStreams.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Data {
    public MyFile directory;
    //private Scanner input;

    public MyRead Input;
    public MyWritte Output;
    public MyWritte ShellOutput;

    public ArrayList<ABSCommand> exec_commands;
    public boolean execute;

    public Data(String main_path, MyRead in, MyWritte out, MyWritte shell_out) {
        try {
            directory = new MyFile(main_path);
        }
        catch (FileNotFound fileNotFound) {
            System.out.println(fileNotFound.toString());
        }
        //input = inp;
        Input = in;
        Output = out;
        ShellOutput = shell_out;
        exec_commands = new ArrayList<ABSCommand>();
        execute = true;
    }

    //public Scanner GetInput() {return input;}
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
            Output.SetOutput("command "+commands.get(0)+" not found");
        }
        catch (FileNotFound fileNotFound) {
            Output.SetOutput(fileNotFound.toString());
        }
        catch (IOException e) {
            Output.SetOutput(e.toString());
        }
        catch (ExeptionOnCommand exeptionOnCommand) {
            Output.SetOutput(exeptionOnCommand.toString());
        }
        //return "";
    }
}
