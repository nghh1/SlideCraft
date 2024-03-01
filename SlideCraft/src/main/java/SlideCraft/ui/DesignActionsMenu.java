package SlideCraft.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import SlideCraft.ui.action.ToolBarAction;

public class DesignActionsMenu extends JPanel {

    static final int width = 140; 
    static final int height = 260; 

    public DesignActionsMenu(){        
        this.setLayout(new BorderLayout());
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
        
        JButton title_Content = newToolBarButton("Title&paragraph", ToolBarAction.TITLE_CONTENT);
        JButton title_TwoContents = newToolBarButton("Title&2paragraphs", ToolBarAction.TITLE_2CONTENTS);
        JButton twoContents = newToolBarButton("2 paragraphs", ToolBarAction.TWO_CONTENTS);
        JButton transitionNorth = newToolBarButton("Transition North", ToolBarAction.TRANSITION_NORTH);
        JButton transitionSouth = newToolBarButton("Transition South", ToolBarAction.TRANSITION_SOUTH);
        JButton transitionEast = newToolBarButton("Transition East", ToolBarAction.TRANSITION_EAST);
        JButton transitionWest = newToolBarButton("Transition West", ToolBarAction.TRANSITION_WEST);
        JButton removeTransition = newToolBarButton("Remove Transition", ToolBarAction.REMOVE_TRANSITION);
        
        toolBar.add(title_Content);
        toolBar.add(title_TwoContents);
        toolBar.add(twoContents);
        toolBar.add(transitionNorth);
        toolBar.add(transitionSouth);
        toolBar.add(transitionEast);
        toolBar.add(transitionWest);
        toolBar.add(removeTransition);
        this.add(toolBar, BorderLayout.NORTH);
    }

    public JButton newToolBarButton(String name, ToolBarAction a){
        JButton new_button = new JButton(name);
        new_button.setBackground(Color.WHITE);
        new_button.addActionListener(a.getAction());
        return new_button;
    }

    
}