package com.company.MyStreams;

public class MyWritte extends Thread {
    private MyWritte myThread;
    private int is_writte_avalible;
    private String writtabltext;
    private IOutput write;

    public static MyWritte CreateMyRead(IOutput _out) {
        MyWritte res = new MyWritte();
        res.is_writte_avalible = 0;
        res.setDaemon(true);
        res.myThread = res;
        res.write = _out;
        res.start();
        res.suspend();
        return res;
    }

    public synchronized void SetOutput(String text) {
        writtabltext = text;
        is_writte_avalible = 1;
        myThread.resume();
    }

    @Override
    public void run() //Этот метод будет выполнен в побочном потоке
    {
        while (!Thread.interrupted()) {
            if (is_writte_avalible == 1) {
                //логика записи куда либо
                is_writte_avalible = 0;
                write.write(writtabltext);
                myThread.suspend();
            }
        }
    }
}
