package com.company.MyStreams;

public class MyRead extends Thread {
    private MyRead myThread;
    private Thread stoppedThread;
    private int is_read_avalible;
    private String text_commit;
    private IInput read;

    public static MyRead CreateMyRead(IInput _in) {
        MyRead res = new MyRead();
        res.is_read_avalible = 0;
        res.setDaemon(true);
        res.myThread = res;
        res.read = _in;
        res.start();
        res.suspend();
        return res;
    }

    public synchronized String GetInput() {
        is_read_avalible = 1;
        stoppedThread = Thread.currentThread();
        stoppedThread.suspend();
        return text_commit;
    }

    public void ExecuteOut() {
        if (is_read_avalible != 0) {
            text_commit = read.GetString();
            myThread.resume();
        }
    }

    //public void

    @Override
    public void run() //Этот метод будет выполнен в побочном потоке
    {
        while (!Thread.interrupted()) {
            if (is_read_avalible == 1) {
                //логика чтения откуда либо
                is_read_avalible = 0;
                stoppedThread.resume();
                stoppedThread = null;
                myThread.suspend();
            }
            if (stoppedThread == null) { myThread.suspend(); }
        }
    }


}
