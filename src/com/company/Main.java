package com.company;

import com.company.MyStreams.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.Scanner;

//class SomeThing extends JFrame
//{
//    private JLabel label = new JLabel("entercomand$");
//    DirDirect Direct = new DirDirect();
//    JButton button = new JButton("enter");
//    TextArea ta = new TextArea("", 30, 100);
//    private JTextField enter = new JTextField();
//    private Scanner scan;
//
//    public SomeThing() {
//        super("DirDirect");
//        setBounds(100,100,1050,670);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//
//
//        getContentPane().setLayout(new GridBagLayout());
//        GridBagConstraints c = new GridBagConstraints();
//
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.weightx = 0.5;
//        //c.fill = 2;
//        c.gridx = 1;
//        c.gridy = 0;
//        ta.setEditable(false);
//        getContentPane().add(ta, c);
//        c.gridx = 0;
//        c.gridy = 1;
//
//        getContentPane().add(label, c);
//        c.gridx = 1;
//        enter.addKeyListener(new KeyAdapter() {
//            public void keyPressed(KeyEvent e) {
//                System.out.println(e.getKeyCode());
//                if (e.getKeyCode()==10) {//enter
//                    button.doClick();
//                }
//                if (e.getKeyCode()==17) {//ctrl
//                    enter.setText("");
//                }
//                if (e.getKeyCode()==16) {//left shift
//                    //enter.setText("");
//
//                    String s_res = Direct.Out("parse "+enter.getText());
//                    scan = new Scanner(s_res);
//                    ArrayList<String> res = new ArrayList<>();
//                    while (scan.hasNext()) {
//                        res.add(scan.next());
//                    }
//                    if (res.size() > 1) {
//                        ta.setText(ta.getText()+"\n"+s_res);
//                    }
//                    else {
//                        enter.setText(res.get(0));
//                    }
//                }
//            }
//
//        });
//        getContentPane().add(enter, c);
//        c.gridx = 2;
//        ButtonEventListener list = new ButtonEventListener();
//        button.addActionListener(list);
//        getContentPane().add(button, c);
//
//        setVisible(true);
//    }
//
//    class ButtonEventListener implements ActionListener {
//        public void actionPerformed(ActionEvent e) {
//            ta.setText(ta.getText()+"\n"+Direct.Out(enter.getText()));
//            enter.setText("");
//            if (!Direct.is_exe()) {
//                System.exit(0);
//            }
//        }
//    }
//}

//class testRead extends Thread {
//    private MyRead getRead;
//    private MyWritte getWritte;
//
//    public static testRead CreateMytest(MyRead _getRead, MyWritte _getWritte) {
//        testRead res = new testRead();
//        res.getRead = _getRead;
//        res.getWritte = _getWritte;
//        res.setDaemon(true);
//        res.start();
//        return res;
//    }
//
//    public void run() //Этот метод будет выполнен в побочном потоке
//    {
//        while (!Thread.interrupted()) {
//            String r = getRead.GetInput();
//            for (int i = 0; i < 5; i++) {
//                getWritte.SetOutput(r);
//            }
//        }
//    }
//}

class SomeThingTest extends JFrame
{
    private JLabel label = new JLabel("entercomand$");
    JButton button = new JButton("enter");
    TextArea ta = new TextArea("", 30, 100);
    private JTextField enter = new JTextField();

    public void WriteString(String text) {
        ta.setText(ta.getText()+"\n"+text);
        ta.setCaretPosition(ta.getText().length());
    }
    public void WriteToShell(String text) {
        Scanner scan = new Scanner(text);
        ArrayList<String> res = new ArrayList<>();
        while (scan.hasNext()) {
            res.add(scan.next());
        }
        if (res.size() > 1) {
            ta.setText(ta.getText()+"\n"+text);
        }
        else {
            enter.setText(res.get(0));
            enter.setCaretPosition(enter.getText().length());
        }
    }

    MyRead rea = MyRead.CreateMyRead(enter::getText);
    MyWritte write = MyWritte.CreateMyRead(this::WriteString);
    MyWritte shell_write = MyWritte.CreateMyRead(this::WriteToShell);

    //testRead t = testRead.CreateMytest(rea, write);

    DirDirect direct = new DirDirect(rea, write, shell_write);

    public SomeThingTest() {
        super("DirDirect");
        Thread run = new Thread(direct);
        run.start();

//        DefaultCaret caret = (DefaultCaret) ta.getCaret();
//        caret.setUpdatePolicy(ALWAYS_UPDATE);

        setBounds(100,100,1050,670);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        //c.fill = 2;
        c.gridx = 1;
        c.gridy = 0;

        ta.setEditable(false);
        getContentPane().add(ta, c);
        c.gridx = 0;
        c.gridy = 1;

        getContentPane().add(label, c);
        c.gridx = 1;

        enter.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyCode());
                if (e.getKeyCode()==10) {//enter
                    button.doClick();
                }
                if (e.getKeyCode()==17) {//ctrl
                    enter.setText("");
                }
                if (e.getKeyCode()==18) {//left shift
                    direct.Out("parse "+enter.getText());
                }
            }
        });

        getContentPane().add(enter, c);
        c.gridx = 2;
        ButtonEventListener list = new ButtonEventListener();
        button.addActionListener(list);
        getContentPane().add(button, c);

        write.SetOutput("Session started\n"+new Date(System.currentTimeMillis()).toString());

        setVisible(true);
    }

    class ButtonEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            rea.ExecuteOut();
            enter.setText("");
        }
    }
}

public class Main {
    static SomeThingTest MainW;
    public static void main(String[] args) throws IOException {
       MainW = new SomeThingTest();
    }
}
