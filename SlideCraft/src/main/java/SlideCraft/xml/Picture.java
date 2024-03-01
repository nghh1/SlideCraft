package SlideCraft.xml;

import javax.swing.*;
import javax.swing.border.Border;

import SlideCraft.ui.SlideCraftFrame;

import java.awt.*;
import java.awt.event.*;

public class Picture extends JLabel implements MouseListener {

    public void mousePressed(java.awt.event.MouseEvent arg0) {
        ((Slide)getParent()).startPoint = SwingUtilities.convertPoint(this, arg0.getPoint(), this.getParent());

        if(arg0.getButton() == MouseEvent.BUTTON3) {
            Component[] componentList = ((Slide)getParent()).getComponents();
            for(Component c: componentList) {
                if(c == ((Slide)getParent()).selectedComponent) {
                    remove(c);
                    validate();
                    repaint();
                }
            }
        } else {
            ((Slide)getParent()).selectedComponent = this;
            Border blackline = BorderFactory.createLineBorder(Color.black);
            this.setBorder(blackline);
        }
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent arg0) {
        Picture label = new Picture();
        for(Component component: ((Slide)getParent()).getComponents()) {
            if(component == ((Slide)getParent()).selectedComponent) {
                if(component instanceof Picture) {
                    label = (Picture) component;
                }
            }
        }

        if(((Slide)getParent()).endPoint != null && ((Slide)getParent()).componentDirection == 1) {
            ImageIcon icon = (ImageIcon) label.getIcon();
            Image image = icon.getImage(); // transform it 
            double yDifference = ((Slide)getParent()).startPoint.getY() - ((Slide)getParent()).endPoint.getY();
            if(icon.getIconHeight() + yDifference > 100) {
                Image newimg = image.getScaledInstance(icon.getIconWidth(), icon.getIconHeight() + (int) yDifference,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
                icon = new ImageIcon(newimg);  // transform it back 
            } else {
                Image newimg = image.getScaledInstance(icon.getIconWidth(), 100,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
                icon = new ImageIcon(newimg);  // transform it back
            }
            label.setIcon(icon);
            label.setSize(icon.getIconWidth(), icon.getIconHeight());
            label.setLocation(label.getX(), (int) ((Slide)getParent()).endPoint.getY());
        } else if(((Slide)getParent()).endPoint != null && ((Slide)getParent()).componentDirection == 2) {
            ImageIcon icon = (ImageIcon) label.getIcon();
            Image image = icon.getImage(); // transform it 
            double xDifference = ((Slide)getParent()).startPoint.getX() - ((Slide)getParent()).endPoint.getX();
            double yDifference = ((Slide)getParent()).startPoint.getY() - ((Slide)getParent()).endPoint.getY();
            if(icon.getIconHeight() + yDifference > 100 && icon.getIconWidth() + xDifference > 100) {
                Image newimg = image.getScaledInstance(icon.getIconWidth() + (int) xDifference, icon.getIconHeight() + (int) yDifference,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
                icon = new ImageIcon(newimg);  // transform it back 
            } else {
                Image newimg = image.getScaledInstance(100, 100,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
                icon = new ImageIcon(newimg);  // transform it back
            }
            label.setIcon(icon);
            label.setSize(icon.getIconWidth(), icon.getIconHeight());
            label.setLocation((int) ((Slide)getParent()).endPoint.getX(), (int) ((Slide)getParent()).endPoint.getY());
        } else if(((Slide)getParent()).endPoint != null && ((Slide)getParent()).componentDirection == 3) {
            ImageIcon icon = (ImageIcon) label.getIcon();
            Image image = icon.getImage(); // transform it 
            double xDifference = ((Slide)getParent()).startPoint.getX() - ((Slide)getParent()).endPoint.getX();
            if(icon.getIconWidth() + xDifference > 100) {
                Image newimg = image.getScaledInstance(icon.getIconWidth() + (int) xDifference, icon.getIconHeight(),  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
                icon = new ImageIcon(newimg);  // transform it back 
            } else {
                Image newimg = image.getScaledInstance(100, icon.getIconHeight(),  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
                icon = new ImageIcon(newimg);  // transform it back
            }
            label.setIcon(icon);
            label.setSize(icon.getIconWidth(), icon.getIconHeight());
            label.setLocation((int) ((Slide)getParent()).endPoint.getX(), label.getY());
        } else if(((Slide)getParent()).endPoint != null && ((Slide)getParent()).componentDirection == 4) {
            ImageIcon icon = (ImageIcon) label.getIcon();
            Image image = icon.getImage(); // transform it 
            double xDifference = ((Slide)getParent()).startPoint.getX() - ((Slide)getParent()).endPoint.getX();
            double yDifference = ((Slide)getParent()).startPoint.getY() - ((Slide)getParent()).endPoint.getY();
            if(icon.getIconWidth() + xDifference > 100 && icon.getIconHeight() - yDifference > 100) {
                Image newimg = image.getScaledInstance(icon.getIconWidth() + (int) xDifference, icon.getIconHeight() - (int) yDifference,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
                icon = new ImageIcon(newimg);  // transform it back 
            } else {
                Image newimg = image.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
                icon = new ImageIcon(newimg);  // transform it back
            }
            label.setIcon(icon);
            label.setSize(icon.getIconWidth(), icon.getIconHeight());
            label.setLocation((int) ((Slide)getParent()).endPoint.getX(), label.getY());
        } else if(((Slide)getParent()).endPoint != null && ((Slide)getParent()).componentDirection == 5) {
            ImageIcon icon = (ImageIcon) label.getIcon();
            Image image = icon.getImage(); // transform it                        
            double yDifference = ((Slide)getParent()).endPoint.getY() - ((Slide)getParent()).startPoint.getY();
            if(icon.getIconHeight() + yDifference > 100) {
                Image newimg = image.getScaledInstance(icon.getIconWidth(), icon.getIconHeight() + (int) yDifference,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
                icon = new ImageIcon(newimg);  // transform it back 
            } else {
                Image newimg = image.getScaledInstance(icon.getIconWidth(), 100,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
                icon = new ImageIcon(newimg);  // transform it back
            }
            label.setIcon(icon);
            label.setSize(icon.getIconWidth(), icon.getIconHeight());
            //label.setLocation(label.getX(), (int) endPoint.getY() - icon.getIconHeight());
        } else if(((Slide)getParent()).endPoint != null && ((Slide)getParent()).componentDirection == 6) {
            ImageIcon icon = (ImageIcon) label.getIcon();
            Image image = icon.getImage(); // transform it 
            double xDifference = ((Slide)getParent()).startPoint.getX() - ((Slide)getParent()).endPoint.getX();
            int xToAdd = (int) xDifference;
            double yDifference = ((Slide)getParent()).startPoint.getY() - ((Slide)getParent()).endPoint.getY();
            int yToAdd = (int) yDifference;
            if(icon.getIconWidth() + xToAdd > 100 && icon.getIconHeight() + yToAdd > 100) {
                Image newimg = image.getScaledInstance(icon.getIconWidth() + xToAdd, icon.getIconHeight() + yToAdd,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
                icon = new ImageIcon(newimg);  // transform it back 
            } else {
                Image newimg = image.getScaledInstance(100, 100,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
                icon = new ImageIcon(newimg);  // transform it back
            }
            label.setIcon(icon);
            label.setSize(icon.getIconWidth(), icon.getIconHeight());
        } else if(((Slide)getParent()).endPoint != null && ((Slide)getParent()).componentDirection == 7) {
            ImageIcon icon = (ImageIcon) label.getIcon();
            Image image = icon.getImage(); // transform it 
            double xDifference = ((Slide)getParent()).endPoint.getX() - ((Slide)getParent()).startPoint.getX();
            int xToAdd = (int) xDifference;
            if(icon.getIconWidth() + xToAdd > 100) {
                Image newimg = image.getScaledInstance(icon.getIconWidth() + xToAdd, icon.getIconHeight(),  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
                icon = new ImageIcon(newimg);  // transform it back 
            } else {
                Image newimg = image.getScaledInstance(100, icon.getIconHeight(),  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
                icon = new ImageIcon(newimg);  // transform it back
            }
            label.setIcon(icon);
            label.setSize(icon.getIconWidth(), icon.getIconHeight());
        } else if(((Slide)getParent()).endPoint != null && ((Slide)getParent()).componentDirection == 8) {
            ImageIcon icon = (ImageIcon) label.getIcon();
            Image image = icon.getImage(); // transform it 
            double xDifference = ((Slide)getParent()).endPoint.getX() - ((Slide)getParent()).startPoint.getX();
            int xToAdd = (int) xDifference;
            double yDifference = ((Slide)getParent()).endPoint.getY() - ((Slide)getParent()).startPoint.getY();
            int yToAdd = (int) yDifference;
            System.out.println(((Slide)getParent()).startPoint);
            System.out.println(((Slide)getParent()).endPoint);
            if(icon.getIconWidth() + xToAdd > 100 && icon.getIconHeight() + yToAdd > 100) {
                Image newimg = image.getScaledInstance(icon.getIconWidth() + xToAdd, icon.getIconHeight() + yToAdd,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
                icon = new ImageIcon(newimg);  // transform it back 
            } else {
                Image newimg = image.getScaledInstance(100, 100,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
                icon = new ImageIcon(newimg);  // transform it back
            }
            label.setIcon(icon);
            label.setSize(icon.getIconWidth(), icon.getIconHeight());
        } 

        ((Slide)getParent()).startPoint = null;
        this.setBorder(null);
        ((Slide)getParent()).selectedComponent = null;
        ((Slide)getParent()).componentDirection = 0;
        ((Slide)getParent()).endPoint = null;
    } 

    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}



    public Picture(ImageIcon icon){
        super(icon);
        this.setSize(icon.getIconWidth(), icon.getIconHeight());
        this.setLocation(new Point(this.getX() + (this.getWidth() / 5), this.getY() + (this.getHeight() / 5)));
        SlideCraftFrame.getInstance().getModel().getSlides().getHelper().placedComponent(SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide(), this);

        addMouseListener(this);
    }

    public Picture(){
        super();
    }
    
}
