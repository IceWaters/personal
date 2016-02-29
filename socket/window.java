/*************************************************************************
	> File Name: window.java
	> Author: Tu Fanglei
	> Mail: 
	> Created Time: Wed 11 Nov 2015 05:05:18 PM CST
 ************************************************************************/

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Container;
import java.awt.*;

class WindowText extends JFrame
{
    private JTextField
        getJText = new JTextField(),
        setJText = new JTextField();
    public String s =" ";
    
    /*
    //to get the text from the JTextField getJText;
    private ActionListener getText = new ActionListener(){
        public void actionPerformed(ActionEvent e){
            s = getJText.getText();
            setJText.setText(s);
        }
    };
    //to set the text to the JTextField setJText;
    private ActionListener setText = new ActionListener(){
        public void actionPerformed(ActionEvent e){
            setJText.setText("nihao");
            setJText.setText(s);
        }
    };
    //*/

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == getJText){
            s = getJText.getText();
            setJText.setText(s);
        }
    }
    public WindowText(){
       // JFrame frame = new JFrame("chat");
       // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // frame.setVisible(true);
        this.setSize(600,400);
        this.setTitle("chat");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
      // getJText.addActionListener(getText);
      //  setJText.addActionListener(setText);
        JLabel user = new JLabel("friend");
      //  frame.setLayout(new FlowLayout());
        this.setLayout(null);
      //  this.add(BorderLayout.NORTH, user);
      //  this.add(BorderLayout.CENTER, setJText);
      //  this.add(BorderLayout.SOUTH, getJText);
        user.setBounds(new Rectangle(10, 10, 50, 20));
        this.add(user);
        setJText.setBounds(new Rectangle(10, 31, 590, 300));
        this.add(setJText);
        getJText.setBounds(new Rectangle(10, 331, 590, 60));
        this.add(getJText);
    }
}
public class window{
	public static void main(String args[])
	{
		WindowText win = new WindowText();
	}

}

