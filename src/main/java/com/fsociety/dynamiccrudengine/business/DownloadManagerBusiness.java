package com.fsociety.dynamiccrudengine.business;

import com.fsociety.dynamiccrudengine.model.Project;
import com.fsociety.dynamiccrudengine.utils.Utils;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadManagerBusiness {

    public void downloadProjectZip(Project project) throws Exception {
        String url = "https://start.spring.io/starter.zip?name="
                + project.getNameProject()
                + "&groupId=" + project.getGroupId()
                + "&artifactId=" + project.getArtifactId()
                + "&version=" + project.getVersion()
                + "&description=" + project.getDescription().replace(" ", "+")
                + "&packageName=" + project.getNamePackage()
                + "&type=maven-project&packaging=jar&javaVersion=1.8"
                + "&language=java&bootVersion=2.3.2.RELEASE"
                + "&dependencies=data-jpa&dependencies=mysql&dependencies=web";
        try {
            String nameFile = project.getNameProject() + ".zip";
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            InputStream in = conn.getInputStream();
            Utils.saveFile("tmp" + File.separator, nameFile, in);
            Utils.Descomprimir(Utils.obtenerRutaPorServidor() +
                    "tmp" + File.separator + nameFile, Utils.obtenerRutaPorServidor()
                    + "tmp/" + project.getNameProject());
        } catch (Exception e){
            throw new Exception(e);
        }
    }
}
