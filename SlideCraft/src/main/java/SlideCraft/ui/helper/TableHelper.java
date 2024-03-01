package SlideCraft.ui.helper;
import javax.swing.JComponent;

import SlideCraft.ui.SlideCraftFrame;
import SlideCraft.xml.Slide;
import SlideCraft.xml.Table;

public class TableHelper extends JComponent{
    Table table;
    Slide s;
    public TableHelper(){
        
    }

    public void insertTable(){
        s = SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide();
        table = new Table();
        s.add(table);
        table.revalidate();
        table.repaint();
        s.revalidate();
        s.repaint();
        s.wasModified();
        SlideCraftFrame.getInstance().getModel().getSlides().getHelper().placedComponent(s,this.table);
    }

   
}
