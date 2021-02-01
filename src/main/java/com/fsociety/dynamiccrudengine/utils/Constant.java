package com.fsociety.dynamiccrudengine.utils;

import com.fsociety.dynamiccrudengine.model.Project;
import com.fsociety.dynamiccrudengine.model.Table;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Constant {
    public static final Color backgroundColor=new Color(36,50,61);
    public static final Color buutonColor=new Color(61,61,249);
    public static final Color buttonColorSecondary=new Color(122,184,225);
    public static final int widthWindow=600;
    public static final int heightWindow=750;
    public static final Font fontTitle=new Font("Lato",Font.PLAIN,36);
    public static final Font fontSubtitle=new Font("Lato",Font.PLAIN,24);
    public static final Font fontLabels=new Font("Lato",Font.PLAIN,20);
    public static final Font fontbutton=new Font("Lato",Font.PLAIN,14);
    public static final Color colorFont=new Color(255,255,255);
    public static Map<String, List<Table>> tablesList=null;
    public static Project project=null;
    public static String urlConnexion="";
    public static void configuratorWindows(JFrame jFrame){
        UIManager.put("OptionPane.background",Constant.backgroundColor);
        UIManager.put("Panel.background",Constant.backgroundColor);
        UIManager.put("OptionPane.messageForeground",Constant.colorFont);
        jFrame.setTitle("Dynamic CRUD Engine");
        jFrame.setLocationRelativeTo(null);
        jFrame.setLayout(null);
        jFrame.setResizable(false);
        jFrame.setSize(Constant.widthWindow,Constant.heightWindow);
        jFrame.getContentPane().setBackground(Constant.backgroundColor);
        jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        jFrame.setLocationRelativeTo(jFrame.getParent());
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                int intput=JOptionPane.showOptionDialog(jFrame,"¿Esta seguro teminar el proceso?\n Se eliminara el proceso", "Cuadro de confirmacion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Si","No"},"Si");
                if(intput== JOptionPane.YES_OPTION){
                    try {
                        Utils.removeTempProject();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    System.exit(0);
                }
            }
        });
    }
}
