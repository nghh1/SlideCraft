package SlideCraft.xml;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

import javax.swing.*;

import SlideCraft.ui.SlideCraftFrame;

public class BarChart extends JPanel{
    private int[] data;
    private BarChart INSTANCE;
    private Point startPoint;
    public BarChart(int[] data){
        this.setLayout(new BorderLayout());
        this.setLocation(50,50);
        this.setSize(400,300);
        this.data = data;
        addListeners();
    }

    public void addChartPanel(){
        JPanel chartPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                int width = getWidth();
                int height = getHeight();

                int barWidth = (width-(data.length-1)*10)/data.length;
                int maxHeight = 0;
                for(int d: data){
                    maxHeight = Math.max(d, maxHeight);
                }
                for(int i=0;i<data.length;i++){
                    int barHeight = (int) (((double)data[i]/maxHeight)*(height-20));
                    int x = i*(barWidth+10);
                    int y = height-barHeight;
                    g.setColor(Color.BLUE);
                    g.fillRect(x,y,barWidth,barHeight);
                    g.setColor(Color.BLACK);
                    g.drawRect(x,y,barWidth,barHeight);
                    g.drawString(String.valueOf(data[i]),x-5+barWidth/2,y-5);
                }
                setOpaque(true);
            }
        };
        this.add(chartPanel, BorderLayout.CENTER);
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
        JMenuItem remove = new JMenuItem("remove bar chart");
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