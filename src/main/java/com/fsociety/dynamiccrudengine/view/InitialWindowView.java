package com.fsociety.dynamiccrudengine.view;

import com.fsociety.dynamiccrudengine.controller.DownloadManagerController;
import com.fsociety.dynamiccrudengine.model.Project;
import com.fsociety.dynamiccrudengine.utils.Constant;
import javax.swing.*;
import java.awt.event.*;

public class InitialWindowView extends JFrame implements ActionListener {

    private JLabel titleLabel;
    private JLabel subTitleLabel;
    private JLabel nameProjectLabel;
    private JLabel groupIdLabel;
    private JLabel artifactIdLabel;
    private JLabel versionLabel;
    private JLabel descriptionLabel;
    private JLabel namePackageLabel;
    private JTextField nameProjectTextField;
    private JTextField groupIdTextField;
    private JTextField artifactIdTextField;
    private JTextField versionTextField;
    private JTextField descriptionTextField;
    private JTextField namePackageTextField;
    private JButton nextButton;

    private final DownloadManagerController downloadManagerController;


     public InitialWindowView() {
         super();
         Constant.configuratorWindows(this);
         initialComponents();
         downloadManagerController=new DownloadManagerController();
     }

     //This method creates and configures the form components
     public void initialComponents(){
         titleLabel=new JLabel();
         titleLabel.setText("Dynamic CRUD Engine");
         titleLabel.setFont(Constant.fontTitle);
         titleLabel.setForeground(Constant.colorFont);
         titleLabel.setBounds(100,38,400,43);
         subTitleLabel=new JLabel();
         subTitleLabel.setText("Iniciar proyecto");
         subTitleLabel.setFont(Constant.fontSubtitle);
         subTitleLabel.setForeground(Constant.colorFont);
         subTitleLabel.setBounds(206,109,188,30);

         //label for fields
         nameProjectLabel=new JLabel();
         nameProjectLabel.setText("Nombre del proyecto:");
         nameProjectLabel.setFont(Constant.fontLabels);
         nameProjectLabel.setForeground(Constant.colorFont);
         nameProjectLabel.setBounds(22,185,191,24);
         groupIdLabel=new JLabel();
         groupIdLabel.setText("GroupId:");
         groupIdLabel.setFont(Constant.fontLabels);
         groupIdLabel.setForeground(Constant.colorFont);
         groupIdLabel.setBounds(22,263,191,24);
         artifactIdLabel=new JLabel();
         artifactIdLabel.setText("ArtifactId:");
         artifactIdLabel.setFont(Constant.fontLabels);
         artifactIdLabel.setForeground(Constant.colorFont);
         artifactIdLabel.setBounds(22,337,191,24);
         versionLabel=new JLabel();
         versionLabel.setText("Versión:");
         versionLabel.setFont(Constant.fontLabels);
         versionLabel.setForeground(Constant.colorFont);
         versionLabel.setBounds(22,415,191,24);
         descriptionLabel=new JLabel();
         descriptionLabel.setText("Descripción:");
         descriptionLabel.setFont(Constant.fontLabels);
         descriptionLabel.setForeground(Constant.colorFont);
         descriptionLabel.setBounds(22,489,191,24);
         namePackageLabel=new JLabel();
         namePackageLabel.setText("Nombre del paquete:");
         namePackageLabel.setFont(Constant.fontLabels);
         namePackageLabel.setForeground(Constant.colorFont);
         namePackageLabel.setBounds(22,563,191,24);

         //fields
         nameProjectTextField=new JTextField();
         nameProjectTextField.setBounds(231,181,341,32);
         nameProjectTextField.setFont(Constant.fontLabels);
         nameProjectTextField.addKeyListener(new KeyListener() {
             @Override
             public void keyTyped(KeyEvent e) {
             }

             @Override
             public void keyPressed(KeyEvent e) {
             }

             @Override
             public void keyReleased(KeyEvent e) {
                 artifactIdTextField.setText(((JTextField)e.getSource()).getText());
             }
         });
         groupIdTextField=new JTextField();
         groupIdTextField.setText("com.ejemplo");
         groupIdTextField.setBounds(231,257,341,32);
         groupIdTextField.setFont(Constant.fontLabels);
         groupIdTextField.addKeyListener(new KeyListener() {
             @Override
             public void keyTyped(KeyEvent e) {
             }

             @Override
             public void keyPressed(KeyEvent e) {
             }

             @Override
             public void keyReleased(KeyEvent e) {
                 namePackageTextField.setText(((JTextField)e.getSource()).getText());
             }
         });
         artifactIdTextField=new JTextField();
         artifactIdTextField.setBounds(231,333,341,32);
         artifactIdTextField.setFont(Constant.fontLabels);
         versionTextField=new JTextField();
         versionTextField.setBounds(231,409,341,32);
         versionTextField.setFont(Constant.fontLabels);
         descriptionTextField=new JTextField();
         descriptionTextField.setBounds(231,485,341,32);
         descriptionTextField.setFont(Constant.fontLabels);
         namePackageTextField=new JTextField();
         namePackageTextField.setBounds(231,561,341,32);
         namePackageTextField.setFont(Constant.fontLabels);
         namePackageTextField.setText("com.ejemplo");

         //button
         nextButton=new JButton();
         nextButton.setText("Siguiente");
         nextButton.setFont(Constant.fontLabels);
         nextButton.setForeground(Constant.colorFont);
         nextButton.setBackground(Constant.buutonColor);
         nextButton.setBounds(192,645,216,42);
         nextButton.addActionListener(this);

         //add a Frame
         this.add(titleLabel);
         this.add(subTitleLabel);
         this.add(nameProjectLabel);
         this.add(groupIdLabel);
         this.add(artifactIdLabel);
         this.add(versionLabel);
         this.add(descriptionLabel);
         this.add(nameProjectLabel);
         this.add(namePackageLabel);
         this.add(nameProjectTextField);
         this.add(groupIdTextField);
         this.add(artifactIdTextField);
         this.add(versionTextField);
         this.add(descriptionTextField);
         this.add(namePackageTextField);
         this.add(nextButton);

     }

    public void actionPerformed(ActionEvent e) {
         if(e.getSource()==nextButton) {
             if (validateField().equals("Exito")) {
                 UIManager.put("OptionPane.background",Constant.backgroundColor);
                 UIManager.put("Panel.background",Constant.backgroundColor);
                 UIManager.put("OptionPane.messageForeground",Constant.colorFont);
                 int intput=JOptionPane.showOptionDialog(this,"¿Esta seguro de conitnuar?\n Está acción no se puede revertir", "Cuadro de confirmacion",
                         JOptionPane.YES_NO_OPTION,
                         JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Si","No"},"Si");
                 if(intput== JOptionPane.YES_OPTION){
                     downloadManagerController.downloadProjectZip(
                             new Project(nameProjectTextField.getText().replace(" ",""), groupIdTextField.getText().replace(" ",""),
                                     artifactIdTextField.getText().replace(" ",""), versionTextField.getText().replace(" ",""),
                                     descriptionTextField.getText(), namePackageTextField.getText().replace(" ","")));
                     DataBaseConnectionView dataBaseConnectionView=new DataBaseConnectionView();
                     dataBaseConnectionView.setLocationRelativeTo(this.getParent());
                     dataBaseConnectionView.setVisible(true);
                     this.setVisible(false);
                 }
             } else {
                 UIManager.put("OptionPane.background",Constant.backgroundColor);
                 UIManager.put("Panel.background",Constant.backgroundColor);
                 UIManager.put("OptionPane.messageForeground",Constant.colorFont);
                 JOptionPane.showMessageDialog(this, validateField(), "Error", JOptionPane.ERROR_MESSAGE);
             }
         }
    }

    public String validateField(){
         if(nameProjectTextField.getText()==null||nameProjectTextField.getText().isEmpty()){
             return "El campo \"Nombre del proyecto\" no puede ir vacio";
         }
        if(groupIdTextField.getText()==null||groupIdTextField.getText().isEmpty()){
            return "El campo \"GroupId\" no puede ir vacio";
        }
        if(artifactIdTextField.getText()==null||artifactIdTextField.getText().isEmpty()){
            return "El campo \"ArtifactId\" no puede ir vacio";
        }
        if(versionTextField.getText()==null||versionTextField.getText().isEmpty()){
            return "El campo \"Versión\" no puede ir vacio";
        }
        if(descriptionTextField.getText()==null||descriptionTextField.getText().isEmpty()){
            return "El campo \"Descripción\" no puede ir vacio";
        }
        if(namePackageTextField.getText()==null||namePackageTextField.getText().isEmpty()){
            return "El campo \"Nombre del paquete\" no puede ir vacio";
        }
        return "Exito";
    }
}
