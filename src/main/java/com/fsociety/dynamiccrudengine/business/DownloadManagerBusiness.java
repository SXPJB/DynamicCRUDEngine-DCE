package com.fsociety.dynamiccrudengine.business;

import com.fsociety.dynamiccrudengine.model.Project;
import com.fsociety.dynamiccrudengine.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadManagerBusiness {


    public void downloadProjectZip(Project project){
        String url="https://start.spring.io/starter.zip?name="+project.getNameProject()
                +"&groupId="+project.getGroupId()
                +"&artifactId="+project.getArtifactId()
                +"&version="+project.getVersion()
                +"&description="+project.getDescription().replace(" ","+")
                +"&packageName="+project.getNamePackage()
                +"&type=maven-project&packaging=jar&javaVersion=1.8"
                +"&language=java&bootVersion=2.3.2.RELEASE&dependencies=data-jpa&dependencies=mysql&dependencies=web";
        String nameFile=project.getNameProject()+".zip";
        try{
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            System.out.println("\nempezando descarga: \n");
            System.out.println(">> URL: " + url);
            System.out.println(">> Nombre: " + nameFile);
            System.out.println(">> tamaño: " + conn.getContentLength() + " bytes");
            InputStream in = conn.getInputStream();
            Utils.saveFile("tmp"+ File.separator,nameFile,in);
            System.out.println(Utils.obtenerRutaPorServidor()+"tmp"+File.separator+nameFile);
            Utils.Descomprimir(Utils.obtenerRutaPorServidor()+"tmp"+File.separator+nameFile,Utils.obtenerRutaPorServidor()+"tmp/"+project.getNameProject());
        } catch (MalformedURLException e) {
            System.out.println("la url: " + url + " no es valida!");
        } catch (ConnectException e) {
            System.out.println("Verifica tu conexcion a internet");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
