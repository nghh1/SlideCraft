package SlideCraft.ui;

import java.awt.*;
import javax.swing.*;
import SlideCraft.ui.action.ToolBarAction;
import java.awt.event.*;

public class InsertActionsMenu extends JPanel implements MouseListener {

    static final int width = 90; 
    static final int height = 200; 

    private JPopupMenu shapesPopup;
    private JPopupMenu diagramsPopup;
    private JButton shape;
    private JButton diagram;
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public InsertActionsMenu(){        
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(SlideCraftFrame.popupMenuColor);
        this.setOpaque(true);
        this.setVisible(true);
        JToolBar toolBar = new JToolBar();
        toolBar.setBackground(this.getBackground());
        toolBar.setPreferredSize(new Dimension(width, height));
        toolBar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolBar.setFloatable(false);
        toolBar.setForeground(Color.WHITE);
        JButton hyperLink = newToolBarButton("Hyperlink", ToolBarAction.INSERT_HYPERLINK);
        JButton image = newToolBarButton("Image", ToolBarAction.INSERT_IMAGE);
        JButton sound = newToolBarButton("Sound", ToolBarAction.INSERT_SOUND);
        shape = newToolBarButton("Shape", ToolBarAction.INSERT_SHAPE);
        
        shape.addMouseListener(this);
        this.shapesPopup = new JPopupMenu();
        // add menu items to popup
        JMenuItem rectangle = new JMenuItem("Rectangle");
        rectangle.addActionListener(ToolBarAction.INSERT_RECTANGLE.getAction());
        this.shapesPopup.add( rectangle );

        JMenuItem circle = new JMenuItem("Circle");
        circle.addActionListener(ToolBarAction.INSERT_CIRCLE.getAction());
        this.shapesPopup.add( circle );

        JButton table = newToolBarButton("Table", ToolBarAction.INSERT_TABLE);
        diagram = newToolBarButton("Diagram", ToolBarAction.INSERT_DIAGRAM);
        
        diagram.addMouseListener(this);
        this.diagramsPopup = new JPopupMenu();
        JMenuItem lineDiagram = new JMenuItem("Line diagram");
        lineDiagram.addActionListener(ToolBarAction.INSERT_LINE_DIAGRAM.getAction());
        this.diagramsPopup.add(lineDiagram);

        JMenuItem barchart = new JMenuItem("Bar chart");
        barchart.addActionListener(ToolBarAction.INSERT_BARCHART.getAction());
        this.diagramsPopup.add(barchart);
        
        toolBar.add(hyperLink);
        toolBar.add(image);
        toolBar.add(sound);
        toolBar.add(shape);
        toolBar.add(table);
        toolBar.add(diagram);
        this.add(toolBar, BorderLayout.NORTH);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getSource()==shape){
            showPopup(e);
        }
        else if(e.getSource()==diagram){
            showDiagramPopup(e);
        }
    }

    private void showPopup(MouseEvent e){
        this.shapesPopup.show(e.getComponent(), e.getX(), e.getY());
        
    }

    private void showDiagramPopup(MouseEvent e){
        this.diagramsPopup.show(e.getComponent(), e.getX(), e.getY());
    }


    public JButton newToolBarButton(String name, ToolBarAction a){
        JButton new_button = new JButton(name);
        new_button.setBackground(Color.WHITE);
        new_button.addActionListener(a.getAction());
        return new_button;
    }    
}
