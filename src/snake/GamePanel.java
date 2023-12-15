package snake;

//==================================================
// IMPORTS
//==================================================

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.Arrays ;

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
    
    boolean can_change_direction = true;
    
    //fruit
    Fruit currentFruit;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.DARK_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        currentFruit = new Fruit(0, 0, 1, "");
        play();
        
        Arrays.fill(x, -1);
        Arrays.fill(y, -1);
        
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

        Arrays.fill(x, -1);
        Arrays.fill(y, -1);
        
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
        
        Arrays.fill(x, -1);
        Arrays.fill(y, -1);
        
        addFood();
        running = true;
        timer = new Timer(80, this);
        timer.start();	
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // Draw the background grid
        drawBackground(graphics);

        // Draw the game elements (snake, fruit, score, etc.)
        draw(graphics);
    }
    
    // New method to draw the background grid
    private void drawBackground(Graphics graphics) {
        // Draw the grid of green and light green boxes
        for (int row = 0; row < HEIGHT / UNIT_SIZE; row++) {
            for (int col = 0; col < WIDTH / UNIT_SIZE; col++) { 
                // Alternate between green and light green based on row and column indices
//                Color boxColor = (row + col) % 2 == 0 ? new Color(99, 229, 80) : new Color(177, 243, 73);
//                Color boxColor = (row + col) % 2 == 0 ? new Color(194, 247, 143) : new Color(178, 241, 112);
                Color boxColor = (row + col) % 2 == 0 ? new Color(186, 249, 133) : new Color(168, 237, 104);

                graphics.setColor(boxColor);
                graphics.fillRect(col * UNIT_SIZE, row * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }
        }
    }

    public void move() {
        for (int i = length - 1; i > 0; i--) {
            // shift the snake one unit to the desired direction to create a move
            x[i] = x[i - 1];
            y[i] = y[i - 1];
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
        
        //lets the snake tp to the other side
        if (x[0] < 0) {
            x[0] = WIDTH - UNIT_SIZE;
        } else if (x[0] >= WIDTH) {
            x[0] = 0;
        }

        if (y[0] < 0) {
            y[0] = HEIGHT - UNIT_SIZE;
        } else if (y[0] >= HEIGHT) {
            y[0] = 0;
        }

    }

    public void draw(Graphics graphics) {
    if (running) {
        // Adjust the fruit position based on the snake's head position
        currentFruit.draw(graphics, UNIT_SIZE);

        graphics.setColor(new Color(136, 92, 152));  
        graphics.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);
        // Draw an arc to create the curved tip of the head
         graphics.fillArc(x[0] - UNIT_SIZE / 2, y[0], UNIT_SIZE * 2, UNIT_SIZE, 45, 90); 
        graphics.setColor(Color.BLACK);
        graphics.fillOval(x[0] + 3, y[0] + 3, 6, 6); // Left eye
        graphics.fillOval(x[0] + 13, y[0] + 3, 6, 6); // Right eye

        // Draw the body and tail with the same color
        for (int i = 1; i < length; i++) {
            if(x[i] == -1 || y[i] == -1){
                break;
            } 
            
            graphics.setColor(new Color(166, 142, 192));  // Same color as the head
            graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        }
        
        // Draw score
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
            currentFruit = new Fruit(foodX, foodY, 1, "/assets/pictures/apple.png");
        } else if(randomValue >= 7 && randomValue < 9){ //20%
            currentFruit = new Banana(foodX, foodY, 3, "/assets/pictures/banana.png");
        } else { //remaining 10% chance
            currentFruit = new Orange(foodX, foodY, 5, "/assets/pictures/orange.png");
        }
    }
    
    public void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            length += currentFruit.pointVal;
            foodEaten += currentFruit.pointVal; // adds fruit's pointVal to the score
            playEatingSound(); // play the eating sound
            addFood();
        }
    }
    
    private void playEatingSound() {
        try {
            // Load the audio file using class loader (assumes it's in the classpath)
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    getClass().getResource("/assets/sounds/fruit-eat.wav")
            );

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Play the clip
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
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
        
        //HANDLE THE TICKSPEED OF THE GAME
        can_change_direction = true;
        
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repaint();
        
        
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(can_change_direction){
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
                can_change_direction = false;
                //basically only allows changes in direction every game tick                
            }
            
        }
    }
}
