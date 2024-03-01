package SlideCraft.xml;

import java.awt.geom.Dimension2D;

public class SlideObject {
    protected int x;
    protected int y;
    protected Dimension2D size;
    protected double scale;
    
    public SlideObject(int x, int y, Dimension2D size, double scale){
        this.x = x;
        this.y = y;
        this.size = size;
        this.scale = scale;
    }

    public void setSize(Dimension2D size){
        this.size = size;
        this.size.setSize(this.size.getWidth()*this.scale, this.size.getHeight()*this.scale);
    }

    public void setScale(double scale){
        this.scale = scale;
        this.size.setSize(this.size.getWidth()*scale, this.size.getHeight()*scale);
    }
    
} 