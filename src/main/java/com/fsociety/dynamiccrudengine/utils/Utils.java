package com.fsociety.dynamiccrudengine.utils;

import com.fsociety.dynamiccrudengine.model.ForagingKey;
import com.fsociety.dynamiccrudengine.model.Table;
import org.apache.ant.compress.taskdefs.Unzip;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static String saveFile(String basepath, String filename, InputStream in) throws FileNotFoundException, IOException {
        File folderArchvo = new File(obtenerRutaPorServidor() + basepath);
        if (!folderArchvo.exists()) {
            folderArchvo.mkdirs();
        }
        String finalpath = obtenerRutaPorServidor() + basepath + normalizer(filename);
        final File file = new File(finalpath);
        try (OutputStream out = new FileOutputStream(file)) {
            IOUtils.copy(in, out);
        }
        return basepath + normalizer(filename);
    }

    public static String getFileRoute(String basepath, String filename) {
        return obtenerRutaPorServidor() + basepath + normalizer(filename);
    }

    public static String normalizer(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("-", " ")
                .replaceAll("/", " ")
                .replace("\\", " ");
    }

    public static InputStream getFileType(String ruta) throws FileNotFoundException {
        ruta = obtenerRutaParaObtenerArchivo(ruta);
        InputStream input = new FileInputStream(new File(ruta));
        return input;
    }

    public static String obtenerRutaParaObtenerArchivo(String ruta) {
        if(ruta.contains("/")){
            ruta = (ruta.contains("/")) ? ruta.replace("/", Utils.obtenerSeparadorRutaPorServidor()) : ruta;
        }
        if(ruta.contains("\\")){
            ruta = (ruta.contains("\\")) ? ruta.replace("\\", Utils.obtenerSeparadorRutaPorServidor()) : ruta;
        }
        return ruta;
    }

    public static void eliminarArchivo(String rutaArchivo) throws IOException {
        Files.deleteIfExists(Paths.get(obtenerRutaPorServidor() + rutaArchivo));
    }

    public static String obtenerRutaPorServidor() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return "C:\\dce\\";
        } else if (SystemUtils.IS_OS_LINUX) {
            return "/dce/";
        } else if (SystemUtils.IS_OS_MAC) {
            return "/dce/";
        }
        return "";
    }

    public static String obtenerSeparadorRutaPorServidor() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return "\\";
        } else if (SystemUtils.IS_OS_LINUX) {
            return "/";
        } else if (SystemUtils.IS_OS_MAC) {
            return "/";
        }
        return "\\";
    }

    public static void Descomprimir(String ficheroZip, String directorioSalida) {
        Unzip unzip=new Unzip();
        unzip.setSrc(new File(ficheroZip));
        File file=  new File(directorioSalida);
        if (!file.exists()) {
            file.mkdirs();
        }
        unzip.setDest(file);
        unzip.execute();
    }

    public static String getFirstLetterToUpperCase(String data){
        return data.substring(0, 1).toUpperCase() + data.substring(1);
    }
    public static String getFirstLetterToLowerCase(String data){
        return data.substring(0, 1).toLowerCase() + data.substring(1);
    }


    public static String getType(String type){
        String typeDataJava="";
        if(type.equals("CHAR")||
            type.equals("VARCHAR")||
            type.equals("BINARY")||
            type.equals("VARBINARY")||
            type.equals("TINYBLOB")||
            type.equals("TINYTEXT")||
            type.equals("BLOB")||
            type.equals("TEXT")||
            type.equals("MEDIUMBLOB")||
            type.equals("MEDIUMTEXT")||
            type.equals("LONGBLOB")||
            type.equals("LONGTEX")||
            type.equals("ENUM")||
            type.equals("LONGVARCHAR")){
            typeDataJava="String";
        }
        if (type.equals("INT")||
            type.equals("SMALLINT")||
            type.equals("MEDIUMINT")||
            type.equals("INTEGER")) {
            typeDataJava="Integer";
        }
        if(type.equals("BIGINT")){
            typeDataJava="Long";
        }
        if(type.equals("DATE")||
           type.equals("DATETIME")||
           type.equals("TIME")||
           type.equals("YEAR")||
           type.equals("TIMESTAMP")){
            typeDataJava="Date";
        }
        if(type.equals("DECIMAL")||
           type.equals("FLOAT")||
           type.equals("DOUBLE")){
            typeDataJava="Double";
        }

        return typeDataJava;
    }
}
