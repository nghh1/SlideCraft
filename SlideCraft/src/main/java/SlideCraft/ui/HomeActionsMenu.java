package SlideCraft.ui;
import java.awt.*;

import javax.swing.*;

import SlideCraft.ui.action.ToolBarAction;

public class HomeActionsMenu extends JPanel {
    static final int width = 110; 
    static final int height = 200; 
    public HomeActionsMenu(){        
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
        // add content below
        JButton fontButton = newToolBarButton("Font", ToolBarAction.FONT);
        JButton colourButton = newToolBarButton("Colour", ToolBarAction.COLOUR);
        JButton boldButton = newToolBarButton("Bold", ToolBarAction.BOLD);
        JButton italicButton = newToolBarButton("Italic", ToolBarAction.ITALIC);
        JButton underlineButton = newToolBarButton("Underline", ToolBarAction.UNDERLINE);  
        //JButton textSpacing = newToolBarButton("Text Spacing", ToolBarAction.TEXT_SPACING);
        JButton bulletedList = newToolBarButton("Bulleted list", ToolBarAction.BULLETEDLIST);
        JButton numberedList = newToolBarButton("Numbered list", ToolBarAction.NUMBEREDLIST);
        JButton listCancel = newToolBarButton("Cancel list", ToolBarAction.LISTCANCEL);
        toolBar.add(fontButton);
        toolBar.add(colourButton);
        toolBar.add(boldButton);
        toolBar.add(italicButton);
        toolBar.add(underlineButton);
        //toolBar.add(textSpacing);
        toolBar.add(bulletedList);
        toolBar.add(numberedList);
        toolBar.add(listCancel);
        this.add(toolBar);
    }

    public JButton newToolBarButton(String name, ToolBarAction a){
        JButton new_button = new JButton(name);
        new_button.setBackground(Color.WHITE);
        new_button.addActionListener(a.getAction());
        return new_button;
    }

}
