import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.awt.event.*;
/*the above will include all of the following:
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
*/

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25; //How bit we want a object to be
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE; // This will determin the maximum units we can have on the screen
    static final int DELAY = 75; //the higher the number the slower the game is

    final int x[] = new int[GAME_UNITS]; //hold all of the x coordinates of the body parts including the head of our snake
    final int y[] = new int[GAME_UNITS]; //hold all of the y coordinates
    int bodyParts = 6; //begin with 6 body parts of the snake
    int applesEaten = 0;
    int appleX; // the x coordinates of where the apple is
    int appleY; // the y coordinate of where the apple is

    //'R' for right, 'L' for left, 'U' for up, 'D' for down
    char direction = 'R'; //have the snake going right by the beginning

    boolean running = false;
    Timer timer;
    Random random;

    //Constructor
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); //Set the size of the game panel
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        newApple(); //create an apple in the game
        running = true;  //set it to true as it's false to begin with
        timer = new Timer(DELAY, this); //DELAY dictates how fast the game is running, passing in this because we're using the action listener interface
        timer.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //Draw the game when not yet gameOver (aka running = true)
        if(running) {
            /* we are creating the following for loop to draw gridlines in our game where each cell represents one unit size
            so that it will be easier for us to visualize the unit's position
             */
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); //means drawing line from x1y1 to x2y2
            }

            for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }

            //Draw the apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); //Filloval(x1, y1, size of width, size of height)

            //Draw the snake
            for (int i = 0; i < bodyParts; i++) {
                //fill the head of the snake
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                //fill the body of the snake
                else {
                    //use the RGB value to create a different shade of green using new Color() to differentiate body and head
                    g.setColor(new Color(45, 180, 0));
                    //can also use the following to generate random color snake
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            //Draw the score
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 40)); //Ink Free is a font name
            //FontMetrics are useful in aligning up text in the center of the screen, can reference from the gameOver() method
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2,g.getFont().getSize());
        }
        //draw gameOver when running = false, which is triggered by snake eating itself or touch the border
        else{
            gameOver(g);
        }
    }

    //this function is to generate a new apple
    public void newApple(){
        //apple appear somewhere along the x axis and y axis
        //We cast the SCREEN_WIDTH/UNIT_SIZE and SCREEN_HEIGHT/UNIT_SIZE to int just to be safe so it doesn't crash our program
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    //method to move the snake
    public void move(){
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;

            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;

            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;

            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
        }
    }

    public void checkApple(){
        if((x[0] == appleX) && (y[0]==appleY)){
            bodyParts++;  //everytime the snake eats an apple, the size of the snake increase by 1
            applesEaten++; // records the score
            newApple(); //everytime the snake eats an apple, generate a new apple
        }
    }

    public void checkCollisions(){
        //check if snake head collides with snkae body
        for(int i = bodyParts; i>0 ; i--){
            //if head of the snake collides with any of its body parts, call game over (running = false)
            if((x[0]==x[i])&& (y[0]==y[i])){
                running = false;
            }
        }

        //check if head touches border
        if(!((0 <= x[0])&&(x[0] < SCREEN_WIDTH)) | !((0 <= y[0])&&(y[0] < SCREEN_HEIGHT)) ){
            running = false;
        }

        //if not running (aka game over) then stop the timer
        if(!running){
            timer.stop();
        }
    }

    //game over screen
    public void gameOver(Graphics g){
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75)); //Ink Free is a font name

        //FontMetrics are useful in aligning up text in the center of the screen using the following two line
        //metrics can be used to calculate the x coordinate of the drawstring to make it to the center
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2,(SCREEN_HEIGHT/2));


        //Draw the score
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 40)); //Ink Free is a font name
        //FontMetrics are useful in aligning up text in the center of the screen, can reference from the gameOver() method
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten,(SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten))/2,g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction!= 'R'){  //avoid user doing 180 degree turn and gameover
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if (direction != 'L'){
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if (direction != 'D'){
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if(direction!= 'U'){
                        direction = 'D';
                    }
                    break;
            }
            super.keyPressed(e);
        }
    }
}
