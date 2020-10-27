package com.fsociety.dynamiccrudengine.controller;

import com.fsociety.dynamiccrudengine.business.GenerateSourceBusiness;

import javax.swing.table.DefaultTableModel;

public class TableSelectController {

    private final GenerateSourceBusiness generateSourceBusiness;

    public TableSelectController() {
        this.generateSourceBusiness=new GenerateSourceBusiness();
    }

    public void generateSource(DefaultTableModel modelTableSelect){
        this.generateSourceBusiness.generateSource(modelTableSelect);
    }
}
