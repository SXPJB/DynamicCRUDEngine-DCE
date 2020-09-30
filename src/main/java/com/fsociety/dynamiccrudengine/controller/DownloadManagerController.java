package com.fsociety.dynamiccrudengine.controller;

import com.fsociety.dynamiccrudengine.business.DownloadManagerBusiness;
import com.fsociety.dynamiccrudengine.model.Project;

public class DownloadManagerController {

    private DownloadManagerBusiness downloadManagerBusiness;

    public DownloadManagerController(){
        downloadManagerBusiness=new DownloadManagerBusiness();
    }


    public void downloadProjectZip(Project project){
        downloadManagerBusiness.downloadProjectZip(project);
    }
}
