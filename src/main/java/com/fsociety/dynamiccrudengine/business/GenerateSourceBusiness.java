package com.fsociety.dynamiccrudengine.business;

import com.fsociety.dynamiccrudengine.model.Attribute;
import com.fsociety.dynamiccrudengine.model.Table;
import com.fsociety.dynamiccrudengine.utils.Constant;
import com.fsociety.dynamiccrudengine.utils.Utils;
import com.google.common.base.CaseFormat;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

            //Generate Endpoint form tables select
            i=0;
            folder=new File(path+"\\endpoint");
            folder.mkdir();
            while (i<modelTableSelect.getRowCount()){
                Table table = tableList.stream().filter(f -> f.getName().equals(modelTableSelect.getValueAt(i,0))).collect(Collectors.toList()).get(0);
                generateEndpoint(table,folder);
                i++;
            }

            folder=new File(path+"\\config");
            folder.mkdir();
            generateCorsConfiguration(folder);
            generateSwaggerConfig(folder);
            generateResponseBody(folder);
            generateUtils(folder);
            path= Utils.obtenerRutaPorServidor()+"tmp\\"+
                    Constant.project.getNameProject()+"\\src\\main\\resources";
            folder=new File(path);
            generateProperties(folder);
            path= Utils.obtenerRutaPorServidor()+"tmp\\"+
                    Constant.project.getNameProject()+"\\pom.xml";
            File file=new File(path);
            addDependencies(file);
        }catch (Exception e){
            e.printStackTrace();
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
            printWriter.println("\tvoid insert("+className+" "+Utils.getFirstLetterToLowerCase(className)+") throws Exception;");
            printWriter.println("\tvoid update("+type+" "+name+", Map<String,Object> data) throws Exception;");
            printWriter.println("\tvoid delete("+type+" "+name+") throws Exception;");
            printWriter.println("\tList<"+className+"> findAll() throws Exception;");
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
             if(table.getAttributeList().stream().filter(f -> Utils.getType(f.getType()).equals("Date")).count() >0){
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
             printWriter.println("\tpublic void insert("+className+" "+variableName+" ) throws Exception{");
             printWriter.println("\t\tLOGGER.debug(\">>>Insert()->"+variableName+":{}\","+variableName+");");
             printWriter.println("\t\ttry{");
             printWriter.println("\t\t\t"+repository+".save("+variableName+");");
             printWriter.println("\t\t}catch (Exception e){");
             printWriter.println("\t\t\tLOGGER.error(\"Exception: {}\",e);");
             printWriter.println("\t\t\tthrow new Exception(e);");
             printWriter.println("\t\t}");
             printWriter.println("\t}");

             //create method update
             printWriter.println("\t@Override");
             printWriter.println("\tpublic void update("+type+" "+name+", Map<String,Object> data) throws Exception{\n");
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
             printWriter.println("\t\t\tthrow new Exception(e);");
             printWriter.println("\t\t}");
             printWriter.println("\t}");

             //create method delete
             printWriter.println("\t@Override");
             printWriter.println("\tpublic void delete("+type+" "+name+") throws Exception{");
             printWriter.println("\t\tLOGGER.debug(\">>>> delete->"+ name+": {}\","+name+");");
             printWriter.println("\t\ttry{");
             printWriter.println("\t\t\tOptional<"+className+"> "+variableName+"Optional = "+repository+".findById("+name+");");
             printWriter.println("\t\t\tif(!"+variableName+"Optional.isPresent()){");
             printWriter.println("\t\t\t\tthrow new Exception(\"No existe el registro\");");
             printWriter.println("\t\t\t}");
             printWriter.println("\t\t\t"+repository+".delete("+variableName+"Optional.get()"+");");
             printWriter.println("\t\t}catch (Exception e){");
             printWriter.println("\t\t\tLOGGER.error(\"Exception: {}\",e);");
             printWriter.println("\t\t\tthrow new Exception(e);");
             printWriter.println("\t\t}");
             printWriter.println("\t}");

             //create method finAll
             printWriter.println("\t@Override");
             printWriter.println("\tpublic List<"+className+"> findAll() throws Exception{");
             printWriter.println("\t\tLOGGER.debug(\">>>> findAll <<<<\");");
             printWriter.println("\t\tList<"+className+">"+variableName+"List=null;");
             printWriter.println("\t\ttry{");
             printWriter.println("\t\t\t"+variableName+"List = "+repository+".findAll();");
             printWriter.println("\t\t}catch (Exception e){");
             printWriter.println("\t\t\tLOGGER.error(\"Exception: {}\",e);");
             printWriter.println("\t\t\tthrow new Exception(e);");
             printWriter.println("\t\t}");
             printWriter.println("\t\tLOGGER.debug(\">>>> findAll <<<< "+ variableName+"List: {}\","+variableName+"List);");
             printWriter.println("\t\treturn "+variableName+"List;");
             printWriter.println("\t}\n");
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

    private void generateEndpoint(Table table,File folder){
        FileWriter classEntity=null;
        PrintWriter printWriter=null;
        System.out.println("Se genera Endpoint");
        try {
            String className= CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL,table.getName());
            classEntity=new FileWriter(folder+"\\"+className+"Endpoint.java");
            String service=Utils.getFirstLetterToLowerCase(className)+"Service";
            String variableName=Utils.getFirstLetterToLowerCase(className);
            String name=CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, table.getPrimaryKey().getName());
            String type=Utils.getType(table.getPrimaryKey().getType());
            printWriter=new PrintWriter(classEntity);
            printWriter.println("package "+Constant.project.getNamePackage()+".endpoint;\n");
            printWriter.println("import "+Constant.project.getNamePackage()+".entity."+className+";");
            printWriter.println("import "+Constant.project.getNamePackage()+".service."+className+"Service;");
            printWriter.println("import "+Constant.project.getNamePackage()+".config.ResponseBody;\n" +
                                "import "+Constant.project.getNamePackage()+".config.Utils;\n" +
                                "import org.slf4j.Logger;\n" +
                                "import org.slf4j.LoggerFactory;\n" +
                                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                                "import org.springframework.http.HttpStatus;\n" +
                                "import org.springframework.http.ResponseEntity;\n" +
                                "import org.springframework.web.bind.annotation.*;\n"+
                                "import java.util.Map;\n"+
                                "import java.util.List;");
            printWriter.println("@RestController");
            printWriter.println("@RequestMapping(\""+variableName+"\")");
            printWriter.println("public class "+className+"Endpoint{\n");
            printWriter.println();
            printWriter.println("\tprivate static final Logger LOGGER = LoggerFactory.getLogger("+className+"Endpoint.class);");
            printWriter.println();
            printWriter.println("\t@Autowired");
            printWriter.println("\tprivate "+className+"Service "+service+";\n");
            printWriter.println();
            printWriter.println("\t@PostMapping(\"/insert\")");
            printWriter.println("\tpublic ResponseEntity<ResponseBody<Void>> insert(@RequestBody "+className+" "+variableName+"){");
            printWriter.println("\t\tLOGGER.debug(\">>>Insert()->"+variableName+":{}\","+variableName+");");
            printWriter.println("\t\tResponseEntity<ResponseBody<Void>> response=null;");
            printWriter.println("\t\ttry{");
            printWriter.println("\t\t\t"+service+".insert("+variableName+");");
            printWriter.println("\t\t\tresponse= Utils.<Void>response(HttpStatus.CREATED,\"Se inserto el registro\",null);");
            printWriter.println("\t\t}catch (Exception e){");
            printWriter.println("\t\t\tresponse=Utils.<Void>response(HttpStatus.BAD_REQUEST,false,\"No se puedo insertar el registro\",null);");
            printWriter.println("\t\t}");
            printWriter.println("\treturn response;");
            printWriter.println("\t}");
            printWriter.println();
            printWriter.println("\t@PostMapping(\"/update/{"+name+"}\")");
            printWriter.println("\tpublic ResponseEntity<ResponseBody<Void>> update(@PathVariable "+type+" "+name+", @RequestBody Map<String,Object> data){");
            printWriter.println("\t\tLOGGER.debug(\">>>> update->"+ name+": {}, "+variableName+": {}\","+name+",data);");
            printWriter.println("\t\tResponseEntity<ResponseBody<Void>> response=null;");
            printWriter.println("\t\ttry{");
            printWriter.println("\t\t\t"+service+".update("+name+",data);");
            printWriter.println("\t\t\tresponse= Utils.<Void>response(HttpStatus.OK,\"Se actualizo el registro\",null);");
            printWriter.println("\t\t}catch (Exception e){");
            printWriter.println("\t\t\tresponse=Utils.<Void>response(HttpStatus.BAD_REQUEST,false,\"No se puedo insertar el registro\",null);");
            printWriter.println("\t\t}");
            printWriter.println("\treturn response;");
            printWriter.println("\t}");
            printWriter.println();
            printWriter.println("\t@GetMapping(\"/delete/{"+name+"}\")");
            printWriter.println("\tpublic ResponseEntity<ResponseBody<Void>> delete(@PathVariable "+type+" "+name+"){");
            printWriter.println("\t\tLOGGER.debug(\">>>> delete->"+ name+": {}\","+name+");");
            printWriter.println("\t\tResponseEntity<ResponseBody<Void>> response=null;");
            printWriter.println("\t\ttry{");
            printWriter.println("\t\t\t"+service+".delete("+name+");");
            printWriter.println("\t\t\tresponse= Utils.<Void>response(HttpStatus.OK,\"Se actualizo el registro\",null);");
            printWriter.println("\t\t}catch (Exception e){");
            printWriter.println("\t\t\tresponse=Utils.<Void>response(HttpStatus.BAD_REQUEST,false,\"No se puedo insertar el registro\",null);");
            printWriter.println("\t\t}");
            printWriter.println("\treturn response;");
            printWriter.println("\t}");
            printWriter.println();
            printWriter.println("\t@GetMapping(\"/findAll\")");
            printWriter.println("\tpublic ResponseEntity<ResponseBody<List<"+className+">>> findAll(){");
            printWriter.println("\t\tLOGGER.debug(\">>>> findAll <<<<\");");
            printWriter.println("\t\tResponseEntity<ResponseBody<List<"+className+">>> response=null;");
            printWriter.println("\t\tList<"+className+">"+variableName+"List=null;");
            printWriter.println("\t\ttry{");
            printWriter.println("\t\t\t"+variableName+"List="+service+".findAll();");
            printWriter.println("\t\t\tresponse=Utils.<List<"+className+">>response(HttpStatus.OK,\"Lista enonctrda\","+variableName+"List);");
            printWriter.println("\t\t}catch (Exception e){");
            printWriter.println("\t\t\tresponse=Utils.<List<"+className+">>response(HttpStatus.NOT_FOUND,\"Lista enonctrda\","+variableName+"List);");
            printWriter.println("\t\t}");
            printWriter.println("\t\treturn response;");
            printWriter.println("\t}");
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

    private void generateCorsConfiguration(File folder){
        FileWriter corsConfiguration=null;
        PrintWriter printWriter=null;
        try {
            String className="CorsConfiguration";
            corsConfiguration=new FileWriter(folder+"\\"+className+".java");
            printWriter=new PrintWriter(corsConfiguration);
            printWriter.println("package "+Constant.project.getNamePackage()+".config;");
            printWriter.println("import org.springframework.context.annotation.Bean;");
            printWriter.println("import org.springframework.context.annotation.Configuration;");
            printWriter.println("import org.springframework.web.servlet.config.annotation.CorsRegistry;");
            printWriter.println("import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;");
            printWriter.println();
            printWriter.println("@Configuration");
            printWriter.println("public class CorsConfiguration {");
            printWriter.println("\t@Bean");
            printWriter.println("\tpublic WebMvcConfigurer corsConfigurer() {");
            printWriter.println("\t\treturn new WebMvcConfigurer() {");
            printWriter.println("\t\t\t@Override");
            printWriter.println("\t\t\tpublic void addCorsMappings(CorsRegistry registry) {");
            printWriter.println("\t\t\t\tregistry.addMapping(\"/**\").allowedOrigins(\"*\");");
            printWriter.println("\t\t\t}");
            printWriter.println("\t\t};");
            printWriter.println("\t}");
            printWriter.println("}");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (null != corsConfiguration)
                    corsConfiguration.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void generateSwaggerConfig(File folder){
        FileWriter swaggerConfig=null;
        PrintWriter printWriter;
        try {
            String className="SwaggerConfig";
            swaggerConfig=new FileWriter(folder+"\\"+className+".java");
            printWriter=new PrintWriter(swaggerConfig);

            printWriter.println("package "+Constant.project.getNamePackage()+".config;");
            printWriter.println("import org.springframework.context.annotation.Bean;\n" +
                    "import org.springframework.context.annotation.Configuration;\n" +
                    "import springfox.documentation.builders.ApiInfoBuilder;\n" +
                    "import springfox.documentation.builders.PathSelectors;\n" +
                    "import springfox.documentation.builders.RequestHandlerSelectors;\n" +
                    "import springfox.documentation.service.ApiInfo;\n" +
                    "import springfox.documentation.spi.DocumentationType;\n" +
                    "import springfox.documentation.spring.web.plugins.Docket;\n" +
                    "import springfox.documentation.swagger2.annotations.EnableSwagger2;");
            printWriter.println();
            printWriter.println("@Configuration");
            printWriter.println("@EnableSwagger2");
            printWriter.println("public class SwaggerConfig {");
            printWriter.println("\t@Bean");
            printWriter.println("\tpublic Docket api() {");
            printWriter.println("\t\treturn new Docket(DocumentationType.SWAGGER_2)\n" +
                    "\t\t\t.select()\n" +
                    "\t\t\t.apis(RequestHandlerSelectors.any())\n" +
                    "\t\t\t.paths(PathSelectors.any())\n" +
                    "\t\t\t.build()\n" +
                    "\t\t\t.apiInfo(metaData());");
            printWriter.println("\t}");
            printWriter.println("\tprivate static ApiInfo metaData() {");
            printWriter.println("\t\treturn new ApiInfoBuilder()\n" +
                    "\t\t\t.title(\""+Constant.project.getNameProject()+"\")\n" +
                    "\t\t\t.description(\""+Constant.project.getDescription()+"\")\n" +
                    "\t\t\t.version(\""+Constant.project.getVersion()+"\")\n"+
                    " \t\t\t.build();");
            printWriter.println("\t}");
            printWriter.println("}");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (null != swaggerConfig)
                    swaggerConfig.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void generateProperties(File folder){
        FileWriter applicationProperties=null;
        PrintWriter printWriter=null;
        try {
            applicationProperties=new FileWriter(folder+"\\application.properties");
            printWriter=new PrintWriter(applicationProperties);
            printWriter.println("#Configracion de conexcion a base de datos");
            printWriter.println("spring.datasource.url="+Constant.urlConnexion);
            printWriter.println("spring.datasource.username="+Constant.project.getUserDB());
            printWriter.println("spring.datasource.password="+Constant.project.getPassDB());
            printWriter.println("#Configuracion de log");
            printWriter.println("logging.level."+Constant.project.getNamePackage()+"=debug");
            printWriter.println("# Mostrar o no registrar para cada consulta SQL");
            printWriter.println("spring.jpa.show-sql = true");
            printWriter.println("# Naming strategy\n" +
                    "spring.jpa.hibernate.naming-strategy = org.hibernate.cfg.ImprovedNamingStrategy\n" +
                    "\n" +
                    "# Use spring.jpa.properties.* for Hibernate native properties (the prefix is\n" +
                    "# stripped before adding them to the entity manager)\n" +
                    "\n" +
                    "# The SQL dialect makes Hibernate generate better SQL for the chosen database\n" +
                    "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5Dialect");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (null != applicationProperties)
                    applicationProperties.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void addDependencies(File file){
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            Utils.addElement(doc,new String[]{"io.springfox","springfox-swagger2","2.9.2"});
            Utils.addElement(doc,new String[]{"io.springfox","springfox-swagger-ui","2.9.2"});
            //write the updated document to file or console
            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        } catch (SAXException | ParserConfigurationException | IOException | TransformerException e1) {
            e1.printStackTrace();
        }
    }

    private void generateResponseBody(File folder) {
        FileWriter responseBody=null;
        PrintWriter printWriter=null;
        try {
            String className="ResponseBody";
            responseBody=new FileWriter(folder+"\\"+className+".java");
            printWriter=new PrintWriter(responseBody);
            printWriter.println("package "+Constant.project.getNamePackage()+".config;");

            printWriter.println("public class "+className+"<T> {");
            printWriter.println("\tprivate boolean success;\n" +
                                "\tprivate  T data;\n" +
                                "\tprivate String message;");
            printWriter.println("\tpublic ResponseBody() {\n" +
                    "\t\tthis.success = true;\n" +
                    "\t}");
            printWriter.println("\tpublic ResponseBody(T data) {\n" +
                    "\t\tthis.success = true;\n" +
                    "\t\tthis.data = data;\n" +
                    "\t}");
            printWriter.println("\tpublic ResponseBody(T data, String message) {\n" +
                    "\t\tthis.success = true;\n" +
                    "\t\tthis.data = data;\n" +
                    "\t\tthis.message = message;\n" +
                    " \t}");
            printWriter.println("//false");
            printWriter.println("\tpublic ResponseBody(boolean success) {\n" +
                    "\t\tthis.success = success;\n" +
                    "\t}");
            printWriter.println("\tpublic ResponseBody(boolean success, T data) {\n" +
                    "\t\tthis.success = success;\n" +
                    "\t\tthis.data = data;\n" +
                    "\t}");
            printWriter.println("\tpublic ResponseBody(boolean success, T data, String message) {\n" +
                    "\t\tthis.success = success;\n" +
                    "\t\tthis.data = data;\n" +
                    "\t\tthis.message = message;\n" +
                    "\t}");
            printWriter.println(
                    "\tpublic boolean isSuccess() {\n" +
                    "\t\treturn success;\n" +
                    "\t}\n" +
                    "\n" +
                    "\tpublic void setSuccess(boolean success) {\n" +
                    "\t\tthis.success = success;\n" +
                    "\t}\n" +
                    "\n" +
                    "\tpublic T getData() {\n" +
                    "\t\treturn data;\n" +
                    "\t}\n" +
                    "\n" +
                    "\tpublic void setData(T data) {\n" +
                    "\t\tthis.data = data;\n" +
                    "\t}\n" +
                    "\n" +
                    "\tpublic String getMessage() {\n" +
                    "\t\treturn message;\n" +
                    "\t}\n" +
                    "\n" +
                    "\tpublic void setMessage(String message) {\n" +
                    "\t\tthis.message = message;\n" +
                    "\t}");
            printWriter.println("}");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (null != responseBody)
                    responseBody.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void generateUtils(File folder) {
        FileWriter utils=null;
        PrintWriter printWriter=null;
        try {
            String className="Utils";
            utils=new FileWriter(folder+"\\"+className+".java");
            printWriter=new PrintWriter(utils);
            printWriter.println("package "+Constant.project.getNamePackage()+".config;");
            printWriter.println("import org.springframework.http.HttpStatus;\n" +
                                "import org.springframework.http.ResponseEntity;");
            printWriter.println("public class "+className+"{\n");
            printWriter.println("\t//true\n" +
                                "\tpublic static <T> ResponseEntity<ResponseBody<T>> response(HttpStatus status, String msg, T data) { \n");
            printWriter.println("\t\tResponseBody<T> body = new ResponseBody<>(data,msg);");
            printWriter.println("\t\treturn new ResponseEntity<>(body, status);" +
                                "\n" +
                                "\t}");
            printWriter.println("\t//false\n" +
                                "\tpublic static <T> ResponseEntity<ResponseBody<T>> response(HttpStatus status, boolean success, String msg, T data) { \n");
            printWriter.println("\t\tResponseBody<T> body = new ResponseBody<T>(success, data, msg);");
            printWriter.println("\t\treturn new ResponseEntity<>(body, status);"+
                                "\n" +
                                "\t}");
            printWriter.println("\n}");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (null != utils)
                    utils.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

}