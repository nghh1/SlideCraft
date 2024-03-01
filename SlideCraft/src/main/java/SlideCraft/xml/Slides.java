package SlideCraft.xml;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import SlideCraft.ui.SlideCraftFrame;
import SlideCraft.ui.helper.UndoRedoHelper;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Slides extends JPanel{
    //private ScrollPane scrollSlides;
    private List<Slide> slides = null;
    private Slide currSelected = null;
    private JPanel slidePreviewPanel;
    private double scale;
    private UndoRedoHelper helper;
    // Uncomment to test slide reordering
    // private int startColor = 10;

    public Slides(){
        this.slidePreviewPanel = new JPanel();
        this.slidePreviewPanel.setLayout(new BoxLayout(this.slidePreviewPanel, BoxLayout.Y_AXIS));
        this.slidePreviewPanel.setBackground(SlideCraftFrame.interfaceMainColor);
        this.helper = new UndoRedoHelper();
        this.setBackground(SlideCraftFrame.backgroundColor);
        this.slides = new ArrayList<>();
        this.scale = 50;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // Add first empty slide
        this.newSlide(this.scale, false);
    }

    public void updateSlidesSize(double scale){
        for(Slide s: this.slides){
            s.setSize(scale);
            s.revalidate();
            s.repaint();
        }
    }
    public void initSlidesPreview(){
        if (slides == null){
            return;
        }
        this.slidePreviewPanel.removeAll();
        int i = 1;
        for (Slide sld: slides){
            this.slidePreviewPanel.add(createPreview(sld, i));
        }
        refresh();
    }

    public JPanel createPreview(Slide sld, int idx){
        SlidePreview sp = new SlidePreview(sld);
        int fontSize = 10;
        JLabel index = new JLabel(Integer.toString(idx));
        index.setBorder(new EmptyBorder(0, 0, 0, 0));
        index.setFont(new Font("Serif", Font.PLAIN, fontSize));
        index.setForeground(Color.WHITE);
        JPanel indexAndSlide = new JPanel();
        indexAndSlide.setLayout(new BoxLayout(indexAndSlide, BoxLayout.X_AXIS));
        indexAndSlide.setBackground(SlideCraftFrame.interfaceMainColor);
        indexAndSlide.setMaximumSize(new Dimension((int)sp.getPreferredSize().getWidth()+fontSize+8, (int)sp.getPreferredSize().getHeight()));
        JPanel indexAndReorder = new JPanel(new BorderLayout());
        indexAndReorder.setBackground(SlideCraftFrame.interfaceMainColor);
        indexAndReorder.setPreferredSize(new Dimension(10, (int)sp.getPreferredSize().getHeight()));
        Icon upIcon = new ImageIcon("./SlideCraft/src/main/resources/resourses/images/up.png");
        JButton up = new JButton(upIcon);
        up.addActionListener(e -> this.moveUp(sld));
        up.setBackground(SlideCraftFrame.interfaceMainColor);
        up.setBorderPainted(false);
        Icon downIcon = new ImageIcon("./SlideCraft/src/main/resources/resourses/images/down.png");
        JButton down = new JButton(downIcon);
        down.addActionListener(e -> this.moveDown(sld));
        down.setBackground(SlideCraftFrame.interfaceMainColor);
        down.setBorderPainted(false);
        indexAndReorder.add(up, BorderLayout.NORTH);
        indexAndReorder.add(index, BorderLayout.CENTER);
        indexAndReorder.add(down, BorderLayout.SOUTH);
        indexAndSlide.add(indexAndReorder);
        indexAndSlide.add(Box.createRigidArea(new Dimension(5, (int)sp.getPreferredSize().getHeight())));
        indexAndSlide.add(sp);

        JPanel slideNewPanel = new JPanel();
        slideNewPanel.setBackground(SlideCraftFrame.interfaceMainColor);
        slideNewPanel.setLayout(new BoxLayout(slideNewPanel, BoxLayout.Y_AXIS));
        slideNewPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        slideNewPanel.add(indexAndSlide);
        return slideNewPanel;
    }

    public synchronized void updatePreview(Slide sld){
        int slide_index = this.slides.indexOf(sld);
        if (slide_index == -1){
            return;
        }
        JPanel boxAndSlide = (JPanel)this.slidePreviewPanel.getComponents()[slide_index];
        JPanel indexAndSlide = (JPanel)boxAndSlide.getComponents()[1];
        SlidePreview slidePreview = (SlidePreview)indexAndSlide.getComponents()[2];
        slidePreview.updatePreview(sld);
        refresh();
    }

    public void moveUp(Slide sld){
        int slide_index = this.slides.indexOf(sld);
        if (slide_index == 0){
            return;
        }
        JPanel slidePanel = (JPanel) this.getComponent(slide_index);
        this.remove(slidePanel);
        this.add(slidePanel, slide_index-1);
        this.slides.remove(sld);
        this.slides.add(slide_index-1, sld);

        JPanel slidePrwPanel = (JPanel) this.slidePreviewPanel.getComponent(slide_index);
        this.slidePreviewPanel.remove(slidePrwPanel);
        this.slidePreviewPanel.add(slidePrwPanel, slide_index-1);
        updateSlidePreviewIndexes();
        refresh();
    }

    public void moveDown(Slide sld){
        int slide_index = this.slides.indexOf(sld);
        if (slide_index == this.slides.size()-1){
            return;
        }
        JPanel slidePanel = (JPanel) this.getComponent(slide_index);
        this.remove(slidePanel);
        this.add(slidePanel, slide_index+1);
        this.slides.remove(sld);
        this.slides.add(slide_index+1, sld);

        JPanel slidePrwPanel = (JPanel) this.slidePreviewPanel.getComponent(slide_index);
        this.slidePreviewPanel.remove(slidePrwPanel);
        this.slidePreviewPanel.add(slidePrwPanel, slide_index+1);
        
        updateSlidePreviewIndexes();
        refresh();
    }

    public void refresh(){
        this.revalidate();
        this.repaint();
        this.slidePreviewPanel.revalidate();
        this.slidePreviewPanel.repaint();
    }

    public Slide getSelectedSlide(){
        return this.currSelected;
    }

    public void clearSlidesPreviewPanel(){
        this.slidePreviewPanel.removeAll();
    }


    public void setSelected(Slide s){
        if (this.currSelected != null){
            this.currSelected.unselect();
        }
        this.currSelected = s;
    }

    public JPanel getPreviewPanel(){
        return this.slidePreviewPanel;
    }

    public List<Slide> getSlides() {
        return this.slides;
    }

    public void newSlide(double scale, boolean isDuplicate){
        Slide n = new Slide(this);
        if (isDuplicate){
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(this.currSelected);
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                ObjectInputStream ois = new ObjectInputStream(bais);
                n = (Slide) ois.readObject();
                n.unselect();
            } catch (IOException|ClassNotFoundException ex) {
                ex.printStackTrace();
            }            
        }

        n.setSize(scale);
        int index = 0;
        if (this.currSelected == null){
            n.select();
            index = this.slides.size();
        } else{
            index = this.slides.indexOf(this.currSelected)+1;
        }
        this.slides.add(index, n);
        JPanel slidePanel = new JPanel();
        slidePanel.setBackground(SlideCraftFrame.backgroundColor);
        slidePanel.setLayout(new BoxLayout(slidePanel, BoxLayout.Y_AXIS));
        slidePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        slidePanel.add(n);
        this.add(slidePanel, index);
        this.slidePreviewPanel.add(createPreview(n, index), index);
        updateSlidePreviewIndexes();
    }
    
    public void removeSlide(){
        int slide_index = this.slides.indexOf(this.currSelected);
        this.remove(slide_index);
        this.slidePreviewPanel.remove(slide_index);
        this.slides.remove(this.currSelected);
        if (this.slides.size() != 0){
            this.slides.get(0).select();
        } else{
            setSelected(null);
        }
        updateSlidePreviewIndexes();
    }
    
    public void updateSlidePreviewIndexes(){
        int i = 1;
        for (Component elem1: this.slidePreviewPanel.getComponents()){
            JPanel boxAndSlide = (JPanel) elem1;
            JPanel indexAndSlide = (JPanel)boxAndSlide.getComponents()[1];
            JPanel indexAndReorder = (JPanel)indexAndSlide.getComponents()[0];
            JLabel index = (JLabel)indexAndReorder.getComponents()[1];
            index.setText(Integer.toString(i));
            i += 1;
        }
    }

    public UndoRedoHelper getHelper(){
        return this.helper;
    }
}
