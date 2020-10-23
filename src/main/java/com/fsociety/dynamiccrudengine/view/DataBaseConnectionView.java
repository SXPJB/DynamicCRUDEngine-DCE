package com.fsociety.dynamiccrudengine.view;

import com.fsociety.dynamiccrudengine.controller.DataBaseConnectionController;
import com.fsociety.dynamiccrudengine.model.Table;
import com.fsociety.dynamiccrudengine.utils.Constant;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DataBaseConnectionView extends JFrame implements ActionListener {

    private JLabel titleLabel;
    private JLabel subTitleLabel;

    private JLabel hostLabel;
    private JLabel userLabel;
    private JLabel passwordLabel;
    private JLabel databaseLabel;

    private JTextField hostTextField;
    private JTextField userTextField;
    private JTextField passwordField;
    private JTextField databaseTextField;

    private JButton nextButton;

    public DataBaseConnectionView(){
        super();
        Constant.configuratorWindows(this);
        initialComponents();
    }

    public void initialComponents(){
        titleLabel=new JLabel();
        titleLabel.setText("Dynamic CRUD Engine");
        titleLabel.setFont(Constant.fontTitle);
        titleLabel.setForeground(Constant.colorFont);
        titleLabel.setBounds(100,38,400,43);
        subTitleLabel=new JLabel();
        subTitleLabel.setText("Conexción a base de datos");
        subTitleLabel.setFont(Constant.fontSubtitle);
        subTitleLabel.setForeground(Constant.colorFont);
        subTitleLabel.setBounds(147,109,300,30);

        hostLabel=new JLabel();
        hostLabel.setText("Host:");
        hostLabel.setFont(Constant.fontLabels);
        hostLabel.setForeground(Constant.colorFont);
        hostLabel.setBounds(22,185,191,24);
        userLabel=new JLabel();
        userLabel.setText("Usuario:");
        userLabel.setFont(Constant.fontLabels);
        userLabel.setForeground(Constant.colorFont);
        userLabel.setBounds(22,263,191,24);
        passwordLabel=new JLabel();
        passwordLabel.setText("Contraseña:");
        passwordLabel.setFont(Constant.fontLabels);
        passwordLabel.setForeground(Constant.colorFont);
        passwordLabel.setBounds(22,337,191,24);
        databaseLabel=new JLabel();
        databaseLabel.setText("Base de datos:");
        databaseLabel.setFont(Constant.fontLabels);
        databaseLabel.setForeground(Constant.colorFont);
        databaseLabel.setBounds(22,415,191,24);

        hostTextField=new JTextField();
        hostTextField.setBounds(231,181,341,32);
        hostTextField.setFont(Constant.fontLabels);
        hostTextField.setText("localhost:3306");

        userTextField=new JTextField();
        userTextField.setBounds(231,257,341,32);
        userTextField.setFont(Constant.fontLabels);
        userTextField.setText("root");

        passwordField=new JTextField();
        passwordField.setBounds(231,333,341,32);
        passwordField.setFont(Constant.fontLabels);

        databaseTextField=new JTextField();
        databaseTextField.setBounds(231,409,341,32);
        databaseTextField.setFont(Constant.fontLabels);

        nextButton=new JButton();
        nextButton.setText("Siguiente");
        nextButton.setFont(Constant.fontLabels);
        nextButton.setForeground(Constant.colorFont);
        nextButton.setBackground(Constant.buutonColor);
        nextButton.setBounds(192,645,216,42);
        nextButton.addActionListener(this);

        this.add(titleLabel);
        this.add(subTitleLabel);
        this.add(hostLabel);
        this.add(userLabel);
        this.add(passwordLabel);
        this.add(databaseLabel);
        this.add(userTextField);

        this.add(hostTextField);
        this.add(userTextField);
        this.add(passwordField);
        this.add(databaseTextField);
        this.add(nextButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(validateField().equals("Exito")){
            DataBaseConnectionController dataBaseConnectionController=new DataBaseConnectionController();
            if(!dataBaseConnectionController.dataBaseConnection(hostTextField.getText(),userTextField.getText(),passwordField.getText(),databaseTextField.getText())){
                return;
            }
            this.setVisible(false);
            TableSelectView tableSelectView=new TableSelectView();
            tableSelectView.setVisible(true);
        }else {
            UIManager.put("OptionPane.background",Constant.backgroundColor);
            UIManager.put("Panel.background",Constant.backgroundColor);
            UIManager.put("OptionPane.messageForeground",Constant.colorFont);
            JOptionPane.showMessageDialog(this, validateField(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String validateField(){
        if(hostTextField.getText()==null||hostTextField.getText().isEmpty()){
            return "El campo \"Host\" no puede ir vacio";
        }
        if(userTextField.getText()==null||userTextField.getText().isEmpty()){
            return "El campo \"Usuario\" no puede ir vacio";
        }
        if(passwordField.getText()==null||passwordField.getText().isEmpty()){
            return "El campo \"Contraseña\" no puede ir vacio";
        }
        if(databaseTextField.getText()==null||databaseTextField.getText().isEmpty()){
            return "El campo \"Base de Datos\" ir vacio";
        }
        return "Exito";
    }
}
