package SlideCraft.xml;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

import javax.swing.*;

import SlideCraft.ui.SlideCraftFrame;

public class LineGraph extends JLayeredPane {
    private double[] data;
    private LineGraph INSTANCE;
    private Point startPoint;
    public LineGraph(double[] data){
        this.data = data;
        addListeners();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        int padding = 50;
        //draw x-axis
        g.drawLine(padding, height-padding, width-padding, height-padding);
        //draw y-axis
        g.drawLine(padding, padding, padding, height-padding);
        //draw data points
        double xScale = (double) (width-2*padding)/(data.length-1);
        double yScale = (double) (height-2*padding)/getMax(data);
        g.setColor(Color.BLACK);
        for(int i=0;i<data.length;i++){
            int x = (int) (padding+i*xScale);
            int y = (int) (height-padding-data[i]*yScale-13);
            g.fillOval(x-2,y-2, 4, 4);
            if(i>0){
                int previousX = (int) (padding+(i-1)*xScale);
                int previousY = (int) (height-padding-data[i-1]*yScale-13);
                g.drawLine(previousX, previousY, x, y);
            }
        }
        //Draw x-axis ticks and labels
        for(int i=0;i<data.length;i++){
            int x = (int) (padding+i*xScale);
            g.drawLine(x, height-padding, x, height-padding+5);
            g.drawString(Integer.toString(i), x-5, height-padding+20);
        }
        //Draw y-axis ticks and labels
        int yTickscount = getMax(data)/getMin(data);
        for(int i=1;i<yTickscount-1;i++){
            int y = height-padding-(int)(i*((double)(height-2*padding)/5));
            int label = (int)(i*((double)getMax(data)/5));
            if(y>padding){
                g.drawLine(padding-5, y, padding, y);
                g.drawString(Integer.toString(label), padding-20, y+5);
            }
        }
        setOpaque(true);
    }

    private int getMax(double[] data){
        int max = Integer.MIN_VALUE;
        for(int i=0;i<data.length;i++){
            max = (int)Math.max(max,data[i]);
        }
        return max;
    }
    private int getMin(double[] data){
        int min = Integer.MAX_VALUE;
        for(int i=0;i<data.length;i++){
            min = (int) Math.min(min,data[i]);
        }
        return min;
    }

    public void addListeners(){
        this.INSTANCE = this;
        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                startPoint = SwingUtilities.convertPoint(INSTANCE, e.getPoint(), (Slide)getParent());
            }
            public void mouseReleased(MouseEvent e){
                startPoint = null;
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter(){
            public void mouseDragged(MouseEvent e){
                if(startPoint!=null){
                    Point location = SwingUtilities.convertPoint(INSTANCE, e.getPoint(), (Slide)getParent());
                    if(((Slide)getParent()).getBounds().contains(location)){
                        Point newLocation = INSTANCE.getLocation();
                        newLocation.translate(location.x-startPoint.x,location.y-startPoint.y);
                        newLocation.x = Math.max(newLocation.x, 0);
                        newLocation.y = Math.max(newLocation.y, 0);
                        newLocation.x = Math.min(newLocation.x,((Slide)getParent()).getWidth()-INSTANCE.getWidth());
                        newLocation.y = Math.min(newLocation.y,((Slide)getParent()).getHeight()-INSTANCE.getHeight());
                        INSTANCE.setLocation(newLocation);
                        startPoint = location;
                    }
                }
                INSTANCE.revalidate();
                INSTANCE.repaint();
                SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().revalidate();
                SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().repaint();
                SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().wasModified();
            }
        });

        JPopupMenu menu = new JPopupMenu();
        JMenuItem remove = new JMenuItem("remove line graph");
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                Slide s = (Slide)getParent();
                s.remove(INSTANCE);
                s.revalidate();
                s.repaint();
                s.wasModified();
            }
        });
        menu.add(remove);
        this.INSTANCE.setComponentPopupMenu(menu);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject();
        addListeners();
    }

}
