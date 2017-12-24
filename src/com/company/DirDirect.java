package com.company;

import com.company.MyStreams.MyRead;
import com.company.MyStreams.MyWritte;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DirDirect implements Runnable {
    static Data core;
    static String modulePath;
    static ModuleLoader loader;

    public boolean is_exe() { return core.execute; }

    public DirDirect(MyRead in, MyWritte out, MyWritte shell_out) {
        core = new Data("/",in ,out, shell_out);
        modulePath = "/Users/Alexey/Documents/Programm Projects/Java/DirDirector/bytecodemodules";

        loader = new ModuleLoader(modulePath, ClassLoader.getSystemClassLoader());

        File dir = new File(loader.GetWorkDir());

        for (String module : dir.list()) {
            try {
                String moduleName = module.split(".class")[0];
                Class clazz = loader.loadClass(moduleName);
                ABSCommand _module = (ABSCommand) clazz.getConstructor(Data.class).newInstance(core);
                core.exec_commands.add(_module);

            } catch (ClassNotFoundException e) {
                System.out.println(e.toString());
            } catch (InstantiationException e) {
                System.out.println(e.toString());
            } catch (IllegalAccessException e) {
                System.out.println(e.toString());
            } catch (NoSuchMethodException e) {
                System.out.println(e.toString());
            } catch (InvocationTargetException e) {
                System.out.println(e.toString());
            }
        }
    }

    public synchronized void Out(String In) {
        //System.out.println(Thread.currentThread().getName());
        ArrayList<String> comm = new ArrayList<String>();
        String testStr = (" "+In).replaceAll("\\\\ ","#");
        Pattern p = Pattern.compile(" [^ ]+");
        Matcher m = p.matcher(testStr);

        while(m.find()) {
            comm.add(testStr.substring(m.start(), m.end()).replaceAll(" ","").replaceAll("#"," "));
        }
        core.command_shifre(comm);
        //core.Output.SetOutput(core.command_shifre(comm));
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (!core.execute) {return;}
            Out(core.Input.GetInput());
        }
        //System.out.println(Thread.currentThread().getName());
    }
}