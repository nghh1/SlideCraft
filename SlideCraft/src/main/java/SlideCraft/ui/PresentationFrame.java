package SlideCraft.ui;

import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.*;

import SlideCraft.xml.Slide;
import SlideCraft.xml.SlidePreview;

// Java code to display the screen size 
import java.awt.*; 

public class PresentationFrame extends JFrame implements KeyListener {

    private JPanel containerPanel;
    private ArrayList<SlidePreview> panels;
    private Timer timer;
    private int currentPanel;
    private int panelWidth;
    private int stepSize = 10; // Adjust for smoother or faster animation

    private boolean setPointerVisible = false; 

    public PresentationFrame(List<Slide> slides) {
        this.panels = new ArrayList<>();
        initUI(slides);
    }

    private void initUI(List<Slide> slides) {
        setTitle("Sliding Panel Transition Example");
        setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        setUndecorated(true);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addKeyListener(this);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel"); 
            getRootPane().getActionMap().put("Cancel", new AbstractAction(){ 
                public void actionPerformed(ActionEvent e){
                    dispose();
                    SlideCraftFrame.getInstance().refreshAll();
                }
        }
       );

       addMouseMotionListener(new MouseMotionListener() {
        public void mouseDragged(java.awt.event.MouseEvent e){}

        public void mouseMoved(java.awt.event.MouseEvent e){
            revalidate();
            repaint();
        }
       });

        containerPanel = new JPanel(null); // Use null layout for manual positioning
        panelWidth = getWidth();
        int i = 0;

        for(Slide slide: slides) {
            int xPos = 0;
            slide.setBounds(i * panelWidth, 0, slide.getWidth(), slide.getHeight());
            i++;
            try {
                SlidePreview s = new SlidePreview(slide, getWidth(), getHeight(), false);
                s.setBounds(xPos, 0, panelWidth, getHeight());
                xPos += panelWidth;
                containerPanel.add(s);
                panels.add(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        getContentPane().add(containerPanel, BorderLayout.CENTER);
        setVisible(true);
        revalidate();
        repaint();
    }
    
    private void startWestTransition(SlidePreview panel1, SlidePreview panel2) {
        // Timer to animate the transition
        timer = new Timer(3, new ActionListener() {
            private int xPos = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                xPos -= stepSize;
                if (xPos <= -panelWidth) {
                    timer.stop();
                }
                panel1.setBounds(xPos, 0, panelWidth, getHeight());
                panel2.setBounds(xPos + panelWidth, 0, panelWidth, getHeight());
            }
        });

        // Start the animation
        timer.start();
    }

    private void startEastTransition(SlidePreview panel1, SlidePreview panel2) {
        // Timer to animate the transition
        timer = new Timer(3, new ActionListener() {
            private int xPos = -panelWidth;

            @Override
            public void actionPerformed(ActionEvent e) {
                xPos += stepSize;
                if (xPos >= 0) {
                    timer.stop();
                }
                panel1.setBounds(xPos, 0, panelWidth, getHeight());
                panel2.setBounds(xPos + panelWidth, 0, panelWidth, getHeight());
            }
        });

        // Start the animation
        timer.start();
    }

    private void startNorthTransition(SlidePreview panel1, SlidePreview panel2) {
        // Timer to animate the transition
        timer = new Timer(3, new ActionListener() {
            private int yPos = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                yPos -= stepSize;
                if (yPos <= -getHeight()) {
                    timer.stop();
                }
                panel1.setBounds(0, yPos, panelWidth, getHeight());
                panel2.setBounds(0, yPos + getHeight(), panelWidth, getHeight());
            }
        });

        // Start the animation
        timer.start();
    }

    private void startSouthTransition(Slide panel1, Slide panel2) {
        // Timer to animate the transition
        // Timer to animate the transition
        timer = new Timer(3, new ActionListener() {
            private int yPos = -getHeight();

            @Override
            public void actionPerformed(ActionEvent e) {
                yPos += stepSize;
                if (yPos >= 0) {
                    timer.stop();
                }
                panel1.setBounds(0, yPos, panelWidth, getHeight());
                panel2.setBounds(0, yPos + getHeight(), panelWidth, getHeight());
            }
        });

        // Start the animation
        timer.start();
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        if(arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (timer == null || !timer.isRunning()) {
                if(currentPanel != panels.size() - 1) {
                    startWestTransition(panels.get(currentPanel), panels.get(currentPanel + 1));
                    currentPanel++;
                } else {
                    dispose();
                }
            }
        } else if(arg0.getKeyCode() == KeyEvent.VK_LEFT) {
            if (timer == null || !timer.isRunning()) {
                if(currentPanel != 0) {
                    startEastTransition(panels.get(currentPanel - 1), panels.get(currentPanel));
                    currentPanel--;
                }
            }
        } else if(arg0.getKeyCode() == KeyEvent.VK_P) {
            setPointerVisible = ! setPointerVisible;
            revalidate();
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {}

    @Override
    public void keyTyped(KeyEvent arg0) {}

    public void paint(Graphics g){
        super.paint(g);
        ((Graphics2D)g).drawString("Press P to enable pointer", 20, getHeight() - 20);
        ((Graphics2D)g).drawString("Press ESC to go back to Editing Mode", 20, getHeight() - 40);

        if (setPointerVisible){
            Point cursor = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(cursor, this);

            int pointerSize = 40;
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(1));
            g2.fillOval((int)cursor.getX()-pointerSize/2, (int)cursor.getY()-pointerSize/2, pointerSize, pointerSize);
        }
    }
}
