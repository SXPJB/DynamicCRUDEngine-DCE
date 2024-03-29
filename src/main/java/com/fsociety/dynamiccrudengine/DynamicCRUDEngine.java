package com.fsociety.dynamiccrudengine;

import com.fsociety.dynamiccrudengine.view.InitialWindowView;
import javax.swing.SwingUtilities;

public class DynamicCRUDEngine {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InitialWindowView().setVisible(true));
    }
}
