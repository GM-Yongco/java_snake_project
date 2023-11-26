/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake;

import java.awt.*;

/**
 *
 * @author chanchan
 */
public class Fruit {
    
    int x, y;
    int pointVal; //points per fruit
    
    public Fruit(int x, int y, int pointVal){
        this.x = x;
        this.y = y;
        this.pointVal = pointVal;
    }
    
    public void draw(Graphics graphics, int unitSize){
        graphics.setColor(new Color(210, 115, 90));
        graphics.fillOval(x, y, unitSize, unitSize);
    }
}

class Banana extends Fruit{
    public Banana(int x, int y, int pointVal){
        super(x, y, pointVal);
    }
    
    @Override
    public void draw(Graphics graphics, int unitSize){
        graphics.setColor(new Color(255, 255, 0));
        graphics.fillOval(x, y, unitSize, unitSize);
    }
}

class Orange extends Fruit{
    public Orange(int x, int y, int pointVal){
        super(x, y, pointVal);
    }
    
    @Override
    public void draw(Graphics graphics, int unitSize){
        graphics.setColor(new Color(255, 165, 0));
        graphics.fillOval(x, y, unitSize, unitSize);
    }
}
