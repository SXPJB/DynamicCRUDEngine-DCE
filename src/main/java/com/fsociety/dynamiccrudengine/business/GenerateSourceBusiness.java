package com.fsociety.dynamiccrudengine.business;

import com.fsociety.dynamiccrudengine.model.Attribute;
import com.fsociety.dynamiccrudengine.model.Table;
import com.fsociety.dynamiccrudengine.utils.Constant;
import com.fsociety.dynamiccrudengine.utils.Utils;
import com.google.common.base.CaseFormat;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateSourceBusiness {

    private List<Table> tableList = null;
    private  int i;

    public  GenerateSourceBusiness(){
        this.tableList=Constant.tablesList.get("listSelect");
        this.i=0;
    }

    public void generateSource (DefaultTableModel modelTableSelect){
        String path= Utils.obtenerRutaPorServidor()+"tmp\\"+
                Constant.project.getNameProject()+"\\src\\main\\java\\"
                +Constant.project.getNamePackage().replace(".","\\");
        File folder=null;
        try{
            //Generate entities form tables select
            folder=new File(path+"\\entity");
            folder.mkdir();

            while (i<modelTableSelect.getRowCount()){
                Table table = tableList.stream().filter(f -> f.getName().equals(modelTableSelect.getValueAt(i,0))).collect(Collectors.toList()).get(0);
                generateEntity(table,folder);
                i++;
            }
            //Generate repository form tables select
            i=0;
            folder=new File(path+"\\repository");
            folder.mkdir();
            while (i<modelTableSelect.getRowCount()){
                Table table = tableList.stream().filter(f -> f.getName().equals(modelTableSelect.getValueAt(i,0))).collect(Collectors.toList()).get(0);
                generateRepository(table,folder);
                i++;
            }
            //Generate service form tables select
            i=0;
            folder=new File(path+"\\service");
            folder.mkdir();
            while (i<modelTableSelect.getRowCount()){
                Table table = tableList.stream().filter(f -> f.getName().equals(modelTableSelect.getValueAt(i,0))).collect(Collectors.toList()).get(0);
                generateService(table,folder);
                i++;
            }
            i=0;
            folder=new File(path+"\\service\\impl");
            folder.mkdir();
            while (i<modelTableSelect.getRowCount()){
                Table table = tableList.stream().filter(f -> f.getName().equals(modelTableSelect.getValueAt(i,0))).collect(Collectors.toList()).get(0);
                generateServiceImpl(table,folder);
                i++;
            }

        }catch (Exception e){

        }

    }

    private void generateEntity(Table table,File folder){
        FileWriter classEntity=null;
        PrintWriter printWriter=null;
        try{
            System.out.println("Se genero una entitidad");
            String className= CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL,table.getName());
            classEntity=new FileWriter(folder+"\\"+className+".java");
            printWriter=new PrintWriter(classEntity);

            printWriter.println("package "+Constant.project.getNamePackage()+".entity;");
            printWriter.println("import java.io.Serializable;");
            printWriter.println("import javax.persistence.Entity;");
            printWriter.println("import javax.persistence.Id;");
            printWriter.println("import javax.persistence.Table;");
            printWriter.println("import javax.persistence.GeneratedValue;");
            printWriter.println("import javax.persistence.GenerationType;");
            printWriter.println("import javax.persistence.Column;");
            printWriter.println("import com.fasterxml.jackson.core.JsonProcessingException;");
            printWriter.println("import com.fasterxml.jackson.databind.ObjectMapper;");
            printWriter.println();
            if(table.getAttributeList().stream().filter(f-> Utils.getType(f.getType()).equals("Date")).collect(Collectors.toList()).size()>0){
                printWriter.println("import java.util.Date;");
            }
            printWriter.println("@Entity");
            printWriter.println("@Table(name= "+"\""+table.getName()+"\""+")");
            printWriter.println("public class "+className+" implements Serializable{ \n");

            printWriter.println("\tpublic "+className+"() {}\n");

            //Generate primaryKey
            printWriter.println("\t@Id");
            printWriter.println("\t@GeneratedValue(strategy = GenerationType.IDENTITY)");
            printWriter.println("\t@Column(name = \""+table.getPrimaryKey().getName()+"\")");
            printWriter.println("\tprivate "+Utils.getType(table.getPrimaryKey().getType())+" "+table.getPrimaryKey().getName()+";");

            //Generate all attributes
            for (Attribute attribute:table.getAttributeList()) {
                printWriter.println("\t@Column(name = \""+attribute.getName()+"\")");
                printWriter.println("\tprivate "+Utils.getType(attribute.getType())+" "+CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, attribute.getName())+";");
            }
            printWriter.println();

            //Generate Getters and Setters Primary Key
            String name=CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, table.getPrimaryKey().getName());
            String type=Utils.getType(table.getPrimaryKey().getType());
            printWriter.println("\tpublic " +type+" get"+Utils.getFirstLetterToUpperCase(name)+"(){");
            printWriter.println("\t\t return "+name+";");
            printWriter.println("\t}");
            printWriter.println();

            printWriter.println("\tpublic void set"+Utils.getFirstLetterToUpperCase(name)+"("+type+" "+name+"){");
            printWriter.println("\t\t  this."+name+"="+name+";");
            printWriter.println("\t}");
            printWriter.println();

            //Generate Getters and Setters
            for(Attribute attribute:table.getAttributeList()){
                name=CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, attribute.getName());
                type=Utils.getType(attribute.getType());
                printWriter.println("\tpublic " +type+" get"+Utils.getFirstLetterToUpperCase(name)+"(){");
                printWriter.println("\t\t return "+name+";");
                printWriter.println("\t}");
                printWriter.println();

                printWriter.println("\tpublic void set"+Utils.getFirstLetterToUpperCase(name)+"("+type+" "+name+"){");
                printWriter.println("\t\t  this."+name+"="+name+";");
                printWriter.println("\t}");
                printWriter.println();
            }
            printWriter.println("\t@Override");
            printWriter.println("\tpublic String toString() {");
            printWriter.println("\t\tObjectMapper mapper = new ObjectMapper();");
            printWriter.println("\t\ttry {");
            printWriter.println("\t\t\treturn mapper.writeValueAsString(this);");
            printWriter.println("\t\t} catch (JsonProcessingException e) {");
            printWriter.println("\t\t\treturn e.getMessage();");
            printWriter.println("\t\t}");
            printWriter.println("\t}\n");
            printWriter.println("}");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != classEntity)
                    classEntity.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void generateRepository(Table table,File folder){
        FileWriter classEntity=null;
        PrintWriter printWriter=null;
        System.out.println("Se genera repository");
        try{
            String className= CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL,table.getName());
            classEntity=new FileWriter(folder+"\\"+className+"Repository.java");
            printWriter=new PrintWriter(classEntity);

            printWriter.println("package "+Constant.project.getNamePackage()+".repository;");
            printWriter.println("import "+Constant.project.getNamePackage()+".entity."+className+";");
            printWriter.println("import org.springframework.data.jpa.repository.JpaRepository;");
            printWriter.println("import org.springframework.stereotype.Repository;");

            printWriter.println();

            printWriter.println("@Repository");
            printWriter.println("public interface "+className+"Repository extends JpaRepository<"+className+","+Utils.getType(table.getPrimaryKey().getType())+">{");
            printWriter.println("}");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != classEntity)
                    classEntity.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void generateService(Table table,File folder){
        FileWriter classEntity=null;
        PrintWriter printWriter=null;
        System.out.println("Se genera service");
        try {
            String className= CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL,table.getName());
            classEntity=new FileWriter(folder+"\\"+className+"Service.java");
            String name=CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, table.getPrimaryKey().getName());
            String type=Utils.getType(table.getPrimaryKey().getType());
            printWriter=new PrintWriter(classEntity);
            printWriter.println("package "+Constant.project.getNamePackage()+".service;");
            printWriter.println("import "+Constant.project.getNamePackage()+".entity."+className+";");
            printWriter.println("import java.util.List;");
            printWriter.println("import java.util.Map;");
            printWriter.println("public interface "+className+"Service{");
            printWriter.println("\tvoid insert("+className+" "+Utils.getFirstLetterToLowerCase(className)+");");
            printWriter.println("\tvoid update("+type+" "+name+", Map<String,Object> data);");
            printWriter.println("\tvoid delete("+type+" "+name+");");
            printWriter.println("\tList<"+className+"> findAll();");
            printWriter.println("}");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != classEntity)
                    classEntity.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void generateServiceImpl(Table table,File folder){
        FileWriter classEntity=null;
        PrintWriter printWriter=null;
        System.out.println("Se genera ServiceImpl");
         try {
             String className= CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL,table.getName());
             classEntity=new FileWriter(folder+"\\"+className+"ServiceImpl.java");
             String repository=Utils.getFirstLetterToLowerCase(className)+"Repository";
             String variableName=Utils.getFirstLetterToLowerCase(className);
             String name=CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, table.getPrimaryKey().getName());
             String type=Utils.getType(table.getPrimaryKey().getType());
             printWriter=new PrintWriter(classEntity);
             printWriter.println("package "+Constant.project.getNamePackage()+".service.impl;\n");
             //import
             printWriter.println("import "+Constant.project.getNamePackage()+".entity."+className+";");
             printWriter.println("import "+Constant.project.getNamePackage()+".repository."+className+"Repository;");
             printWriter.println("import "+Constant.project.getNamePackage()+".service."+className+"Service;");
             printWriter.println("import org.slf4j.Logger;");
             printWriter.println("import org.slf4j.LoggerFactory;");
             printWriter.println("import org.springframework.beans.factory.annotation.Autowired;");
             printWriter.println("import org.springframework.stereotype.Service;");
             printWriter.println("import java.util.List;");
             printWriter.println("import java.util.Map;");
             printWriter.println("import java.util.Optional;");
             if(table.getAttributeList().stream().filter(f-> Utils.getType(f.getType()).equals("Date")).collect(Collectors.toList()).size()>0){
                 printWriter.println("import java.util.Date;");
             }
             printWriter.println();

             printWriter.println("@Service");
             printWriter.println("public class "+className+"ServiceImpl implements "+className+"Service{\n");
             printWriter.println();
             printWriter.println("\tprivate static final Logger LOGGER = LoggerFactory.getLogger("+className+"ServiceImpl.class);");
             printWriter.println();
             printWriter.println("\t@Autowired");
             printWriter.println("\tprivate "+className+"Repository "+repository+";\n");

             //create method insert
             printWriter.println("\t@Override");
             printWriter.println("\tpublic void insert("+className+" "+variableName+" ){");
             printWriter.println("\t\tLOGGER.debug(\">>>Insert()->"+variableName+":{}\","+variableName+");");
             printWriter.println("\t\ttry{");
             printWriter.println("\t\t\t"+repository+".save("+variableName+");");
             printWriter.println("\t\t}catch (Exception e){");
             printWriter.println("\t\t\tLOGGER.error(\"Exception: {}\",e);");
             printWriter.println("\t\t}");
             printWriter.println("}");

             //create method update
             printWriter.println("\t@Override");
             printWriter.println("\tpublic void update("+type+" "+name+", Map<String,Object> data){\n");
             printWriter.println("\t\tLOGGER.debug(\">>>> update->"+ name+": {}, "+variableName+": {}\","+name+",data);");
             printWriter.println("\t\ttry{");
             printWriter.println("\t\t\tOptional<"+className+"> "+variableName+"Optional = "+repository+".findById("+name+");");
             printWriter.println("\t\t\tif(!"+variableName+"Optional.isPresent()){");
             printWriter.println("\t\t\t\tthrow new Exception(\"No existe el registro\");");
             printWriter.println("\t\t\t}");
             for (Attribute attribute:table.getAttributeList()) {
                 String attributeName=CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, attribute.getName());
                 printWriter.println("\t\t\t//"+attributeName);
                 printWriter.println("\t\t\tif(data.containsKey(\""+attributeName+"\")){");
                 //hacer validacion de que tipo de dato para asi colcar los pasos
                 if(Utils.getType(attribute.getType()).equals("Integer")){
                     printWriter.println("\t\t\t\tInteger "+attributeName+" = (Integer)data.get(\""+attributeName+"\");");
                 }
                 if(Utils.getType(attribute.getType()).equals("String")){
                     printWriter.println("\t\t\t\tString "+attributeName+" = data.get(\""+attributeName+"\").toString();");
                 }
                 if(Utils.getType(attribute.getType()).equals("Date")){
                     printWriter.println("\t\t\t\tDate "+attributeName+" = (Date)data.get(\""+attributeName+"\");");
                 }
                 if(Utils.getType(attribute.getType()).equals("Double")){
                     printWriter.println("\t\t\t\tDouble "+attributeName+" = (Double)data.get(\""+attributeName+"\");");
                 }
                 if(Utils.getType(attribute.getType()).equals("Long")){
                     printWriter.println("\t\t\t\tLong "+attributeName+" = (Long)data.get(\""+attributeName+"\");");
                 }
                 printWriter.println("\t\t\t\t"+variableName+"Optional.get().set"+Utils.getFirstLetterToUpperCase(attributeName)+"("+attributeName+");");

                 printWriter.println("\t\t\t}");
             }
             printWriter.println("\t\t\t"+repository+".save("+variableName+"Optional.get()"+");");
             printWriter.println("\t\t}catch (Exception e){");
             printWriter.println("\t\t\tLOGGER.error(\"Exception: {}\",e);");
             printWriter.println("\t\t}");
             printWriter.println("\t}");

             //create method delete
             printWriter.println("\t@Override");
             printWriter.println("\tpublic void delete("+type+" "+name+"){");
             printWriter.println("\t\tLOGGER.debug(\">>>> delete->"+ name+": {}\","+name+");");
             printWriter.println("\t\ttry{");
             printWriter.println("\t\t\tOptional<"+className+"> "+variableName+"Optional = "+repository+".findById("+name+");");
             printWriter.println("\t\t\tif(!"+variableName+"Optional.isPresent()){");
             printWriter.println("\t\t\t\tthrow new Exception(\"No existe el registro\");");
             printWriter.println("\t\t\t}");
             printWriter.println("\t\t\t"+repository+".delete("+variableName+"Optional.get()"+");");
             printWriter.println("\t\t}catch (Exception e){");
             printWriter.println("\t\t\tLOGGER.error(\"Exception: {}\",e);");
             printWriter.println("\t\t}");
             printWriter.println("\t}");

             //create method finAll
             printWriter.println("\t@Override");
             printWriter.println("\tpublic List<"+className+"> findAll(){");
             printWriter.println("\t\tLOGGER.debug(\">>>> findAll <<<<\");");
             printWriter.println("\t\tList<"+className+">"+variableName+"List=null;");
             printWriter.println("\t\ttry{");
             printWriter.println("\t\t\t"+variableName+"List = "+repository+".findAll();");
             printWriter.println("\t\t\tif("+variableName+"List.isEmpty()){");
             printWriter.println("\t\t\t\tthrow new Exception(\"No existen datos\");");
             printWriter.println("\t\t\t}");
             printWriter.println("\t\t}catch (Exception e){");
             printWriter.println("\t\t\tLOGGER.error(\"Exception: {}\",e);");
             printWriter.println("\t\t}");
             printWriter.println("\t\treturn "+variableName+"List;");
             printWriter.println("\t}");

             printWriter.println();

             printWriter.println("}");
         }catch (Exception e){
             e.printStackTrace();
         } finally {
             try {
                 if (null != classEntity)
                     classEntity.close();
                } catch (Exception e2) {
                 e2.printStackTrace();
             }
         }
    }

    public void generateCorsConfiguration(){
        FileWriter corsConfiguration=null;
    }
}
