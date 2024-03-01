package SlideCraft.ui.helper;

import SlideCraft.ui.SlideCraftFrame;
import SlideCraft.xml.Slide;
import SlideCraft.xml.RectangleShape;
import SlideCraft.xml.CircleShape;
import java.awt.Point;
import java.awt.event.*;

public class ShapeInserter {
    
    Slide mainSlide;
    MouseMotionListener mouseMotionListener = null;

    Point startPoint = null;
    Point endPoint = null;

    boolean rectangleInsertion = false;
    boolean circleInsertion = false;

    RectangleShape r = null;
    CircleShape c = null;

    public ShapeInserter(){
        mouseMotionListener = new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                if (startPoint==null){
                    startPoint = new Point(e.getX(), e.getY());
                }
                else{
                    endPoint = new Point(e.getX(), e.getY());
                    updateShape();
                }
            }
            public void mouseMoved(MouseEvent e){
                if (endPoint != null){
                    updateShape();
                    stopInsertion();
                }
            }
        };
    }

    public void insertRectangle(){
        this.mainSlide = SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide();
        this.mainSlide.addMouseMotionListener(mouseMotionListener);
        this.circleInsertion = false;
        this.rectangleInsertion = true;
    }

    public void stopInsertion(){
        this.startPoint = null;
        this.endPoint = null;
        this.r = null;
        this.c = null;
        this.circleInsertion = false;
        this.rectangleInsertion = false;
        this.mainSlide.removeMouseMotionListener(mouseMotionListener);
    }

    public void insertCircle(){
        this.mainSlide = SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide();
        this.mainSlide.addMouseMotionListener(mouseMotionListener);
        this.rectangleInsertion = false;
        this.circleInsertion = true; 
    }

    public void updateShape (){
        if ( this.rectangleInsertion && r != null ){
            this.mainSlide.remove(r);
        }
        if ( this.circleInsertion && c != null){
            this.mainSlide.remove(c);
        }
        int x, y, width, height;
        x = (endPoint.getX() > startPoint.getX()) ? (int)startPoint.getX() : (int)endPoint.getX();
        y = (endPoint.getY() > startPoint.getY()) ? (int)startPoint.getY() : (int)endPoint.getY();
        width  = (endPoint.getX() > startPoint.getX()) ? (int)(endPoint.getX()-startPoint.getX()) : (int)(startPoint.getX()-endPoint.getX());
        height = (endPoint.getY() > startPoint.getY()) ? (int)(endPoint.getY()-startPoint.getY()) : (int)(startPoint.getY()-endPoint.getY());
        
        if (rectangleInsertion){
            this.r = new RectangleShape(x, y, width, height);
            this.mainSlide.add(r);
            this.r.revalidate();
            this.r.repaint();
            SlideCraftFrame.getInstance().getModel().getSlides().getHelper().placedComponent(this.mainSlide,this.r);
        }
        else if (circleInsertion){
            this.c = new CircleShape(x, y, width, height);
            this.mainSlide.add(c);
            this.c.revalidate();
            this.c.repaint();
            SlideCraftFrame.getInstance().getModel().getSlides().getHelper().placedComponent(this.mainSlide,this.c);
        }
        
        this.mainSlide.revalidate();
        this.mainSlide.repaint();
        this.mainSlide.wasModified();
    }
    
    
}



