package com.fsociety.dynamiccrudengine;

import com.fsociety.dynamiccrudengine.view.InitialWindowView;
import javax.swing.*;

public class DynamicCRUDEngine {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new InitialWindowView().setVisible(true);
            }
        });
    }

}
