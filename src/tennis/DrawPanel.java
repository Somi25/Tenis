package tennis;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

abstract class DrawPanel extends JPanel
{	
	private static final int WIDTH_WINDOW = 1280;
	private static final int HEIGHT_WINDOW = 720;
	
	protected List<Point> ball;
	protected List<Point> racket_1;
	protected List<Point> racket_2;

    public DrawPanel() 
    {
    	ball = new ArrayList<>();
    	racket_1 = new ArrayList<>();
    	racket_2 = new ArrayList<>();
    }
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH_WINDOW, 10);
        g.fillRect(0, 630, WIDTH_WINDOW, 10);
        for (Point ball : ball)
        {
        	g.fillOval(ball.x-10, ball.y-10, 20, 20);
        }
        
        for (Point racket_1 : racket_1)
        {
        	g.fillRect(racket_1.x-5, racket_1.y-50, 10, 100);
        }
        
        for (Point racket_2 : racket_2)
        {
        	g.fillRect(racket_2.x-5, racket_2.y-50, 10, 100);
        }
    }


}