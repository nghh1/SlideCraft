package SlideCraft.xml;

import java.util.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

import java.awt.MouseInfo;

import javax.sound.sampled.LineEvent.Type;

import SlideCraft.ui.SlideCraftFrame;
import SlideCraft.ui.action.DraggableResizableTextArea;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Cursor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.awt.event.*;
 
public class Slide extends JLayeredPane implements MouseListener, Cloneable {
    protected transient Slides parent;
    protected double scale;
    public boolean isSelected = false;
    
    protected int transition;
    protected int layer;
    public JComponent selectedComponent;
    public Point startPoint;
    public Point endPoint;
    public int componentDirection;
    private JPopupMenu popup;

    private boolean drawing;
    private boolean erasing;
    private boolean playingAudio;

    private Clip audioClip;

    // private Rectangle[] rect = new Rectangle[10];
    // private int numOfRecs = 0;
    // private int currentSquareIndex = -1;

    // point and size
    private HashMap<Point, Tuple<Integer, Color>> pencilDrawing = new HashMap<Point, Tuple<Integer, Color>>(); 
    private java.awt.event.MouseEvent drawingEvent;
    private transient Drawing drawingListener;
    private Integer pencilSize;
    private JTextArea speakerNotes;

    public Slide(Slides parent) {
        this.layer = 1;
        this.parent = parent;
        this.setBorder(null);
        this.setBackground(Color.WHITE);
        this.setOpaque(true);
        this.setVisible(true);
        this.addMouseListener(this);
        this.drawingListener = new Drawing();
        this.addMouseMotionListener(this.drawingListener);
        this.popup = new JPopupMenu();
        // add menu items to popup
        this.popup.add(new JMenuItem("Remove")).addActionListener( event -> {
            SlideCraftFrame.getInstance().removeSlide();
        });
        this.popup.add(new JMenuItem("Duplicate")).addActionListener( event -> {
            SlideCraftFrame.getInstance().addSlide(true);
        });
        this.speakerNotes = new JTextArea("Speaker notes");
        this.speakerNotes.setBackground(new Color(200, 200, 200));
    }
    //Standard clone() method to perform Shallow Cloning
	@Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void mouseClicked(MouseEvent e) {
        if (!isSelected){
            this.select();
        }   
        for (Component c: getComponents()){
            if ( c instanceof DraggableResizableTextArea ){
                ((DraggableResizableTextArea)c).unselect();
            }
        }

        if (SlideCraftFrame.getInstance().bucketUsed()){
            this.setBackground(SlideCraftFrame.getInstance().getBucket());
        }

        SlideCraftFrame.getInstance().getSpeakerNotesPanel().removeAll();
        SlideCraftFrame.getInstance().getSpeakerNotesPanel().revalidate();
        SlideCraftFrame.getInstance().getSpeakerNotesPanel().repaint();
        SlideCraftFrame.getInstance().getSpeakerNotesPanel().add(this.speakerNotes);
    }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {
        if (!isSelected){
            this.select();
        }  
        showPopup(e);
    }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    private void showPopup(MouseEvent e){
        if(e.isMetaDown()){
          this.popup.show(e.getComponent(), e.getX(), e.getY());
        } 
    }

    public void setParent(Slides parent){
        this.parent = parent;
    }

    public boolean isSelected(){
        return this.isSelected;
    }

    public int getTransition() {
        return this.transition;
    }

    public void removeTransition() {
        this.transition = 0;
    }

    public void setTransitionNorth() {
        this.transition = 1;
    }
    
    public void setTransitionSouth() {
        this.transition = 2;
    }

    public void setTransitionEast() {
        this.transition = 3;
    }

    public void setTransitionWest() {
        this.transition = 4;
    }

    //Resize size when slider move
    public void setSize(double scale){
        int width = (int) (12*scale);
        int height = (int) (8*scale);
        this.setPreferredSize(new Dimension(width,height));
        this.revalidate();
        this.repaint();
    }

    public synchronized void select(){
        setBorder( BorderFactory.createLineBorder(SlideCraftFrame.selectColor, 2) );
        parent.setSelected(this);
        isSelected = true;
        wasModified();
    }

    public synchronized void unselect(){
        this.setBorder(null);
        this.isSelected = false;
        wasModified();
    }

    public void wasModified(){
        this.revalidate();
        this.repaint();
        parent.updatePreview(this);
    }

    public void setDrawing(){
        this.drawing = true;
        this.erasing = false;
    }

    public boolean pencilUsed(){
        return this.drawing;
    }

    public boolean eraserUsed(){
        return this.erasing;
    }

    public void dropPencil(){
        drawingEvent = null;
        this.drawing = false;
    }

    public void dropEraser(){
        this.erasing = false;
        revalidate();
        repaint();
    }

    public void setErasing(){
        this.erasing = true;
        this.drawing = false;
    }

    public JTextArea getSpeakerNotes(){
        return this.speakerNotes;
    }

    public void addImage() {
        JFileChooser openImageFileChooser = new JFileChooser();
        openImageFileChooser.setCurrentDirectory(new File("C:\\"));
        openImageFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
        openImageFileChooser.setFileFilter(new FileNameExtensionFilter("JPEG Images", "jpeg"));

        int returnValue = openImageFileChooser.showOpenDialog(SlideCraftFrame.getInstance());

        BufferedImage originalBI = null;

        if(returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                originalBI = ImageIO.read(openImageFileChooser.getSelectedFile());
            } catch (Exception e2) {}
        } else {
            return;
        }

        BufferedImage newBI = new BufferedImage(50, 50, BufferedImage.TYPE_3BYTE_BGR);

        try {
            newBI = resizeImage(originalBI, 250, 120);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageIcon icon = new ImageIcon(newBI);
        Picture picture = new Picture(icon);

        picture.addMouseMotionListener(new ComponentDragged());

        this.add(picture, layer);
        this.layer += 1;

        JPopupMenu menu = new JPopupMenu();
        JMenuItem remove = new JMenuItem("Remove Picture");
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                remove(picture);
                revalidate();
                repaint();
                wasModified();
            }
        });
        menu.add(remove);
        picture.setComponentPopupMenu(menu);

        wasModified();
    }

    public void addSound() {
        JFileChooser openAudioFileChooser = new JFileChooser();
        openAudioFileChooser.setCurrentDirectory(new File("C:\\"));
        openAudioFileChooser.setFileFilter(new FileNameExtensionFilter("WAV Files", "wav"));
        int returnValue = openAudioFileChooser.showOpenDialog(SlideCraftFrame.getInstance());

        if(returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(openAudioFileChooser.getSelectedFile());
                audioClip = AudioSystem.getClip();
                audioClip.open(audioIn);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            return;
        }

        JButton audioButton = new JButton();

        audioClip.addLineListener(new LineListener() {
            @Override
            public void update(final LineEvent event) {
                if (event.getType().equals(Type.STOP)) {
                    playingAudio = false;
                    audioClip.stop();
                    audioClip.setMicrosecondPosition(0);
                    ImageIcon playIcon = new ImageIcon("./SlideCraft/src/main/resources/resourses/images/Play.png");
                    audioButton.setIcon(playIcon);
                }
            }
        });

        audioButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent arg0) {
                startPoint = SwingUtilities.convertPoint(audioButton, arg0.getPoint(), audioButton.getParent());

                selectedComponent = audioButton;
                Border blackline = BorderFactory.createLineBorder(Color.black);
                audioButton.setBorder(blackline);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent arg0) {
                startPoint = null;
                audioButton.setBorder(null);
                selectedComponent = null;
            } 
        });

        audioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
                if(playingAudio) {
                    playingAudio = false;
                    if(audioClip != null) {
                        audioClip.stop();
                    }
                    ImageIcon playIcon = new ImageIcon("./SlideCraft/src/main/resources/resourses/images/Play.png");
                    audioButton.setIcon(playIcon);
                } else {
                    playingAudio = true;
                    if(audioClip != null) {
                        if(!audioClip.isRunning()) {
                            audioClip.start();
                        }
                    }
                    ImageIcon pauseIcon = new ImageIcon("./SlideCraft/src/main/resources/resourses/images/pause.png");
                    audioButton.setIcon(pauseIcon);
                }
            } 
        });

        audioButton.addMouseMotionListener(new ComponentDragged());
        audioButton.setSize(75, 75);
        audioButton.setBackground(Color.RED);
        ImageIcon playIcon = new ImageIcon("./SlideCraft/src/main/resources/resourses/images/play.png");
        audioButton.setIcon(playIcon);
        
        this.add(audioButton, layer);
        layer++;

        JPopupMenu menu = new JPopupMenu();
        JMenuItem remove = new JMenuItem("Remove Audio");
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                remove(audioButton);
                revalidate();
                repaint();
                wasModified();
            }
        });
        menu.add(remove);
        audioButton.setComponentPopupMenu(menu);

    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    private class ComponentDragged extends MouseMotionAdapter {
        @Override
        public void mouseDragged(java.awt.event.MouseEvent arg0) {
            Point location = SwingUtilities.convertPoint(selectedComponent, arg0.getPoint(), selectedComponent.getParent());
            Point newLocation = selectedComponent.getLocation();
            newLocation.translate(location.x - startPoint.x, location.y - startPoint.y);                   
            newLocation.x = Math.max(newLocation.x, 0);
            newLocation.y = Math.max(newLocation.y, 0);
            newLocation.x = Math.min(newLocation.x, selectedComponent.getParent().getWidth() - selectedComponent.getWidth());
            newLocation.y = Math.min(newLocation.y, selectedComponent.getParent().getHeight() - selectedComponent.getHeight());
            endPoint = newLocation;
            selectedComponent.setLocation(newLocation);
            startPoint = location;

            // if(componentDirection == 1 || componentDirection == 2 || componentDirection == 3 || componentDirection == 4 ||
            //     componentDirection == 5 || componentDirection == 6 || componentDirection == 7 || componentDirection == 8) {
            //         selectedComponent.setLocation(newLocation);
            //         startPoint = location;
            //         return;
            // } else if(selectedComponent.getParent().getBounds().contains(location)) {
            //     //selectedComponent.setLocation(newLocation);
            //     //startPoint = location;
            // }
            wasModified();
        }

        @Override
        public void mouseMoved(MouseEvent e) 
        {
            Component component = e.getComponent();
            Container parent = component.getParent();
            parent.setComponentZOrder(component, 0);
            component.setCursor(Cursor.getDefaultCursor());
            mouseOverEdge(e);
        }   

        public void mouseOverEdge(MouseEvent e)
        {
            selectedComponent = (JComponent) e.getComponent();
            Point p = new Point(e.getX() + e.getComponent().getX(), e.getY() + e.getComponent().getY());
            if(isOverRect(p, 7, 7, e.getComponent())) 
            {
                Rectangle r = e.getComponent().getBounds();
                startPoint = e.getPoint();
                switch(getOutcode(p, e.getComponent())) 
                {
                    case Rectangle.OUT_TOP:
                        if(Math.abs(p.y - r.y) < 7) {
                            e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                            componentDirection = 1;
                        }
                        break;
                    case Rectangle.OUT_TOP + Rectangle.OUT_LEFT:
                        if(Math.abs(p.y - r.y) < 7 && Math.abs(p.x - r.x) < 7) {
                            e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                            componentDirection = 2;
                        }
                        break;
                    case Rectangle.OUT_LEFT:
                        if(Math.abs(p.x - r.x) < 7) {
                            e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                            componentDirection = 3;
                        }
                        break;
                    case Rectangle.OUT_LEFT + Rectangle.OUT_BOTTOM:
                        if(Math.abs(p.x - r.x) < 7 && Math.abs(p.y - (r.y + r.height)) < 7) {
                            e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
                            componentDirection = 4;
                        }
                        break;
                    case Rectangle.OUT_BOTTOM:
                        if(Math.abs(p.y - (r.y + r.height)) < 7) {
                            e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                            componentDirection = 5;
                        }
                        break;
                    case Rectangle.OUT_BOTTOM + Rectangle.OUT_RIGHT:
                        if(Math.abs(p.x - (r.x + r.width)) < 7 && Math.abs(p.y - (r.y + r.height)) < 7) {
                            e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                            componentDirection = 6;
                        }
                        break;
                    case Rectangle.OUT_RIGHT:
                        if(Math.abs(p.x - (r.x + r.width)) < 7) {
                            e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                            componentDirection = 7;
                        }
                        break;
                    case Rectangle.OUT_RIGHT + Rectangle.OUT_TOP:
                        if(Math.abs(p.x - (r.x + r.width)) < 7 && Math.abs(p.y - r.y) < 7) {
                            e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                            componentDirection = 8;
                        }
                        break;
                }
            }
        }

        private int getOutcode(Point p, Component component) {
            Rectangle r = (Rectangle)(component.getBounds()).clone();
            r.grow(-7, -7);
            return r.outcode(p.x, p.y);        
        }

        private boolean isOverRect(Point p, int xoffset, int yoffset, Component component) {
            Rectangle r = (Rectangle)(component.getBounds()).clone();
            r.grow(xoffset, yoffset);
            return r.contains(p);       
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Point cursor = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(cursor, this);

        pencilSize = pencilSize == null ? 8 : SlideCraftFrame.getInstance().getPencilSize();

        if (erasing){
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1));
            g2.drawOval((int)cursor.getX()-pencilSize/2, (int)cursor.getY()-pencilSize/2, pencilSize, pencilSize);
        }
        
        if ( drawingEvent != null ){
            Point point = new Point((int)drawingEvent.getX(), (int)drawingEvent.getY());
            Color c = SlideCraftFrame.getInstance().getDrawingColor() != null ? SlideCraftFrame.getInstance().getDrawingColor(): Color.BLACK;
            Tuple<Integer, Color> dotParams = new Tuple<>(pencilSize, c);
            pencilDrawing.put(point, dotParams);
            drawingEvent = null;
        }
        
        for (Point p: pencilDrawing.keySet()){
            g.setColor(pencilDrawing.get(p).getItem2());
            int penSize = pencilDrawing.get(p).getItem1();
            g.fillOval((int)p.getX()-penSize/2, (int)p.getY()-penSize/2, penSize, penSize);
        }
        
        
    }

    private class Drawing extends MouseMotionAdapter implements Serializable {
        @Override
        public void mouseDragged(java.awt.event.MouseEvent e) {
            Component[] componentList = getComponents();
            boolean inBounds = false;
            for(Component c: componentList) {
                if(e.getX() >= c.getX() && e.getX() <= c.getX() + c.getWidth()) {
                    if(e.getY() >= c.getY() && e.getY() <= c.getY() + c.getHeight()) {
                        inBounds = true;
                    }
                }
            }

            if(!inBounds) {  
                if (drawing){
                    drawingEvent = e;
                    synchronized(this){
                        repaint();
                        wasModified();
                    }
                } else if (erasing){
                    drawingEvent = null;
                    int pencilSize = (int) (SlideCraftFrame.getInstance().getPencilSize()/1.3);
                    for (int i = -1*pencilSize; i <= pencilSize; i++){
                        for (int j = -1*pencilSize; j <= pencilSize; j++){
                            Point p = new Point(e.getX()+i, e.getY()+j);
                            pencilDrawing.remove(p);
                        }
                    }
                    synchronized(this){
                        repaint();
                        wasModified();
                    }
                }
            }
        }

        public void mouseMoved(java.awt.event.MouseEvent e) {
            if (erasing){
                revalidate();
                repaint();
            }
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject();

        this.parent = SlideCraftFrame.getInstance().getModel().getSlides();

        this.popup = new JPopupMenu();
        // add menu items to popup
        this.popup.add(new JMenuItem("Remove")).addActionListener( event -> {
            SlideCraftFrame.getInstance().removeSlide();
        });
        this.popup.add(new JMenuItem("Duplicate")).addActionListener( event -> {
        SlideCraftFrame.getInstance().addSlide(true);
        });

        this.drawingListener = new Drawing();
        this.addMouseMotionListener(this.drawingListener);
    }
}

class Tuple<T1, T2> implements Serializable{
    private final T1 item1;
    private final T2 item2;
    
    public Tuple(T1 item1, T2 item2) {
        this.item1 = item1;
        this.item2 = item2;
    }
    
    public T1 getItem1() {
        return item1;
    }
    
    public T2 getItem2() {
        return item2;
    }
}
