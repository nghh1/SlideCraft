package SlideCraft.ui.helper;

import java.awt.*;

import javax.swing.*;

import SlideCraft.data.DataModel;
import SlideCraft.ui.SlideCraftFrame;

public class PercentageSlider extends JSlider{
    private JLabel percentageLabel;
    private int value;
    public PercentageSlider(int min, int max){
        super(min, max);
        setPaintTicks(true);
        setForeground(Color.WHITE);
        percentageLabel = new JLabel();
        setPreferredSize(new Dimension(200, 30));
    }

    public void updatePercentageLabel(DataModel model) {
        setPaintLabels(true);
        this.value = getValue();
        String label = value+"%";
        percentageLabel.setText(label);
        model.getSlides().updateSlidesSize(this.value);
    }

    public int getPercentageValue(){
        return this.value;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(SlideCraftFrame.interfaceMainColor);
        g.fillRect(0,0, getWidth(), getHeight());
        super.paintComponent(g);
        int percentage = (int) (((double)getValue()/(double)getMaximum())*100);
        String label = percentage+"%";
        FontMetrics metrics = g.getFontMetrics();
        int x = (getWidth()-metrics.stringWidth(label))/2;
        int y = getHeight()-5;
        g.setColor(Color.WHITE);
        g.drawString(label, x, y);
    }
}