package com.fsociety.dynamiccrudengine.controller;

import com.fsociety.dynamiccrudengine.business.DataBaseConnectionBusiness;
import com.fsociety.dynamiccrudengine.utils.Constant;
import javax.swing.*;
import java.sql.Connection;

public class DataBaseConnectionController {

    private final DataBaseConnectionBusiness dataBaseConnectionBusiness;

    public DataBaseConnectionController(){
        dataBaseConnectionBusiness=new DataBaseConnectionBusiness();
    }

    public boolean dataBaseConnection(String host,String user,String password,String database){
        Connection connection=null;
        boolean connexionSuccess=false;
        try {
            connection=dataBaseConnectionBusiness.connectionDataBase(host,user,password,database);
            if(connection==null){
                throw new Exception("Error de conexion");
            }
            UIManager.put("OptionPane.background", Constant.backgroundColor);
            UIManager.put("Panel.background",Constant.backgroundColor);
            UIManager.put("OptionPane.messageForeground",Constant.colorFont);
            JOptionPane.showMessageDialog(null,"Conexcion exitosa","Exito",JOptionPane.INFORMATION_MESSAGE);
            Constant.tablesList=dataBaseConnectionBusiness.getMetadata(connection);
            System.out.println("Tablas seleccionable \n"+Constant.tablesList.get("listSelect"));
            System.out.println("Tablas No seleccionable \n"+Constant.tablesList.get("listNoSelect"));
            connection.close();
            connexionSuccess=true;
        }catch (Exception e){
            System.out.println("error: "+e.getMessage());
        }
        return connexionSuccess;
    }
}
