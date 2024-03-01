package SlideCraft.xml;


import javax.swing.*;

import SlideCraft.ui.SlideCraftFrame;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Shape extends JComponent implements MouseListener{
    public static int borderWidth = 4;
    protected int x, y, width, height;
    protected Color colour = Color.BLACK;
    protected Color fillColor = null;
    protected JPopupMenu popup;
    protected transient MouseMotionListener dragListener;

    private Point dragStart = null;
    private Point dragEnd = null;
    
    public void mouseClicked(MouseEvent e) {
        if (SlideCraftFrame.getInstance().bucketUsed()){
            fillColor = SlideCraftFrame.getInstance().getBucket();
            ((Slide)getParent()).wasModified();
            revalidate();
            repaint();
        }
    }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {
        if (e.isMetaDown()){
            showPopup(e);
        }
    }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    
    public Shape(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        addMouseListener(this);
        setPreferredSize(new Dimension(width, height));
        setVisible(true);
        setOpaque(true);
        setBounds(x, y, width, height);
        addListeners();
    }

    private void updatePosition(){
        this.x += dragEnd.getX() - dragStart.getX();
        this.y += dragEnd.getY() - dragStart.getY();
        setBounds(x, y, width, height);
        Slide selectSlide = ((Slide)getParent());
        revalidate();
        repaint();
        selectSlide.revalidate();
        selectSlide.repaint();
        selectSlide.wasModified();
    }

    private void showPopup(MouseEvent e){
        this.popup.show(e.getComponent(), e.getX(), e.getY());
    }

    public void addListeners(){
        this.dragListener = new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                if (dragStart == null){
                    dragStart = new Point(e.getX(), e.getY());
                }
                else{
                    dragEnd = new Point(e.getX(), e.getY());
                    updatePosition();
                }
            }
            public void mouseMoved(MouseEvent e){
                if (dragEnd != null){
                    dragStart = null;
                    dragEnd = null;
                }
            }
        };

        this.addMouseMotionListener(this.dragListener);

        this.popup = new JPopupMenu();
        // add menu items to popup
        this.popup.add(new JMenuItem("Remove")).addActionListener( event -> {
            Slide selectSlide = ((Slide)getParent());
            selectSlide.remove(this);
            selectSlide.revalidate();
            selectSlide.repaint();
            selectSlide.wasModified();
        });

        this.popup.add(new JMenuItem("Colour")).addActionListener( event -> {
            Color newColour = JColorChooser.showDialog(this, "Choose circle colour", colour);
            if(newColour != null) {
                colour = newColour;
                fillColor = newColour;
            }
            repaint();
            revalidate();
            ((Slide)getParent()).wasModified();
        });
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject();
        addListeners();
    }
}