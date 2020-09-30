package com.fsociety.dynamiccrudengine.business;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnectionBusiness {

    public Connection connectionDataBase(String host,String user,String password,String database){
        Connection connection=null;
        try {
            connection= DriverManager.getConnection(
                    "jdbc:mysql://"+host+"/"+database+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", user, password);
            if(connection==null){
                throw  new SQLException("Error en la conexcion");
            }
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null,e.getMessage()+" verficar sus datos", "Error de conexcion",JOptionPane.ERROR_MESSAGE);
        }
        return connection;
    }

}
