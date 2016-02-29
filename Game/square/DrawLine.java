import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;

public class DrawLine extends JFrame {
	public static void main(String[] args) {
		new DrawLine();
	}
	public DrawLine() {
		setSize(500,300);
		setVisible(true);

	}
	public void paint(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect(10, 10, 10, 10);
	}
}
