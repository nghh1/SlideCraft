package SlideCraft.ui.helper;

import java.io.Serializable;
import java.util.Stack;
import javax.swing.*;

import SlideCraft.xml.Slide;

public class UndoRedoHelper implements Serializable{
    private Stack<JComponent> undoAvailable;
    private Stack<JComponent> redoAvailable;
    private Stack<Slide> undoSlides;
    private Stack<Slide> redoSlides;

    public UndoRedoHelper(){
        undoAvailable = new Stack<>();
        redoAvailable = new Stack<>();
        undoSlides = new Stack<>();
        redoSlides = new Stack<>();
    }

    public void placedComponent(Slide slide,JComponent component){
        undoAvailable.push(component);
        undoSlides.push(slide);
        redoAvailable.clear();
        redoSlides.clear();
    }

    public static void undoAction(UndoRedoHelper helper){
        if(!helper.undoAvailable.isEmpty()){
            JComponent componentOnPanel = helper.undoAvailable.pop();
            Slide slide = helper.undoSlides.pop();
            helper.redoAvailable.push(componentOnPanel);
            helper.redoSlides.push(slide);
            slide.remove(componentOnPanel);
            //Need refresh afterward
            slide.revalidate();
            slide.repaint();
        }
    }

    public static void redoAction(UndoRedoHelper helper){
        if(!helper.redoAvailable.isEmpty()){
            JComponent redoComponent = helper.redoAvailable.pop();
            Slide slide = helper.redoSlides.pop();
            helper.undoAvailable.push(redoComponent);
            helper.undoSlides.push(slide);
            slide.add(redoComponent);
            //Need refresh afterward
            slide.revalidate();
            slide.repaint();
        }
    }
}
