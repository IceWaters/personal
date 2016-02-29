import java.awt.Color;
import java.awt.Graphics;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Number;
import java.lang.Math;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class snake extends JFrame {
    public final static int width = 500;    //the scale of the snake
    public final static int height = 300;
    public final static int length = 10;    //the length of each point of the Snake
    public final static int pointsMax_X = width/length;    
    public final static int pointsMax_Y = height/length;
    public final static int initialSize = 4;    //the initial length of the Snake
    
    static char currentDir;    //the current direction of the Snake
    static int applePos = 0; 
    static int speed = 1000;
    static ArrayList<Integer> points = new ArrayList<Integer> ();
 //   static boolean alive  = true;

    public static void main(String args[]){

        snake newSnake = new snake();
        SnakePosition snakePos = new SnakePosition();
        snakePos.initializeSnake();
        newSnake.repaint();
       // newSnake.repaint();
        while(true){
           // Thread.sleep(1000);
            try{
                Thread.sleep(speed);                                                    
            }catch(InterruptedException e){}
           // newSnake.repaint();
            System.out.println("ok");
            boolean alive  = snakePos.nextSnake();
            if(alive == false)
            {
                int n = JOptionPane.showConfirmDialog(null, "       Game Over!\nDo you want to play again?", "Snake Game",JOptionPane.YES_NO_OPTION);
                if(n == 0)
                //to restart the game
                    snakePos.initializeSnake();
                else 
                    System.exit(0);
                System.out.println("The choice is : "+ n);
                System.out.println(alive);
            }
            newSnake.repaint();
        //  newSnake.repaint(21,51,width+40,height+30);
        }
    }

    public snake(){
        this.setTitle("Snake Game ");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width+100,height+100);
        this.setVisible(true);
        this.addKeyListener(new MyKeyListener());
        MyPanel mp = new MyPanel();
        this.add(mp);
    }

    private class MyPanel extends JPanel{

        public void paint(Graphics g){
            super.paint(g);
            System.out.println("OK");
            int length = points.size();
            Font f1 = new Font("Dorid Serif", Font.BOLD,18);
        /*  to show the game over in the center of the walls 
         *  Font f2 = new Font("Dorid Serif", Font.BOLD,38);
            if(alive == false){
                String over = "Game Over!";
                g.setFont(f2);
                g.drawString(over,160,200);
            }
            // */
            String str = "The length : " + String.valueOf(length);
            g.setFont(f1);
            g.drawString(str,29,25);
            //to draw the retangle
            Color Grey = new Color(525252);
            g.setColor(Color.black);
        /*    g.drawLine(20,50,width+21,50);
            g.drawLine(20,height+51,width+21,height+51);
            g.drawLine(20,50,20,height+51);
            g.drawLine(width+21,50,width+21,height+51);
        //*/
            g.drawRect(21,51,width+2,height+2);
            for(int i = 0; i < points.size(); i++){
            //to draw the snake
                int point = snake.points.get(i).intValue();
                System.out.println(i +" : "+point);
                int points_Y = point / (pointsMax_X + 2);
                int points_X = point % (pointsMax_X + 2) - 1;
                System.out.println("points_X : "+points_X);
                System.out.println("points_Y : "+points_Y);
                g.setColor(Color.BLACK);
                g.drawRect(21+points_X*10,51+points_Y*10,10,10); 
                if(i == 0)
                    g.setColor(Color.GRAY);
                else 
                    g.setColor(Color.LIGHT_GRAY);
                g.fillRect(22+points_X*10,52+points_Y*10,8,8);                    
            }

            //to generate the apple and draw it
            if(applePos == 0){
                do{
                //to generate the position of sanke randomly and also need to judge whether the position is legal
                    applePos = (int) (Math.random()*(snake.pointsMax_X + 2)*snake.pointsMax_Y);  
                    System.out.println("applePos : " + applePos);
                }while(SnakePosition.knockWall(applePos));
            }
            int points_Y = applePos / (pointsMax_X + 2);
            int points_X = applePos % (pointsMax_X + 2) - 1;
            g.setColor(Color.RED);
            g.fillRect(20+points_X*10,50+points_Y*10,10,10); 
        }
    }
}

/*
 *The direction fo the snake head to the end:
 * up:0
 * down:1
 * left:2
 * rigth:3
 */
class SnakePosition{

    static boolean knockWall(int point){
        //to judge whether the points of the snake is out of range
        if(point > 0 && point < (snake.pointsMax_X + 2)*snake.pointsMax_Y)
            if(point % (snake.pointsMax_X+2) != 0 && (point+1) % (snake.pointsMax_X + 2) != 0 ){
                return false;
            }
        System.out.println("knockWall : "+ point);
        return true;
    }

    static boolean knockSnake(){
        //to judge whether the snake knock itself
        int head = snake.points.get(0).intValue();

        for(int i = 1; i < snake.points.size(); i++){
            int point = snake.points.get(i).intValue();
            if(point == head){
                System.out.println("knockSnake : "+ i +" " + point);
                return true;
            }
        }

            return false;
    }

    static int dirTodegree(char direction){
        int degree;
        switch(direction){
            case 0: degree = (snake.pointsMax_X + 2); break;
            case 1: degree = -(snake.pointsMax_X + 2); break;
            case 2: degree = 1; break;
            case 3: degree = -1; break;
            default: degree = 0; break;
        }
        return degree;
    } 
    public void initializeSnake(){
        int Head;
        int next;
        boolean bool;
        snake1024.applePos = 0;
        do{
            bool = false;
            snake.points.clear();
            do{
            //to generate the position of the head of snake randomly and also need to judge whether the position is legal
                Head = (int) (Math.random()*(snake.pointsMax_X + 2)*snake.pointsMax_Y);  
                System.out.println("Head : " + Head);
            }while(SnakePosition.knockWall(Head));
            snake.points.add(new Integer(Head));

            //to generate the direction randomly
            snake.currentDir = (char)(Math.random()*4);
            int degree = dirTodegree(snake.currentDir);

            next = Head;
            for(int i = 0; i < (snake.initialSize - 1); i++){                
                next = next + degree;
                System.out.println("next : "+next);
                snake.points.add(new Integer(next));
                bool = bool || SnakePosition.knockWall(next);
            }
        }while(bool);
    }  
    
    public boolean nextSnake(){

        int snakeLength = snake.points.size();
        int Head = snake.points.get(0).intValue() - dirTodegree(snake.currentDir);

        if(knockWall(Head) || knockSnake())
           return false;

        if(Head != snake.applePos)
            snake.points.remove(snakeLength - 1);
        else 
            snake.applePos = 0;
        snake.points.add(0,new Integer(Head));
        return true;
    }
}

//to listen the keyboard and control the direction
class MyKeyListener extends KeyAdapter{
    static int flag = 0;
    public void keyPressed(KeyEvent e){
        char Dir = 5;
        switch(e.getKeyCode()){
            case KeyEvent.VK_UP: Dir = 0; break;
            case KeyEvent.VK_DOWN: Dir = 1; break;
            case KeyEvent.VK_LEFT: Dir = 2; break;
            case KeyEvent.VK_RIGHT: Dir = 3; break;
        }

        if(Dir == snake.currentDir){
            if(flag == 0){
                snake.speed = 500;
                flag++;
            }
            else if(flag == 1){
                snake.speed = 250;
                flag++;
            }
            else if(flag == 2){
                snake.speed = 125;
                flag++;
            }
            else if(flag == 3){
                snake.speed = 1000;
                flag = 0;
            }
        }
        if(Dir/2 != snake.currentDir/2)
        //the snake can not turn to the opposite direction
            snake.currentDir = Dir;
    }
}
