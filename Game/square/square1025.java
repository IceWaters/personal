import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import java.lang.Number;
import java.lang.Math;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

public class square extends JFrame {
    public final static int squareLength = 40;    //the length of each square
    public final static int numWalls_X = 10;
    public final static int numWalls_Y = 16;     
    public final static int wallLength_X = squareLength * numWalls_X;    
    public final static int wallLength_Y = squareLength * numWalls_Y;

    static Map<Integer,Integer> squares = new HashMap<Integer,Integer>();
    static Map<Integer,Integer> movingSquares = new HashMap<Integer, Integer>();
    static int[] nextSquares = new int[5];
    static int[] numOfRow = new int[numWalls_Y];
    
    public static void main(String args[]){
        nextSquares[0] = 7;
        square newGame = new square();
        squarePosition squarePos = new squarePosition();

        squarePos.initialize();
        squarePos.nextType();
        newGame.repaint();

        boolean isMoving = true;
        while(isMoving){
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){}
            newGame.repaint();
            try{
                Thread.sleep(30);
            }catch(InterruptedException e){}
            System.out.println("PAINT is ok!!!");
            isMoving = squarePos.nextPosition();
        }
        System.out.println("bad!!");
    }

    public square(){
        this.setTitle("Sqaure Game ");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(wallLength_X+3*squareLength+120,wallLength_Y+100);
        this.setVisible(true);
        this.addKeyListener(new MyKeyListener());
        MyPanel mp = new MyPanel();
        this.add(mp);
    }

    private class MyPanel extends JPanel{

        public void paint(Graphics g){//{{{
            super.paint(g);
            System.out.println("OK");
            Font f1 = new Font("Dorid Serif", Font.BOLD,28);
            String next = "Next";
            g.setFont(f1);
            g.drawString(next,87+wallLength_X,49+squareLength);
            String score = "Score";
            g.drawString(score,84+wallLength_X,450);
            g.drawRect(29,29,wallLength_X+2,wallLength_Y+2);
            g.drawRect(60+wallLength_X,29+2*squareLength,3*squareLength+32,6*squareLength+2);

        if(!squares.isEmpty()){//{{{
            for (Map.Entry<Integer, Integer> entry : squares.entrySet()) {
                int point = entry.getKey().intValue();
                int color = entry.getValue().intValue();
                int points_Y = point / (numWalls_X + 2);
                int points_X = point % (numWalls_X + 2) - 1;
                System.out.println("squares : points_X : "+points_X);
                System.out.println("squares : points_Y : "+points_Y);
                g.setColor(Color.BLACK);
                g.drawRect(30+points_X * squareLength, 30+points_Y * squareLength, squareLength, squareLength); 
                switch(color){
                    case 0: g.setColor(Color.GREEN); break;
                    case 1: g.setColor(Color.BLUE); break;
                    case 2: g.setColor(Color.RED); break;
                    case 3: g.setColor(Color.PINK); break;
                    case 4: g.setColor(Color.YELLOW); break;
                }
                g.fillRect(31+points_X * squareLength, 31+points_Y * squareLength, squareLength - 2, squareLength - 2); 
            }
        }//}}}

        if(!movingSquares.isEmpty()){//{{{
            for (Map.Entry<Integer, Integer> entry : movingSquares.entrySet()) {
                int point = entry.getKey().intValue();
                int color = entry.getValue().intValue();
                int points_Y = point / (numWalls_X + 2);
                int points_X = point % (numWalls_X + 2) - 1;
                System.out.println("movingSquares : points_X : "+points_X);
                System.out.println("movingSquares : points_Y : "+points_Y);
                g.setColor(Color.BLACK);
                g.drawRect(30+points_X * squareLength, 30+points_Y * squareLength, squareLength, squareLength); 
                switch(color){
                    case 0: g.setColor(Color.GREEN); break;
                    case 1: g.setColor(Color.BLUE); break;
                    case 2: g.setColor(Color.RED); break;
                    case 3: g.setColor(Color.PINK); break;
                    case 4: g.setColor(Color.YELLOW); break;
                }
                g.fillRect(31+points_X * squareLength, 31+points_Y * squareLength, squareLength - 2, squareLength - 2); 
            }
        }//}}}

        if(nextSquares[0] < 6){//{{{
            int color = nextSquares[4];
            int X;
            //to judge the start posiotion of the next type
            if(nextSquares[2] == numWalls_X+1){
                X = 100 + wallLength_X; 
            }
            else
                X = 100 + wallLength_X - squareLength;
            int Y = 29 + 3 * squareLength;
            for (int i = 0; i < 4; i++) {
                int point = nextSquares[i] + 1;
                
                int points_Y = point / (numWalls_X + 2);
                int points_X = point % (numWalls_X + 2);
                System.out.println("nextSquares: points_X : "+points_X);
                System.out.println("nextSquares: points_Y : "+points_Y);
                g.setColor(Color.BLACK);
                g.drawRect(X+points_X * squareLength, Y+points_Y * squareLength, squareLength, squareLength); 
                switch(color){
                    case 0: g.setColor(Color.GREEN); break;
                    case 1: g.setColor(Color.BLUE); break;
                    case 2: g.setColor(Color.RED); break;
                    case 3: g.setColor(Color.PINK); break;
                    case 4: g.setColor(Color.YELLOW); break;
                }
                g.fillRect(X + 1 +points_X * squareLength, Y + 1 +points_Y * squareLength, squareLength - 2, squareLength - 2); 
            }
        }//}}}
            System.out.println("paint is ok!!!");
            return ;
        }
    }
}

/*
 *    1        2       3          4         5                6
 *    _       _         _ 
 *   |1|     |1|_     _|1|       _ _        _
 *   |_|_    |_|_|   |_|_|      |1|_|     _|1|_           _ _ _ _
 *   |_|_|     |_|   |_|        |_|_|    |_|_|_|         |1|_|_|_|  vertical
 *  
 *   three   right    left    bigSquare   center         rectangle
 *
 */

class squarePosition{
    final static int[] three = {0, square.numWalls_X + 2, 2 * (square.numWalls_X + 2), 2 * (square.numWalls_X + 2)+1}; 
    final static int[] right = {0, square.numWalls_X + 2, square.numWalls_X + 3, 2 * (square.numWalls_X + 2) + 1};
    final static int[] left = {0, square.numWalls_X + 2, square.numWalls_X + 1, 2 * (square.numWalls_X + 2) - 1};
    final static int[] bigSquare = {0, 1, square.numWalls_X + 2, square.numWalls_X + 2 + 1};
    final static int[] center = {0, square.numWalls_X + 1, square.numWalls_X + 2, square.numWalls_X + 3};
    final static int[] rectangle = {0, square.numWalls_X + 2, 2 * (square.numWalls_X + 2), 3 * (square.numWalls_X + 2)};
    
//    static char Num;  
    static int[] numToShape(char num){
        int[] shape = new int[4];

        switch(num){
            case 0: shape = three; break;
            case 1: shape = right; break;
            case 2: shape = left; break;
            case 3: shape = bigSquare; break;
            case 4: shape = center; break;
            case 5: shape = rectangle; break;
        }
        
        for(int i = 0; i < 4; i ++ )
            System.out.println(shape[i]);
        return shape;
    }

    static boolean legal(Map<Integer, Integer> map){
    //to judge whether the next position is out of the wall
        if(!map.isEmpty()){
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                int point = entry.getKey().intValue();
                System.out.println("point : "+ point);
                if(point > 0 && point < (square.numWalls_X + 2) * square.numWalls_Y)
                    if(point % (square.numWalls_X + 2) !=  0 && (point + 1) %(square.numWalls_X + 2) != 0)
                        continue;
                return false;
            }
        }

        return true;
    }

    static boolean knockSquares(Map<Integer, Integer> map){
    //to judge whether the next position is already be filles with other static squares
        if(!square.squares.isEmpty()){
            for (Map.Entry<Integer, Integer> squaresEntry : square.squares.entrySet()) {
                    int pointFilled = squaresEntry.getKey().intValue();
                for (Map.Entry<Integer, Integer> mapEntry : map.entrySet()) {
                    int point = mapEntry.getKey().intValue();
                    if(pointFilled == point)
                        return true;
                }
            }
        }
        return false;
    }

    static void nextType(){
            char num = (char) (Math.random()*6);
            int[] shape = numToShape(num);
            for(int i = 0; i < 4; i++)
                square.nextSquares[i] = shape[i];

            square.nextSquares[4] = (int)(Math.random() * 5) ;
    }

    public void initialize(){
        square.squares.clear();
        square.nextSquares[0] = 7;
        do{
            square.movingSquares.clear();
            //to gengerate the shape randomly
            char num = (char) (Math.random()*6);
            int[] shape = numToShape(num);

            //to gengerate the position of the square
            int head = (int) (Math.random()*square.numWalls_X) + 1;
            int color = (int)(Math.random() * 5) ;
            System.out.println("Color : " + color);
            for(int i = 0; i < 4; i++){
                square.movingSquares.put(new Integer(head + shape[i]), new Integer(color));
            }
        }while(!legal(square.movingSquares));
    }

    public boolean nextPosition(){
        Map<Integer, Integer> temp = new HashMap<Integer, Integer> ();
        int head = (square.numWalls_X + 2) * square.numWalls_Y;
        if(!square.movingSquares.isEmpty()){//{{{//{{{
            for (Map.Entry<Integer, Integer> entry : square.movingSquares.entrySet()) {
                int point = entry.getKey().intValue();
                if(point  < head ){
                //to get the head of the square
                    head = point;
                }
                point = point + square.numWalls_X + 2;
                Integer color = entry.getValue();
                temp.put(new Integer(point), color);
            }
        }//}}}//}}}
        
        if(legal(temp) && !knockSquares(temp)){
        //the square is still moving   
            System.out.println("It is moving!!!");
            square.movingSquares = temp;
            return true;
        }
        else{
        //the square stopped
            if(head < (square.numWalls_X + 2) && head > 0){
            //the square stopped at the top, the game is over
                System.out.println("Game Over!!!");
                return false;
            }
            else{
            //next square
                if(!square.movingSquares.isEmpty()){//{{{
                    for (Map.Entry<Integer, Integer> entry : square.movingSquares.entrySet()) {
                        square.squares.put(entry.getKey(),entry.getValue());
                    }
                }//}}}
                
                do{
                    square.movingSquares.clear();
                    //the next square is for sure, we just need to confire its head position
                    int newHead = (int) (Math.random()*square.numWalls_X) + 1;
                    for(int i = 0; i < 4; i++){
                        square.movingSquares.put(new Integer(newHead + square.nextSquares[i]), new Integer(square.nextSquares[4]));
                    }
                    System.out.println("testing next Square");
                }while(!legal(square.movingSquares));
                nextType();
                System.out.println("Next Square!!!");
                return true;
            }
        }
    }
}


/*
 * to listen the keyboard and control the movement of the squares
 * up: turn the direction of the square
 * down: increase the speed
 * left: move to the left 
 * rightht: move to the right
 */
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
        

    }
}
