package SlideCraft.ui;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

public class ToolBarButton extends JButton implements MouseListener {
    public ToolBarButton(String name, JPanel popupMenu){
        this.setText(name);
        this.setPreferredSize(new Dimension(60,30));
        this.setBackground(Color.WHITE);
        this.setFocusPainted(false);
        if ( popupMenu != null ){
            this.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    SlideCraftFrame.getInstance().refreshToolBarPopups(popupMenu);
                }
            });
        }
    }

    public void mouseClicked(MouseEvent e) {
        //this.setBorder(BorderFactory.createLineBorder(SlideCraftFrame.selectColor, 2));
    }
    public void mousePressed(MouseEvent e) {
        //this.setBorder(BorderFactory.createLineBorder(SlideCraftFrame.selectColor, 2));
    }
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {
        //this.setBorder(BorderFactory.createLineBorder(SlideCraftFrame.selectColor, 2));
    }
    public void mouseExited(MouseEvent e) {}

}
