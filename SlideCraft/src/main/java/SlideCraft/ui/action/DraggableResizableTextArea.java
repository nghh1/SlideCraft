package SlideCraft.ui.action;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.DefaultCaret;

import org.drjekyll.fontchooser.FontDialog;

import SlideCraft.ui.SlideCraftFrame;
import SlideCraft.xml.Slide;

public class DraggableResizableTextArea extends JEditorPane{
    //private JTextArea textBox;
    private int x = 0,y = 0;
    private int width = 0,height = 0;
    private int direction;
    private boolean select;
    private JEditorPane INSTANCE;
    private Point startPoint;

    private JPopupMenu popup;

    public DraggableResizableTextArea(){
        INSTANCE = this;
        setText("Text");
        setOpaque(true);
        this.select = false;
        setBounds(50,50, 100, 100);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        addListeners();
    }

    public boolean isSelected(){
        return this.select;
    }

    public void select(){
        setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
        this.select = true;
        this.getCaret().setVisible(true);
        ((Slide)getParent()).wasModified();
    }

    public void unselect(){
        setBorder(null);
        this.select = false;
        this.getCaret().setVisible(false);
        ((Slide)getParent()).wasModified();
    }

    private boolean inResizeBound(MouseEvent e){
        int x=e.getX();
        int y=e.getY();
        int width = INSTANCE.getWidth();
        int height = INSTANCE.getHeight();
        return x==INSTANCE.getX() || x >= width-5|| y==INSTANCE.getY() || y>=height-5;
    }  

    public boolean mouseOverEdge(MouseEvent e)
    {
        Point p = new Point(e.getX() + e.getComponent().getX(), e.getY() + e.getComponent().getY());
        if(isOverRect(p, 5, 5, e.getComponent())) 
        {
            Rectangle r = e.getComponent().getBounds();
            switch(getOutcode(p, e.getComponent())) 
            {
                case Rectangle.OUT_TOP:
                    if(Math.abs(p.y - r.y) < 5) {
                        e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                        direction = Cursor.N_RESIZE_CURSOR;
                    }
                    break;
                case Rectangle.OUT_TOP + Rectangle.OUT_LEFT:
                    if(Math.abs(p.y - r.y) < 5 && Math.abs(p.x - r.x) < 5) {
                        e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                        direction = Cursor.NW_RESIZE_CURSOR;
                    }
                    break;
                case Rectangle.OUT_LEFT:
                    if(Math.abs(p.x - r.x) < 5) {
                        e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                        direction = Cursor.W_RESIZE_CURSOR;
                    }
                    break;
                case Rectangle.OUT_LEFT + Rectangle.OUT_BOTTOM:
                    if(Math.abs(p.x - r.x) < 5 && Math.abs(p.y - (r.y + r.height)) < 5) {
                        e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
                        direction = Cursor.SW_RESIZE_CURSOR;
                    }
                    break;
                case Rectangle.OUT_BOTTOM:
                    if(Math.abs(p.y - (r.y + r.height)) < 5) {
                        e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                        direction = Cursor.S_RESIZE_CURSOR;
                    }
                    break;
                case Rectangle.OUT_BOTTOM + Rectangle.OUT_RIGHT:
                    if(Math.abs(p.x - (r.x + r.width)) < 5 && Math.abs(p.y - (r.y + r.height)) < 5) {
                        e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                        direction = Cursor.SE_RESIZE_CURSOR;
                    }
                    break;
                case Rectangle.OUT_RIGHT:
                    if(Math.abs(p.x - (r.x + r.width)) < 5) {
                        e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                        direction = Cursor.E_RESIZE_CURSOR;
                    }
                    break;
                case Rectangle.OUT_RIGHT + Rectangle.OUT_TOP:
                    if(Math.abs(p.x - (r.x + r.width)) < 5 && Math.abs(p.y - r.y) < 5) {
                        e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                        direction = Cursor.NE_RESIZE_CURSOR;
                    }
                    break;
            }
            return true;
        }
        return false;
    }

    private int getOutcode(Point p, Component component) {
        Rectangle r = (Rectangle)(component.getBounds()).clone();
        r.grow(-2, -2);
        return r.outcode(p.x, p.y);        
    }

    private boolean isOverRect(Point p, int xoffset, int yoffset, Component component) {
        Rectangle r = (Rectangle)(component.getBounds()).clone();
        r.grow(xoffset, yoffset);
        return r.contains(p);       
    }
    
    private Color chooseColour(){
        return JColorChooser.showDialog(null, "Choose Text Colour", Color.black);
    }

    public void addListeners(){
        this.INSTANCE = this;
        this.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent de) {
                BackgroundTask worker = new BackgroundTask(SlideCraftFrame.getInstance()) {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            SlideCraftFrame.getInstance().getSearchField().search();
                        } catch (Exception e) {
                            throw e;
                        } 
                        return null;
                    }
        
                    @Override
                    protected void done() {
                        stopProcessing();
                        try {
                            get();
                        } catch (Exception e) {
                          showErrorMessage(e);
                        }
                    }
                };
                worker.execute();
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                BackgroundTask worker = new BackgroundTask(SlideCraftFrame.getInstance()) {
                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            SlideCraftFrame.getInstance().getSearchField().search();
                        } catch (Exception e) {
                            throw e;
                        } 
                        return null;
                    }
        
                    @Override
                    protected void done() {
                        stopProcessing();
                        try {
                            get();
                        } catch (Exception e) {
                          showErrorMessage(e);
                        }
                    }
                };
                worker.execute();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
            //Plain text components don't fire these events.
            }
        });        

        this.popup = new JPopupMenu();
        this.popup.add(new JMenuItem("Colour")).addActionListener( event->{
            Color changedColour = chooseColour();
            if(changedColour != null){setForeground(changedColour);}
        });
        this.popup.add(new JMenuItem("Font")).addActionListener( event ->{
            FontDialog.showDialog(INSTANCE);
        });
        this.popup.add(new JMenuItem("Remove")).addActionListener( event -> {
            Slide selectSlide = ((Slide)getParent());
            selectSlide.remove(INSTANCE);
            selectSlide.revalidate();
            selectSlide.repaint();
            selectSlide.wasModified();
        });

        addMouseMotionListener(new MouseMotionListener(){
            @Override
            public void mouseDragged(MouseEvent e){
                select();
                if ( ( x==0 && y==0 && width==0 && height==0 ) ){
                    x = e.getX();
                    y = e.getY();
                    width = getWidth();
                    height = getHeight();
                } 

                if(inResizeBound(e)){
                    int displacementX = e.getX()-x;
                    int displacementY = e.getY()-y;
                    int updateWidth = width;
                    int updateHeight = height;
                    switch(direction){
                        case Cursor.N_RESIZE_CURSOR:
                            updateHeight -= displacementY;
                            setLocation(INSTANCE.getX(),INSTANCE.getY()+displacementY);
                            break;
                        case Cursor.NE_RESIZE_CURSOR:
                            updateWidth += displacementX;
                            updateHeight -= displacementY;
                            setLocation(INSTANCE.getX(),INSTANCE.getY()+displacementY);
                            break;
                        case Cursor.E_RESIZE_CURSOR:
                            updateWidth+=displacementX;
                            break;
                        case Cursor.SE_RESIZE_CURSOR:
                            updateWidth+=displacementX;
                            updateHeight+=displacementY;
                            break;
                        case Cursor.S_RESIZE_CURSOR:
                            updateHeight+=displacementY;
                            break;
                        case Cursor.SW_RESIZE_CURSOR:
                            updateWidth-=displacementX;
                            updateHeight+=displacementY;
                            setLocation(getX()+displacementX, getY());
                            break;
                        case Cursor.W_RESIZE_CURSOR:
                            updateWidth-=displacementX;
                            setLocation(getX()+displacementX, getY());
                            break;
                        case Cursor.NW_RESIZE_CURSOR:
                            updateWidth-=displacementX;
                            updateHeight-=displacementY;
                            setLocation(getX()+displacementX, getY()+displacementY);
                            break;
                    }
                    setSize(new Dimension(updateWidth,updateHeight));
                }
                else if(!inResizeBound(e) && startPoint!=null){
                    Point location = SwingUtilities.convertPoint(INSTANCE, e.getPoint(), ((Slide)getParent()));
                    if(((Slide)getParent()).getBounds().contains(location)) {
                        Point newLocation = INSTANCE.getLocation();
                        newLocation.translate(location.x-startPoint.x,location.y-startPoint.y);                   
                        newLocation.x = Math.max(newLocation.x, 0);
                        newLocation.y = Math.max(newLocation.y, 0);
                        newLocation.x = Math.min(newLocation.x, ((Slide)getParent()).getWidth()-INSTANCE.getWidth());
                        newLocation.y = Math.min(newLocation.y, ((Slide)getParent()).getHeight()-INSTANCE.getHeight());
                        INSTANCE.setLocation(newLocation);
                        startPoint = location;
                    }
                }
                ((Slide)getParent()).wasModified();
            }

            @Override
            public void mouseMoved(MouseEvent e){
                x = 0;
                y = 0;
                width = 0; 
                height = 0;
                if(inResizeBound(e)){
                    mouseOverEdge(e);
                }else{
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }
        });

        setCaret(new DefaultCaret(){
            public void focusLost(FocusEvent fe) {
                setVisible(false);
                setSelectionVisible(false);   
                setBlinkRate( 0 ); 
            }

            public void focusGained(FocusEvent e)
            {
                setVisible(true);
                setSelectionVisible(true);
                setBlinkRate( UIManager.getInt("TextField.caretBlinkRate"));
            }
        });

        addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e){
                select();
                startPoint = SwingUtilities.convertPoint(INSTANCE, e.getPoint(), ((Slide)getParent()));
                if(SwingUtilities.isRightMouseButton(e)){
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
                if (SlideCraftFrame.getInstance().bucketUsed()){
                    setBackground(SlideCraftFrame.getInstance().getBucket());
                    ((Slide)getParent()).wasModified();
                    revalidate();
                    repaint();
                }
            }
            @Override
            public void mouseReleased(MouseEvent e){
                startPoint = null;
            }
            public void mousePressed(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject();
        addListeners();
    }
    
}
