/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author chanchan
 */
public class Fruit {
    protected int x, y;
    protected int pointVal;
    protected BufferedImage image;

    public Fruit(int x, int y, int pointVal, String imagePath) {
        this.x = x;
        this.y = y;
        this.pointVal = pointVal;
        try {
            this.image = ImageIO.read(getClass().getResource(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading image: " + imagePath);
        }
    }

    public void draw(Graphics graphics, int unitSize) {
        graphics.drawImage(image, x, y, unitSize, unitSize, null);
    }
}
