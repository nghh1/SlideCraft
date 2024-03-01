package SlideCraft.ui.helper;

import java.awt.*;
import javax.swing.*;
import SlideCraft.xml.LineGraph;
import SlideCraft.ui.SlideCraftFrame;
import SlideCraft.xml.BarChart;
import SlideCraft.xml.Slide;

public class DiagramHelper {
    private Slide s;
    private LineGraph lineGraph;
    private BarChart barChart;

    public DiagramHelper(){

    }

    public void insertLineDiagram(){
        try{
            String numberOfInputs = JOptionPane.showInputDialog("Enter number of inputs:");
            if((Integer.parseInt(numberOfInputs)>=0 || Integer.parseInt(numberOfInputs)<=9 )&& numberOfInputs!=null){
                int numInputs = Integer.parseInt(numberOfInputs);
                double[] inputs = new double[numInputs];
                for(int i=0;i<numInputs;i++){
                    String input = JOptionPane.showInputDialog("Enter input no."+(i+1)+":");
                    try{
                        inputs[i] = Integer.parseInt(input);
                    }catch(NumberFormatException e){
                        JOptionPane.showMessageDialog(null,"Invalid input, please enter number");
                        return;
                    }
                }
                lineGraph = new LineGraph(inputs);
                lineGraph.setSize(new Dimension(400,300));
                lineGraph.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                lineGraph.setLocation(100,50);
                s = SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide();
                s.add(lineGraph);
                s.revalidate();
                s.repaint();
                s.wasModified();
                SlideCraftFrame.getInstance().getModel().getSlides().getHelper().placedComponent(s,lineGraph);
            }
            else
                return;
        }catch(NumberFormatException e){
            return;
        }
        
    }

    public void insertBarChart(){
        try{
            String numberOfBars = JOptionPane.showInputDialog("Enter number of bars for bar chart:");
            if(Integer.parseInt(numberOfBars)!=0 || numberOfBars!=null){
                int numBars = Integer.parseInt(numberOfBars);
                int[] data = new int[numBars];
                String[] categories = new String[numBars];
                for(int i=0;i<numBars;i++){
                    String input = JOptionPane.showInputDialog("Enter value of bar:");
                    if(Integer.parseInt(input)!=0 || input!=null){
                        data[i]= Integer.parseInt(input);
                        String category = JOptionPane.showInputDialog("Enter category name of bar:");
                        if(category!=null)
                            categories[i] = category;
                        else
                            return;
                    }
                    else
                        return;
                }
                barChart = new BarChart(data);
                barChart.addChartPanel();
                JPanel labelPanel = new JPanel(new GridLayout(1,data.length));
                for(String category:categories){
                    JLabel label = new JLabel(category, JLabel.CENTER);
                    labelPanel.add(label);
                }
                barChart.add(labelPanel, BorderLayout.PAGE_END);
                s = SlideCraftFrame.getInstance().getModel().getSlides().getSelectedSlide();
                s.add(barChart);
                s.revalidate();
                s.repaint();
                s.wasModified();;
                SlideCraftFrame.getInstance().getModel().getSlides().getHelper().placedComponent(s,barChart);
            }else
                return;
        }catch(NumberFormatException e){
            return;
        }
        
    }
}
