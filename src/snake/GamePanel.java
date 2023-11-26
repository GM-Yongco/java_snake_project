package snake;

//==================================================
// IMPORTS
//==================================================

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{

    public void printf(Object x){
        System.out.println(x);
    }
    
    private static final long serialVersionUID = 1L;

    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int NUMBER_OF_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

    // hold x and y coordinates for body parts of the snake
    final int x[] = new int[NUMBER_OF_UNITS];
    final int y[] = new int[NUMBER_OF_UNITS];

    // initial length of the snake
    int length = 5;
    int foodEaten;
    int foodX;
    int foodY;
    char direction = 'R';
    boolean running = false;
    Random random;
    Timer timer;
    
    //fruit
    Fruit currentFruit;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.DARK_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        currentFruit = new Fruit(0, 0, 1);
        play();
        
         this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                if(!running){
                // Check if the click was within the bounds of the restart cuz those coordinates is where Restart is
                if (mouseX >= 150 && mouseX <= 260 && mouseY >= 370 && mouseY <= 420) {
                    
                    restartGame();
                }
                else if (mouseX >= 390 && mouseX <= 500 && mouseY >= 380 && mouseY <= 410) {
                        
                     System.exit(0);
                }
                }
            }
        });
        
    }	
    private void restartGame() {
        length = 5; // Reset snake length
        foodEaten = 0; // Reset score
        direction = 'R'; // Reset snake direction
        running = true; // Set game state to running

        int initialSnakeX = WIDTH / 2; // Set initial X coordinate
        int initialSnakeY = HEIGHT / 2; // Set initial Y coordinate

         for (int i = 0; i < length; i++) {
            x[i] = initialSnakeX - i * UNIT_SIZE; // Adjust X-coordinate for each segment
            y[i] = initialSnakeY; // Set Y-coordinate (same for all segments)
        }

        // Add initial food after restarting
        addFood();

        // Start the game loop again if needed
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    public void play() {
        addFood();
        running = true;
        timer = new Timer(80, this);
        timer.start();	
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void move() {
        int i;
        for (i = length; i > 0; i--) {
            // shift the snake one unit to the desired direction to create a move
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        if (direction == 'L') {
            x[0] = x[0] - UNIT_SIZE;
        } else if (direction == 'R') {
            x[0] = x[0] + UNIT_SIZE;
        } else if (direction == 'U') {
            y[0] = y[0] - UNIT_SIZE;
        } else {
            y[0] = y[0] + UNIT_SIZE;
        }	
    }

    public void checkFood() {
        if(x[0] == foodX && y[0] == foodY) {
            length++;
            foodEaten += currentFruit.pointVal; //adds fruit's pointVal to the score
            addFood();
        }
    }

    public void draw(Graphics graphics) {

        if (running) {            
            currentFruit.draw(graphics, UNIT_SIZE); //shows fruit !

            graphics.setColor(Color.white);
            graphics.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);

            for (int i = 1; i < length; i++) {
                    graphics.setColor(new Color(40, 200, 150));
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            graphics.setColor(Color.white);
            graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + foodEaten, (WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2, graphics.getFont().getSize());

        } else {
            gameOver(graphics);
        }
    }

    public void addFood() {
        foodX = random.nextInt((int)(WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        foodY = random.nextInt((int)(HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

        int randomValue = random.nextInt(10);
        
        if(randomValue < 7){ //70% chance
            currentFruit = new Fruit(foodX, foodY, 1);
        }else if(randomValue >= 7 && randomValue < 9){ //20%
            currentFruit = new Banana(foodX, foodY, 3);
        }else{ //remaining 10% chance
            currentFruit = new Orange(foodX, foodY, 5);
        }
        
    }


    public void checkHit() {
        // check if head run into its body
        for (int i = length; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }

        // check if head run into walls
        if (x[0] < 0 || x[0] > WIDTH || y[0] < 0 || y[0] > HEIGHT) {
            running = false;
        }

        if(!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 50));
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);

        graphics.setColor(Color.white);
        graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
        metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + foodEaten, (WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2, graphics.getFont().getSize());
        
        graphics.setColor(Color.white);
        graphics.setFont(new Font("Ink Free", Font.ROMAN_BASELINE, 30));        
        graphics.drawString("Restart", 150, 400);
        
        graphics.setColor(Color.white);
        graphics.setFont(new Font("Ink Free", Font.ROMAN_BASELINE, 30));
        graphics.drawString("Exit", 390, 400);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (running) {
                move();
                checkFood();
                checkHit();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;		
            }
        }
    }
}
