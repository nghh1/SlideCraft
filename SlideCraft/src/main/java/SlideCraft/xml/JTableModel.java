package SlideCraft.xml;

import javax.swing.table.AbstractTableModel;

public class JTableModel extends AbstractTableModel{
    private Object[][] data = {{"",""},{"",""}};
    private String[] columnNames = {"Column 1","Column 2"};
    
    public void addColumn(){
        String[] newColumnNames = new String[columnNames.length+1];
        Object[][] newColumns = new Object[data.length][columnNames.length+1];
        System.arraycopy(columnNames,0,newColumnNames,0,columnNames.length);
        //newColumnNames[columnNames.length] = name;
        for(int i=0;i<columnNames.length;i++){
            System.arraycopy(data[i],0,newColumns[i],0,data[i].length);
        }
        columnNames = newColumnNames;
        data = newColumns;
        fireTableStructureChanged();
    }
    public void removeColumn(int column){
        String[] newColumnNames = new String[columnNames.length-1];
        Object[][] newColumns = new Object[data.length][columnNames.length-1];
        for(int i=0,j=0;i<columnNames.length;i++){
            if(i!=column){
                newColumnNames[j]=columnNames[i];
                j++;
            }
        }
        for(int i=0;i<data.length;i++){
            System.arraycopy(data[i],0,newColumns[i],0,column);
            System.arraycopy(data[i],column+1,newColumns[i], column, columnNames.length-column-1);

        }
        columnNames = newColumnNames;
        data = newColumns;
        fireTableStructureChanged();
    }
    @Override
    public int getColumnCount(){
        return columnNames.length;
    }
    public void setColumnHeader(int column, String newName){
        columnNames[column]=newName;
        fireTableStructureChanged();
    }
    @Override
    public String getColumnName(int column){
        if(column<columnNames.length)
            return columnNames[column];
        return null;
    }

    public void addRow(){
        Object[][] newRows = new Object[data.length+1][columnNames.length];
        for(int i=0;i<data.length;i++){
            System.arraycopy(data[i], 0, newRows[i], 0, data[i].length);
        }
        data = newRows;
        fireTableRowsInserted(data.length-1, data.length-1);
    }
    public void removeRow(int row){
        Object[][] newRows = new Object[data.length-1][columnNames.length];
        for(int i=0, j=0;i<data.length;i++){
            if(i!=row){
                System.arraycopy(data[i],0,newRows[j],0,data[i].length);
                j++;
            }
        }
        data = newRows;
        fireTableRowsDeleted(row,row);
    }
    @Override
    public int getRowCount(){
        return data.length;
    }
    @Override
    public void setValueAt(Object value, int row, int column){
        data[row][column] = value;
        fireTableCellUpdated(row, column);
    }
    @Override
    public Object getValueAt(int row, int column){
        return data[row][column];
    }
    @Override
    public boolean isCellEditable(int row, int column){
        return true;
    }
}
