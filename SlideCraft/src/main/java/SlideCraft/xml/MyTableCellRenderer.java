package SlideCraft.xml;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class MyTableCellRenderer extends DefaultTableCellRenderer{
    TableCellRenderer render;
    Border border;
    public MyTableCellRenderer(TableCellRenderer r, Color c){
        render =r;
        border = BorderFactory.createLineBorder(Color.BLACK);
    }
    //@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        JComponent c = (JComponent) render.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setBorder(border);
        return c;
    }
}
