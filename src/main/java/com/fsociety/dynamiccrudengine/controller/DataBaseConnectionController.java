package com.fsociety.dynamiccrudengine.controller;

import com.fsociety.dynamiccrudengine.business.DataBaseConnectionBusiness;
import com.fsociety.dynamiccrudengine.utils.Constant;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class DataBaseConnectionController {

    private DataBaseConnectionBusiness dataBaseConnectionBusiness;

    public DataBaseConnectionController(){
        dataBaseConnectionBusiness=new DataBaseConnectionBusiness();
    }

    public void dataBaseConnection(String host,String user,String password,String database){
        Connection connection=null;
        try {
            connection=dataBaseConnectionBusiness.connectionDataBase(host,user,password,database);
            if(connection==null){
                throw new SQLException("Error de conexion");
            }
            UIManager.put("OptionPane.background", Constant.backgroundColor);
            UIManager.put("Panel.background",Constant.backgroundColor);
            UIManager.put("OptionPane.messageForeground",Constant.colorFont);
            JOptionPane.showMessageDialog(null,"Conexcion exitosa","Exito",JOptionPane.INFORMATION_MESSAGE);
            connection.close();
        }catch (SQLException e){
            System.out.println("error: "+e.getMessage());
        }
    }
}
