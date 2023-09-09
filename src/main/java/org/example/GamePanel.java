package org.example;
import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.awt.event.*;


public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 1200;
    static final int SCREEN_HEIGHT = 1200;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'E';
    boolean running = false;
    Timer timer;
    Random random;
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        //reseting Snake
        bodyParts = 6;
        applesEaten = 0;
        direction = 'E';
        for(int i = 0; i < bodyParts; i++){
            x[i] = 0;
            y[i] = 0;
        }
        running = true;
        newApple();
        if(timer != null){
            timer.stop();
        }
        timer = new Timer(DELAY, this);
        timer.start();

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){

        if(running){
        // creating the grid
            for(int i = 0; i< SCREEN_HEIGHT/UNIT_SIZE; i++){
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);

            }
            // creating apple random loc
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // creating the snake body + head
            for(int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                g.setColor(Color.white);
                g.setFont(new Font("Noto Mono", Font.BOLD, 50));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("Score : " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score : " + applesEaten))/2, g.getFont().getSize());
            }
        }else{
            gameOver(g);
        }
    }
    public void newApple(){
        //random location for apple
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
    }
    public void move(){
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch(direction){
            case'N':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case'S':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case'E':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case'W':
                x[0] = x[0] - UNIT_SIZE;
                break;

        }
    }
    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions(){
        // if head collides with body
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // if head touches left border
        if(x[0] < 0){
            running = false;
        }
        // if head touches right border
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }
        // if head touches top border
        if(y[0] < 0){
            running = false;
        }

        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }
        if(!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        g.setColor(Color.white);
        g.setFont(new Font("Noto Mono", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, (SCREEN_HEIGHT/2));
        g.setColor(Color.white);
        g.setFont(new Font("Noto Mono", Font.BOLD, 50));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score : " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score : " + applesEaten))/2, g.getFont().getSize());
        g.setFont(new Font("Noto Mono", Font.BOLD, 50));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press Space to Restart", (SCREEN_WIDTH - metrics3.stringWidth("Press Space to Restart"))/2, (SCREEN_HEIGHT*3/5));
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

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT, KeyEvent.VK_A:
                    if(direction != 'E'){
                        direction = 'W';
                    }
                    break;
                case KeyEvent.VK_RIGHT, KeyEvent.VK_D:
                    if(direction != 'W'){
                        direction = 'E';
                    }
                    break;
                case KeyEvent.VK_UP, KeyEvent.VK_W:
                    if(direction != 'S'){
                        direction = 'N';
                    }
                    break;
                case KeyEvent.VK_DOWN, KeyEvent.VK_S:
                    if(direction != 'N'){
                        direction = 'S';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if(!running) {
                        startGame();
                    }
                    break;
            }

        }
    }
}
