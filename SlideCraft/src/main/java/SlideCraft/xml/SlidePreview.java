package SlideCraft.xml;

import java.awt.image.BufferedImage;
import java.awt.*;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import SlideCraft.ui.SlideCraftFrame;
import SlideCraft.ui.action.BackgroundTask;

import java.awt.event.*;

public class SlidePreview extends JPanel implements MouseListener {

  private BufferedImage img;
  private int height = 140;
  private int width = 220;

  private  Slide mainSlide;
  private int transition;
  private JPopupMenu popup;

  private boolean functional = true;

  protected MouseAdapter clickListener = null;
  public void mouseClicked(MouseEvent e) {
    if (!mainSlide.isSelected()){
      mainSlide.select();
    } 
    SlideCraftFrame.getInstance().getSpeakerNotesPanel().removeAll();
    SlideCraftFrame.getInstance().getSpeakerNotesPanel().revalidate();
    SlideCraftFrame.getInstance().getSpeakerNotesPanel().repaint();
    SlideCraftFrame.getInstance().getSpeakerNotesPanel().add(mainSlide.getSpeakerNotes());
  }
  public void mousePressed(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {
    if (!mainSlide.isSelected()){
      mainSlide.select();
    }  
    showPopup(e);
  }
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}

  public SlidePreview(Slide s) {
    this.mainSlide = s;
    this.transition = s.getTransition();
    updateImage(s);
    this.addMouseListener(this);
    initPopup();
  }

  public SlidePreview(Slide s, int width, int height, boolean functional){
    this(s);
    this.width = width;
    this.height = height;
    this.functional = functional;
    if (!functional){
      updateImage(s);
      this.removeMouseListener(this);
    }
  }

  private void showPopup(MouseEvent e){
    if(e.isMetaDown()){
      this.popup.show(e.getComponent(), e.getX(), e.getY());
    } 
  }

  public int getTransition() {
    return this.transition;
  }

  public void initPopup(){
    this.popup = new JPopupMenu();
    // add menu items to popup
    this.popup.add(new JMenuItem("Remove")).addActionListener( event -> {
      SlideCraftFrame.getInstance().removeSlide();
    });
    this.popup.add(new JMenuItem("Duplicate")).addActionListener( event -> {
      SlideCraftFrame.getInstance().addSlide(true);
    });
  }

  private synchronized void updateImage(Slide s) {
    this.setPreferredSize(new Dimension(width, height));
    this.setMaximumSize( this.getPreferredSize() );
    this.setMinimumSize( this.getPreferredSize() );
    try {
        Dimension slideSize = s.getPreferredSize();
        BufferedImage image = new BufferedImage((int)slideSize.getWidth(), (int)slideSize.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        Slide clone = (Slide)s.clone();
        if (!functional){
          clone.unselect();
        }
        SwingUtilities.paintComponent(g2, clone, this, 0, 0, (int)slideSize.getWidth(), (int)slideSize.getHeight());
        g2.dispose();
        this.img = image;
    } catch (Exception e) {
        e.printStackTrace();
    }
  }

  public synchronized void updatePreview(Slide s) {
    BackgroundTask worker = new BackgroundTask(SlideCraftFrame.getInstance()) {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                  updateImage(s);
                  revalidate();
                  repaint(); // Trigger repaint to reflect the changes
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
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(img, 0, 0, width, height, this);
  }
}
