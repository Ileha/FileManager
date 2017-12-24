package com.company.MyStreams;

import com.company.ABSCommand;

import java.util.ArrayList;

public class MyWritte extends Thread {
    private MyWritte myThread;
    //private int is_writte_avalible;
    private ArrayList<String> writtabltext;
    private IOutput write;

    public static MyWritte CreateMyRead(IOutput _out) {
        MyWritte res = new MyWritte();
        //res.is_writte_avalible = 0;
        res.writtabltext = new ArrayList<>();
        res.setDaemon(true);
        res.myThread = res;
        res.write = _out;
        res.start();
        res.suspend();
        return res;
    }

    public synchronized void SetOutput(String text) {
        writtabltext.add(text);
        //is_writte_avalible = 1;
        myThread.resume();
    }

    @Override
    public void run() //Этот метод будет выполнен в побочном потоке
    {
        while (!Thread.interrupted()) {
            if (writtabltext.size() > 0) {
                //логика записи куда либо
                //is_writte_avalible = 0;
                write.write(writtabltext.get(0));
                writtabltext.remove(0);
                if (writtabltext.size() == 0) {
                    myThread.suspend();
                }
            }
        }
    }
}
