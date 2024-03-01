package SlideCraft.ui;

import java.awt.*;

import javax.swing.*;

import SlideCraft.ui.action.ToolBarAction;

public class DrawActionsMenu extends JPanel {

    static final int width = 100; 
    static final int height = 155; 
    private JComboBox<Integer> dropdown;
    public DrawActionsMenu(){        
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
        JButton pencil = newToolBarButton("Pencil", ToolBarAction.PENCIL);

        Integer[] options = new Integer[100]; 
        for (int i = 1; i < 100; i++){
            options[i-1] = i;
        }
 
        this.dropdown = new JComboBox<>(options); 
        this.dropdown.setPreferredSize(new Dimension(15, 15));
        this.dropdown.setSelectedItem(8);
        JButton eraser = newToolBarButton("Eraser", ToolBarAction.ERASER);
        JButton bucket = newToolBarButton("Bucket", ToolBarAction.COLOURBUCKET);
        JButton textbox = newToolBarButton("Textbox", ToolBarAction.TEXTBOX);
        toolBar.add(pencil);
        JLabel hint = new JLabel("Pencil width:");
        hint.setFont(new Font("SansSerif",Font.BOLD,10));
        hint.setForeground(Color.WHITE);
        toolBar.add(hint);
        toolBar.add(dropdown); 
        toolBar.add(eraser);
        toolBar.add(bucket);
        toolBar.add(textbox);
        this.add(toolBar, BorderLayout.NORTH);
    }

    public JButton newToolBarButton(String name, ToolBarAction a){
        JButton new_button = new JButton(name);
        new_button.setBackground(Color.WHITE);
        new_button.addActionListener(a.getAction());
        return new_button;
    }

    public int getPencilSize(){
        return (int)(this.dropdown.getSelectedItem());
    }
}
