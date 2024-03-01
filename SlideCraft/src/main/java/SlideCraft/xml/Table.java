package SlideCraft.xml;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import SlideCraft.ui.SlideCraftFrame;

public class Table extends JTable{
    private JTable INSTANCE;
    private Point startPoint;
    public Table(){
        JTableModel model = new JTableModel();
        setModel(model);
        INSTANCE  = this;
        setDefaultRenderer(Object.class, new MyTableCellRenderer(this.getDefaultRenderer(Object.class), Color.BLACK));
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setSize(new Dimension(INSTANCE.getPreferredSize().width,INSTANCE.getRowHeight()*(INSTANCE.getRowCount())));
        addListeners();
    }

    public void addListeners(){
        this.INSTANCE = this;
        //INSTANCE.getTableHeader().setReorderingAllowed(false);
        
        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                startPoint = SwingUtilities.convertPoint(INSTANCE, e.getPoint(), (Slide)getParent());
            }
            public void mouseReleased(MouseEvent e){
                startPoint = null;
            }
        });
        //this.addMouseListener(mouseClickListener);
        addMouseMotionListener(new MouseMotionAdapter(){
            public void mouseDragged(MouseEvent e){
                if(startPoint!=null){
                    Point location = SwingUtilities.convertPoint(INSTANCE, e.getPoint(), (Slide)getParent());
                    if(((Slide)getParent()).getBounds().contains(location)){
                        Point newLocation = INSTANCE.getLocation();
                        newLocation.translate(location.x-startPoint.x,location.y-startPoint.y);
                        newLocation.x = Math.max(newLocation.x, 0);
                        newLocation.y = Math.max(newLocation.y, 0);
                        newLocation.x = Math.min(newLocation.x,((Slide)getParent()).getWidth()-INSTANCE.getWidth());
                        newLocation.y = Math.min(newLocation.y,((Slide)getParent()).getHeight()-INSTANCE.getHeight());
                        INSTANCE.setLocation(newLocation);
                        startPoint = location;
                        SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().wasModified();
                    }
                }
            }
        });
        //this.addMouseMotionListener(mouseMotionListener);

        JPopupMenu menu = new JPopupMenu();
        JMenuItem addRow = new JMenuItem("Add row");
        addRow.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                addRow();
                INSTANCE.setSize(new Dimension(INSTANCE.getPreferredSize().width,INSTANCE.getRowHeight()*(INSTANCE.getRowCount())));
                SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().wasModified();
            }
        });
        
        JMenuItem removeRow = new JMenuItem("Remove row");
        removeRow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                removeRow();
                INSTANCE.setSize(new Dimension(INSTANCE.getPreferredSize().width,INSTANCE.getRowHeight()*(INSTANCE.getRowCount())));
                SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().wasModified();
            }
        });
        JMenuItem addColumn = new JMenuItem("Add column");
        addColumn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                addColumn();
                INSTANCE.setSize(new Dimension(INSTANCE.getPreferredSize().width,INSTANCE.getRowHeight()*(INSTANCE.getRowCount())));
                SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().wasModified();
            }
        });
        
        JMenuItem changeColumnName = new JMenuItem("Edit columnName");
        changeColumnName.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                editColumnName();
                SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().wasModified();
            }
        });
        
        JMenuItem removeColumn = new JMenuItem("Remove column");
        removeColumn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                removeColumn();
                INSTANCE.setSize(new Dimension(INSTANCE.getPreferredSize().width,INSTANCE.getRowHeight()*(INSTANCE.getRowCount())));
                SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().wasModified();
            }
        });
        JMenuItem edit = new JMenuItem("Edit cell");
        edit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                editCell();
                SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide().wasModified();
            }
        });
        JMenuItem removeTable = new JMenuItem("Remove table");
        removeTable.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                Slide s = (Slide)getParent();
                s.remove(INSTANCE);
                s.revalidate();
                s.repaint();
                s.wasModified();
            }
        });
        menu.add(addRow);
        menu.add(removeRow);
        menu.add(addColumn);
        //menu.add(changeColumnName);
        menu.add(removeColumn);
        menu.add(edit);
        menu.add(removeTable);
        this.INSTANCE.setComponentPopupMenu(menu);
        
    }

    public void addRow(){
        JTableModel model = (JTableModel)getModel();
        model.addRow();
    }
    public void removeRow(){
        int selectedRow = this.getSelectedRow();
        JTableModel model = (JTableModel)getModel();
        if(selectedRow!=-1)
            model.removeRow(selectedRow);
    }
    public void addColumn(){
        //String name = JOptionPane.showInputDialog("Enter column name:");
        JTableModel model = (JTableModel)getModel();
        //if(name !=null && !name.isEmpty())
        model.addColumn();
    }
    public void removeColumn(){
        int selectedColumn = this.getSelectedColumn();
        JTableModel model = (JTableModel)getModel();
        if(selectedColumn!=-1)
            model.removeColumn(selectedColumn);
    }
    public void editColumnName(){
        int selectedColumn = this.getSelectedColumn();
        JTableModel model = (JTableModel)getModel();
        if(selectedColumn!=-1){
            String newName = JOptionPane.showInputDialog("Enter new column name:");
            if(newName!=null && !newName.isEmpty())
            model.setColumnHeader(selectedColumn, newName);
        }
    }
    public void editCell(){
        int selectedRow = this.getSelectedRow();
        int selectedColumn = this.getSelectedColumn();
        JTableModel model = (JTableModel)getModel();
        if(selectedRow!=-1 && selectedColumn!=-1){
            String value = JOptionPane.showInputDialog("Enter new value:");
            if(value!=null && !value.isEmpty()){
                model.setValueAt(value, selectedRow, selectedColumn);
            }
        }
    }

    
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject();
        addListeners();
        setDefaultRenderer(Object.class, new MyTableCellRenderer(this.getDefaultRenderer(Object.class), Color.BLACK));
        
    }
    
}
