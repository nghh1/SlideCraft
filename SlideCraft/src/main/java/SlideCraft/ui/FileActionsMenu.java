package SlideCraft.ui;

import java.awt.*;


import javax.swing.*;

import SlideCraft.ui.action.ToolBarAction;

public class FileActionsMenu extends JPanel {

    static final int width = 90; 
    static final int height = 135; 
    public FileActionsMenu(){        
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(SlideCraftFrame.popupMenuColor);
        this.setOpaque(true);
        this.setVisible(true);
        JToolBar toolBar = new JToolBar();
        toolBar.setBackground(this.getBackground());
        toolBar.setPreferredSize(new Dimension(width, height));
        toolBar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolBar.setFloatable(false);
        toolBar.setForeground(Color.WHITE);
        JButton new_file = newToolBarButton("New File", ToolBarAction.NEW_FILE);
        JButton open_file = newToolBarButton("Open File", ToolBarAction.OPEN_FILE);
        JButton save_file = newToolBarButton("Save File", ToolBarAction.SAVE_FILE);
        JButton save_as_file = newToolBarButton("Save As", ToolBarAction.SAVE_AS_FILE);
        toolBar.add(new_file);
        toolBar.add(open_file);
        toolBar.add(save_file);
        toolBar.add(save_as_file);
        this.add(toolBar, new GridBagConstraints());
    }

    public JButton newToolBarButton(String name, ToolBarAction a){
        JButton new_button = new JButton(name);
        new_button.setBackground(Color.WHITE);
        new_button.addActionListener(a.getAction());
        return new_button;
    }

    
}
