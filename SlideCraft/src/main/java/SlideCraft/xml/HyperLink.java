package SlideCraft.xml;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.*;

import SlideCraft.ui.SlideCraftFrame;

public class HyperLink extends JLabel{
    private HyperLink INSTANCE;
    private Point startPoint;
    public HyperLink(String url){
        setText(url);
        setSize(new Dimension(getFontMetrics(getFont()).stringWidth(url), getFontMetrics(getFont()).getHeight()));
        setLocation(10,50);
        setForeground(Color.BLUE);
        addListeners(url);
    }

    public void addListeners(String url){
        this.INSTANCE = this;
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                startPoint = SwingUtilities.convertPoint(INSTANCE, e.getPoint(), (Slide)getParent());
            }
            @Override
            public void mouseClicked(MouseEvent ev){
                if(ev.getButton()==MouseEvent.BUTTON1){
                    try{
                        if(Desktop.isDesktopSupported()==true && Desktop.getDesktop()!=null && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
                            Desktop.getDesktop().browse(new URI(url));
                        }else{
                            JOptionPane.showMessageDialog(SlideCraftFrame.getInstance(),"URL not valid","URL error",JOptionPane.ERROR_MESSAGE);
                        }
                    }catch(NullPointerException | IOException | URISyntaxException e){
                        JOptionPane.showMessageDialog(SlideCraftFrame.getInstance(),"URL not valid","URL error",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
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
            }
        });

        JPopupMenu menu = new JPopupMenu();
        JMenuItem remove = new JMenuItem("remove hyperlink");
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
    
}
