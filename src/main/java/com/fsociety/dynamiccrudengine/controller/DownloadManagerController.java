package com.fsociety.dynamiccrudengine.controller;

import com.fsociety.dynamiccrudengine.business.DownloadManagerBusiness;
import com.fsociety.dynamiccrudengine.model.Project;
import com.fsociety.dynamiccrudengine.utils.Constant;

public class DownloadManagerController {

    private final DownloadManagerBusiness downloadManagerBusiness;

    public DownloadManagerController(){
        downloadManagerBusiness=new DownloadManagerBusiness();
    }


    public void downloadProjectZip(Project project){
        Constant.project=project;
        downloadManagerBusiness.downloadProjectZip(project);
    }
}
