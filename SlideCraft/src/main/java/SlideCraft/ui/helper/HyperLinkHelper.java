package SlideCraft.ui.helper;

import javax.swing.JOptionPane;


import SlideCraft.ui.SlideCraftFrame;
import SlideCraft.xml.HyperLink;
import SlideCraft.xml.Slide;

public class HyperLinkHelper {
    private HyperLink hyperLink;
    private Slide s;
    public HyperLinkHelper(){

    }
    public void insertHyperLink(){
        s = SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide();
        String url = JOptionPane.showInputDialog("Input url:");
        if(url!=null && !url.isEmpty()){
            hyperLink = new HyperLink(url);
            s.add(hyperLink);
            s.revalidate();
            s.repaint();
            s.wasModified();
            SlideCraftFrame.getInstance().getModel().getSlides().getHelper().placedComponent(s,hyperLink);
        }
        
    }
}
