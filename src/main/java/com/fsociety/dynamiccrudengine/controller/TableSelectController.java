package com.fsociety.dynamiccrudengine.controller;

import com.fsociety.dynamiccrudengine.business.GenerateSourceBusiness;
import com.fsociety.dynamiccrudengine.utils.Constant;
import com.fsociety.dynamiccrudengine.utils.Utils;
import com.fsociety.dynamiccrudengine.view.InitialWindowView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TableSelectController {

    private final GenerateSourceBusiness generateSourceBusiness;

    public TableSelectController() {
        this.generateSourceBusiness=new GenerateSourceBusiness();
    }

    public void generateSource(DefaultTableModel modelTableSelect){
        this.generateSourceBusiness.generateSource(modelTableSelect);
    }

    public void saveProject(JFrame frame){
        try {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.showOpenDialog(fileChooser);
            String originRoute= Utils.obtenerRutaPorServidor()+"tmp"+Utils.obtenerSeparadorRutaPorServidor()+ Constant.project.getNameProject();
            String destRoute = fileChooser.getSelectedFile().getAbsolutePath()+"\\"+Constant.project.getNameProject()+".zip";
            Utils.comprimir(originRoute,destRoute);
            int intput=JOptionPane.showOptionDialog(frame,"Operación realizada exitosamente\n" +
                            "¿Desea terminar la ejecución?", "Cuadro de confirmacion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Si","No"},"Si");
            if(intput==JOptionPane.YES_OPTION){
                Utils.removeTempProject();
                System.exit(0);
            }
            frame.setVisible(false);
            InitialWindowView view=new InitialWindowView();
            view.setVisible(true);
        }catch (Exception e){
            JOptionPane.showMessageDialog(frame,"Error al realizar la operación","Error al guardar el archivo",JOptionPane.ERROR_MESSAGE);
        }
    }
}
