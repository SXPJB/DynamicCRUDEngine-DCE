package com.fsociety.dynamiccrudengine.view;

import com.fsociety.dynamiccrudengine.controller.TableSelectController;
import com.fsociety.dynamiccrudengine.model.Table;
import com.fsociety.dynamiccrudengine.utils.Constant;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TableSelectView extends JFrame implements ActionListener {

    private JLabel titleLabel;
    private JLabel subTitleLabel;
    private JLabel titleTableLabel;
    private JLabel titleTable2Label;
    private JScrollPane scrollPane1;
    private JScrollPane scrollPane2;
    private JScrollPane scrollPane3;
    private JTable jTableAvailable;
    private JTable jTableSelect;
    private JButton selectButton;
    private JButton removeButton;
    private JButton  nextButton;
    private JButton cancelButton;
    private DefaultTableModel modelSelect;
    private DefaultTableModel modelAvailable;

    private final TableSelectController tableSelectController;

    public TableSelectView(){
        this.tableSelectController=new TableSelectController();
        Constant.configuratorWindows(this);
        initialComponents();
    }

    public void initialComponents() {
        titleLabel = new JLabel();
        titleLabel.setText("Dynamic CRUD Engine");
        titleLabel.setFont(Constant.fontTitle);
        titleLabel.setForeground(Constant.colorFont);
        titleLabel.setBounds(100, 38, 400, 43);
        subTitleLabel = new JLabel();
        subTitleLabel.setText("Tablas resultantes");
        subTitleLabel.setFont(Constant.fontSubtitle);
        subTitleLabel.setForeground(Constant.colorFont);
        subTitleLabel.setBounds(210, 101, 200, 30);
        titleTableLabel=new JLabel();
        titleTableLabel.setText("En las siguientes tablas no se puede generar el CRUD");
        titleTableLabel.setFont(Constant.fontLabels);
        titleTableLabel.setForeground(Constant.colorFont);
        titleTableLabel.setBounds(60,150,550,30);

        selectButton=new JButton();
        selectButton.setText("Seleccionar");
        selectButton.setBounds(245,526,110,35);
        selectButton.setFont(Constant.fontbutton);
        selectButton.setForeground(Constant.colorFont);
        selectButton.setBackground(Constant.buutonColor);
        selectButton.addActionListener(this);

        removeButton=new JButton();
        removeButton.setText("Remover");
        removeButton.setBounds(245,474,110,35);
        removeButton.setFont(Constant.fontbutton);
        removeButton.setForeground(Constant.colorFont);
        removeButton.setBackground(Constant.buutonColor);
        removeButton.addActionListener(this);

        //ScrollPanel for table one
        scrollPane1=new JScrollPane(noSelectTable());
        scrollPane1.setBounds(25,184,550,110);

        titleTable2Label=new JLabel();
        titleTable2Label.setText("Seleccione las tablas de las que desea realizar el CRUD");
        titleTable2Label.setFont(Constant.fontLabels);
        titleTable2Label.setForeground(Constant.colorFont);
        titleTable2Label.setBounds(60,342,550,30);

        //ScrollPanel for table two
        jTableAvailable=availableTables();
        scrollPane2=new JScrollPane(jTableAvailable);
        scrollPane2.setBounds(25,411,200,188);

        //ScrollPanel for table three
        jTableSelect=SelectTables();
        scrollPane3=new JScrollPane(jTableSelect);
        scrollPane3.setBounds(376,411,200,188);

        //button
        nextButton=new JButton();
        nextButton.setText("Generar proyecto");
        nextButton.setFont(Constant.fontLabels);
        nextButton.setForeground(Constant.colorFont);
        nextButton.setBackground(Constant.buutonColor);
        nextButton.setBounds(375,650,200,42);
        nextButton.addActionListener(this);

        cancelButton=new JButton();
        cancelButton.setText("Cancelar");
        cancelButton.setFont(Constant.fontLabels);
        cancelButton.setForeground(Constant.colorFont);
        cancelButton.setBackground(Constant.buttonColorSecondary);
        cancelButton.setBounds(40,650,165,42);
        cancelButton.addActionListener(this);

        this.add(titleLabel);
        this.add(subTitleLabel);
        this.add(titleTableLabel);
        this.add(scrollPane1);
        this.add(titleTable2Label);
        this.add(scrollPane2);
        this.add(scrollPane3);
        this.add(selectButton);
        this.add(removeButton);
        this.add(nextButton);
        this.add(cancelButton);
    }


    public JTable availableTables(){
        List<Table> tablesSelect= Constant.tablesList.get("listSelect");
        JTable jTable=null;
        try {
            String[] columnNames = {"Nombre de la tabla"};

            modelAvailable = new DefaultTableModel(null, columnNames);
            jTable=new JTable(modelAvailable);
            jTable.setBounds(30, 40, 200, 300);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment( JLabel.LEFT );
            jTable.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
            for (Table table:tablesSelect){
                Object[] nuevafila = {table.getName()};
                modelAvailable.addRow(nuevafila);
            }
            resizeColumnWidth(jTable);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jTable;
    }

    public JTable SelectTables(){
        JTable jTable=null;
        try {
            String[] columnNames = {"Nombre de la tabla"};
            modelSelect = new DefaultTableModel(null, columnNames);
            jTable=new JTable(modelSelect);
            jTable.setBounds(30, 40, 200, 300);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment( JLabel.LEFT );
            jTable.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
            resizeColumnWidth(jTable);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jTable;
    }



    public JTable noSelectTable(){
        JTable jTable=null;
        try {
            List<Table> tablesNoSelect= Constant.tablesList.get("listNoSelect");
            String[] columnNames = {"Nombre de la tabla"};
            final DefaultTableModel modelo;
            modelo = new DefaultTableModel(null, columnNames);
            jTable=new JTable(modelo);
            jTable.setBounds(30, 40, 200, 300);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment( JLabel.CENTER );
            jTable.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
            for (Table table:tablesNoSelect){
                Object[] row={table.getName()};
                modelo.addRow(row);
            }
            resizeColumnWidth(jTable);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jTable;
    }
    public void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 300)
                width=300;
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        UIManager.put("OptionPane.background",Constant.backgroundColor);
        UIManager.put("Panel.background",Constant.backgroundColor);
        UIManager.put("OptionPane.messageForeground",Constant.colorFont);

        int row=0;
        Object[] tableName=null;
        if(e.getSource()==selectButton){
            if(jTableAvailable.getSelectedRow()==-1){
                JOptionPane.showMessageDialog(this,"Requere seleccionar una tabla","Error",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            row=jTableAvailable.getSelectedRow();
            tableName= new Object[]{jTableAvailable.getModel().getValueAt(row, 0)};
            modelSelect.addRow(tableName);
            modelAvailable.removeRow(row);
        }
        if(e.getSource()==removeButton){
            if(jTableSelect.getModel().getRowCount()==0){
                JOptionPane.showMessageDialog(this,"Para remover al menos se requere una tabla seleccionada","Error",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if(jTableSelect.getSelectedRow()==-1){
                JOptionPane.showMessageDialog(this,"Para remover al menos se requere seleccionar una tabla","Error",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            row=jTableSelect.getSelectedRow();
            System.out.println(row);
            tableName= new Object[]{jTableSelect.getModel().getValueAt(row, 0)};
            modelSelect.removeRow(row);
            modelAvailable.addRow(tableName);
        }

        if(e.getSource()==nextButton){

            if(modelSelect.getRowCount()==0){
                JOptionPane.showMessageDialog(this,"Para continuar al menos se requere tener seleccionada una tabla","Error",JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int intput=JOptionPane.showOptionDialog(this,"¿Esta seguro de conitnuar?\n Está acción no se puede revertir", "Cuadro de confirmacion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Si","No"},"Si");
            if(intput== JOptionPane.YES_OPTION){
                tableSelectController.generateSource(modelSelect);
                tableSelectController.saveProject(this);
            }
        }
        if(e.getSource()==cancelButton){
            DataBaseConnectionView view=new DataBaseConnectionView();
            view.setVisible(true);
            view.setLocationRelativeTo(this.getParent());
            this.setVisible(false);
        }
    }
}