package com.fsociety.dynamiccrudengine.utils;

import com.fsociety.dynamiccrudengine.model.Project;
import org.apache.ant.compress.taskdefs.Unzip;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.tools.zip.ZipEntry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;

public class Utils {
    public static String saveFile(String basepath, String filename, InputStream in) throws IOException {
        File folderArchvo = new File(obtenerRutaPorServidor() + basepath);
        if (!folderArchvo.exists()) {
            folderArchvo.mkdirs();
        }
        String finalpath = obtenerRutaPorServidor() + basepath + filename;
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
        if (ruta.contains("/")) {
            ruta = (ruta.contains("/")) ? ruta.replace("/", Utils.obtenerSeparadorRutaPorServidor()) : ruta;
        }
        if (ruta.contains("\\")) {
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
    public static String obtenerRutaRaizPorOS() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return "C:\\dce";
        } else if (SystemUtils.IS_OS_LINUX) {
            return "/dce";
        } else if (SystemUtils.IS_OS_MAC) {
            return "/dce";
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
        Unzip unzip = new Unzip();
        unzip.setSrc(new File(ficheroZip));
        File file = new File(directorioSalida);
        if (!file.exists()) {
            file.mkdirs();
        }
        unzip.setDest(file);
        unzip.execute();
    }

    public static void comprimir(String directorio, String directorioSalidaZip) {
        ZipOutputStream outputStream = null;
        try {
            outputStream = new ZipOutputStream(new FileOutputStream(new File(directorioSalidaZip)));
            zipFile(outputStream, new File(directorio), "");
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                outputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void zipFile(ZipOutputStream output, File file, String basePath) {
        FileInputStream input = null;
        try {
            // file CONTENTS
            if (file.isDirectory()) {
                // current CONTENTS lose face in the file table columns
                File[] list = file.listFiles();
                basePath = basePath + (basePath.length() == 0 ? "" : "/")
                        + file.getName();
                // recursive loop cycle for each file compression
                for (File f : list) {
                    zipFile(output, f, basePath);
                }
            } else {
                // Compressed file
                basePath = (basePath.length() == 0 ? "" : basePath + "/")
                        + file.getName();
                output.putNextEntry(new ZipEntry(basePath));
                input = new FileInputStream(file);
                int readLen = 0;
                byte[] buffer = new byte[1024 * 8];
                while ((readLen = input.read(buffer, 0, 1024 * 8)) != -1) {
                    output.write(buffer, 0, readLen);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // close the stream
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static String getFirstLetterToUpperCase(String data) {
        return data.substring(0, 1).toUpperCase() + data.substring(1);
    }

    public static String getFirstLetterToLowerCase(String data) {
        return data.substring(0, 1).toLowerCase() + data.substring(1);
    }

    public static void addElement(Document doc, String[] content) {
        NodeList dependencies = doc.getElementsByTagName("dependencies");
        Element dependencyNew = null;

        dependencyNew = (Element) dependencies.item(0);

        Element dependency = doc.createElement("dependency");
        dependency.appendChild(doc.createTextNode(" "));
        dependencyNew.appendChild(dependency);

        Element group = doc.createElement("groupId");
        group.appendChild(doc.createTextNode(content[0]));
        dependency.appendChild(group);

        Element artifactId = doc.createElement("artifactId");
        artifactId.appendChild(doc.createTextNode(content[1]));
        dependency.appendChild(artifactId);

        Element version = doc.createElement("version");
        version.appendChild(doc.createTextNode(content[2]));
        dependency.appendChild(version);
    }


    public static String getType(String type) {
        String typeDataJava = "";
        if (type.equals("CHAR") ||
                type.equals("VARCHAR") || type.equals("BINARY") ||
                type.equals("VARBINARY") || type.equals("TINYBLOB") ||
                type.equals("TINYTEXT") || type.equals("BLOB") ||
                type.equals("TEXT") || type.equals("MEDIUMBLOB") ||
                type.equals("MEDIUMTEXT") || type.equals("LONGBLOB") ||
                type.equals("LONGTEXT") || type.equals("ENUM") ||
                type.equals("LONGVARCHAR")) {
            typeDataJava = "String";
        }
        if (type.equals("INT") || type.equals("SMALLINT") ||
                type.equals("MEDIUMINT") || type.equals("INTEGER")) {
            typeDataJava = "Integer";
        }
        if (type.equals("BIGINT")) {
            typeDataJava = "Long";
        }
        if (type.equals("DATE") || type.equals("DATETIME") ||
                type.equals("TIME") || type.equals("YEAR") ||
                type.equals("TIMESTAMP")) {
            typeDataJava = "Date";
        }
        if (type.equals("DECIMAL") || type.equals("FLOAT") ||
                type.equals("DOUBLE")) {
            typeDataJava = "Double";
        }
        if (type.equals("BIT") || type.equals("TINYINT")) {
            typeDataJava = "Boolean";
        }
        return typeDataJava;
    }

    private static void removeFolderR(File folder) {
        if (!folder.exists()) { return; }

        if (folder.isDirectory()) {
            for (File f : folder.listFiles()) {
                removeFolderR(f);  }
        }
        folder.delete();
    }

    public static void removeTempProject(){
        String ruta=obtenerRutaRaizPorOS();
        removeFolderR(new File(ruta));
        Constant.project=new Project();
    }
}
