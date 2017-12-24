package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class ABSCommand {
    private String name;
    protected Data WorkDir;
    protected ArrayList<String> Commands;
    protected ArrayList<String> Keys;
    private int min_args;

    //конструктор
    public ABSCommand(String _name, int _min_args, Data _dir) {
        name = _name;
        WorkDir = _dir;
        min_args = _min_args;
    }

    protected String GetPath(String path) throws FileNotFound {
        Pattern p = Pattern.compile("/[^/]+$");
        Matcher m = p.matcher(path);

        if (m.find()) return path.substring(0, m.start()+1);
        else throw new FileNotFound("Ошибка", path);
    }
    protected String GetFile(String path) throws FileNotFound {
        Pattern p = Pattern.compile("/[^/]+$");
        Matcher m = p.matcher(path);

        if (m.find()) {
            String res = path.substring(m.start()+1, m.end());
            if (res.equals("")) {throw new FileNotFound("Ошибка", path);}
            else {return path.substring(m.start()+1, m.end());}
        }
        else return path;
    }

    protected boolean FindKey(String need_key) {
        return Keys.stream().anyMatch((String x) -> x.equals(need_key));
    }

    private static ArrayList<String> GetKeys(ArrayList<String> commands) {
        Pattern p = Pattern.compile("-[a-z]{1}");
        ArrayList<String> res = commands.stream()
                .filter((String x) -> p.matcher(x).matches()).collect(Collectors.toCollection(ArrayList::new));
        return res;
    }

    private static ArrayList<String> GetCommands(ArrayList<String> commands) {
        Pattern p = Pattern.compile("-[a-z]{1}");
        ArrayList<String> res = commands.stream()
                .filter((String x) -> !p.matcher(x).matches()).collect(Collectors.toCollection(ArrayList::new));
        return res;
    }

    public String GetName() {
        return name;
    }
    public void ExecCommand(ArrayList<String> commands) throws FileNotFound, IOException, ExeptionOnCommand {
        Commands = GetCommands(commands);
        if (min_args <= Commands.size()-1) {
            Keys = GetKeys(commands);
            ABSExecCommand();
            Keys = null;
        }
        else {
            WorkDir.Output.SetOutput("error of argument read man");
        }
        Commands = null;
    }
    protected abstract void ABSExecCommand() throws FileNotFound, IOException, ExeptionOnCommand;
    public abstract String ForMan();
}