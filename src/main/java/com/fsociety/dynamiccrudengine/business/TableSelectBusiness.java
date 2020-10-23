package com.fsociety.dynamiccrudengine.business;

import com.fsociety.dynamiccrudengine.model.Table;
import com.fsociety.dynamiccrudengine.utils.Constant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableSelectBusiness {
    private  int i=0;

    public  List<String> searchRelations(String tableName){
        List<String> tableRelations=null;
        boolean bandera=true;
        try {
            List<Table> tableList = Constant.tablesList.get("listSelect");
            tableRelations = new ArrayList<>();
            Table table = tableList.stream().filter(f -> f.getName().equals(tableName)).collect(Collectors.toList()).get(0);
            System.out.println(table);
            while (bandera&&i<table.getForagingKeyList().size()){
                Table table1=tableList.stream().filter(f -> f.getName().equals(table.getForagingKeyList().get(i).getTableName())).collect(Collectors.toList()).get(0);
                System.out.println(table1.getName());
                System.out.println(table1.getForagingKeyList());
                if(table.getForagingKeyList()==null){
                    bandera=false;
                }
                i++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  tableRelations;
    }
}
