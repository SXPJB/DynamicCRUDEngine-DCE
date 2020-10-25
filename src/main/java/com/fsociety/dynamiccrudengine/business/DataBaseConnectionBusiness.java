package com.fsociety.dynamiccrudengine.business;

import com.fsociety.dynamiccrudengine.model.Attribute;
import com.fsociety.dynamiccrudengine.model.ForagingKey;
import com.fsociety.dynamiccrudengine.model.PrimaryKey;
import com.fsociety.dynamiccrudengine.model.Table;
import com.fsociety.dynamiccrudengine.utils.Constant;
import com.google.common.base.CaseFormat;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBaseConnectionBusiness {

    public Connection connectionDataBase(String host,String user,String password,String database){
        Connection connection=null;
        try {
            Constant.urlConnexion="jdbc:mysql://"+host+"/"+database+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            Constant.project.setUserDB(user);
            Constant.project.setPassDB(password);
            connection= DriverManager.getConnection(Constant.urlConnexion, Constant.project.getUserDB(), Constant.project.getPassDB());
            if(connection==null){
                throw  new SQLException("Error en la conexcion");
            }
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null,e.getMessage()+" verficar sus datos", "Error de conexcion",JOptionPane.ERROR_MESSAGE);
        }
        return connection;
    }

    public Map<String,List<Table>> getMetadata(Connection connection){
        String catalog=null;
        String schemaPattern=null;
        List<Table> tableList=null;
        List<Table> tableNoSelectList=null;
        Map<String,List<Table>> tablesMap=null;
        try{
            catalog=connection.getCatalog();
            schemaPattern=connection.getSchema();

            //get metadata from database
            DatabaseMetaData databaseMetaData=connection.getMetaData();

            ResultSet rs=databaseMetaData.getTables(catalog,schemaPattern,"%", null);
            if(rs==null){
                throw new Exception("No se encontro nada");
            }
            tableList=new ArrayList<>();
            tableNoSelectList=new ArrayList<>();
            while (rs.next()){
                Table table=new Table();
                table.setName(rs.getString("TABLE_NAME"));
                //get primary key
                ResultSet resultSet=databaseMetaData.getPrimaryKeys(catalog,schemaPattern,table.getName());
                while (resultSet.next()){
                    table.setPrimaryKey(new PrimaryKey());
                    table.getPrimaryKey().setName(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, resultSet.getString("COLUMN_NAME")));
                }
                resultSet=databaseMetaData.getColumns(catalog, schemaPattern, table.getName(), null);
                //get columns table
                table.setAttributeList(new ArrayList<Attribute>());
                while (resultSet.next()){
                    if(table.getPrimaryKey().getName().equals(resultSet.getString("COLUMN_NAME"))){
                        table.getPrimaryKey().setType(resultSet.getString("TYPE_NAME"));
                        table.getPrimaryKey().setAutoincrement(resultSet.getString("IS_AUTOINCREMENT").equals("YES")?true:false);
                    }else{
                        table.getAttributeList().add(new Attribute(resultSet.getString("COLUMN_NAME"),
                                resultSet.getString("TYPE_NAME")));
                    }
                }
                //get Forking key
                resultSet=databaseMetaData.getImportedKeys(catalog,schemaPattern,table.getName());
                table.setForagingKeyList(new ArrayList<ForagingKey>());
                while (resultSet.next()){
                    table.getForagingKeyList().add(new ForagingKey(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,resultSet.getString("FKCOLUMN_NAME")),
                            resultSet.getString("PKTABLE_NAME")));
                }
                if(table.getPrimaryKey().getAutoincrement()&&table.getPrimaryKey().getType().equals("INT")){
                    tableList.add(table);
                }else {
                    tableNoSelectList.add(table);
                }
            }
            tablesMap=new HashMap<>();
            tablesMap.put("listSelect",tableList);
            tablesMap.put("listNoSelect",tableNoSelectList);

        }catch (Exception e){
            e.printStackTrace();
        }
        return tablesMap;
    }

}
