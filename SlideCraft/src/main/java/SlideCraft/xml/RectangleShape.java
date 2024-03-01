package SlideCraft.xml;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Graphics2D;

public class RectangleShape extends Shape{
    public RectangleShape(int x, int y, int width, int height){
        super(x, y, width, height);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(this.colour);
        g2d.setStroke(new BasicStroke(borderWidth));
        g2d.drawRect(borderWidth,borderWidth, width-borderWidth*2, height-borderWidth*2);
        if (this.fillColor != null){
            g2d.setColor(this.fillColor);
            g2d.fill(new Rectangle(borderWidth,borderWidth, width-borderWidth*2, height-borderWidth*2));
        }
    }
}