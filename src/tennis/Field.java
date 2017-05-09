package tennis;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.ImageIcon;

public class Field extends DrawPanel
{
	
	private int i = 11;
	private int j = 11;
	
	protected DrawPanel field_panel;
	protected JPanel score_panel;
	protected JLabel score_label;
	protected JLabel light_1_label;
	protected JLabel light_2_label;
	
	public Field(int WIDTH_WINDOW)
	{
		//Pálya
		setLayout(null);
		setBounds(0, 80, WIDTH_WINDOW,640);
		setBackground(Color.black);
		setFocusable(true);
		
		//Billentyűzet gombok
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pause");
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "pressed 1_up");
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "released 1_up");
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "pressed 1_down");
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "released 1_down");
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "pressed 2_up");
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "released 2_up");
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "pressed 2_down");
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "released 2_down");		
		
		//Eredményjelzõ
		score_panel = new JPanel();
		score_panel.setLayout(null);
		score_panel.setBounds(0, 0, WIDTH_WINDOW,80);
		score_panel.setBackground(Color.black);
		score_panel.setVisible(false);
		
		score_label = new JLabel("0 : 0");
		score_label.setBounds(WIDTH_WINDOW/2-81, 0, 162, 80);
		score_label.setFont(new Font("0 : 0", Font.BOLD, 80));
		score_label.setForeground(Color.WHITE);
		score_panel.add(score_label);
		
		try {
			light_1_label = new JLabel(new ImageIcon(ImageIO.read( Thread.currentThread().getContextClassLoader().getResourceAsStream("light.png"))));
		 } catch (IOException exp) {
	           exp.printStackTrace();
	       }
		
		try {
			light_2_label = new JLabel(new ImageIcon(ImageIO.read( Thread.currentThread().getContextClassLoader().getResourceAsStream("light.png"))));
		 } catch (IOException exp) {
	           exp.printStackTrace();
	       }
		light_1_label.setBounds(315, 0, 20, 80);
		light_2_label.setBounds(945, 0, 20, 80);
		light_1_label.setVisible(false);
		light_2_label.setVisible(false);
		score_panel.add(light_1_label);
		score_panel.add(light_2_label);	
        
		setVisible(false);
	}
	
	//Labda, ütők kirajzolása
    public void ball(int x, int y) {
    	ball.clear();
    	ball.add(new Point(x, y));        	
    	repaint();
    }
    
    public void racket_1(int x, int y) {
    	racket_1.clear();
    	racket_1.add(new Point(x, y));
    	repaint();
    }
    
    public void racket_2(int x, int y) {
    	racket_2.clear();
    	racket_2.add(new Point(x, y));
        repaint();
    }
    
    public void setScore(String score){
    	score_label.setText(score);
    }
    
    //gomb esemény aktiválás
    protected void add_action(AbstractAction pause_action, AbstractAction pressed_1_up_action, AbstractAction released_1_up_action, AbstractAction pressed_1_down_action, AbstractAction released_1_down_action, AbstractAction pressed_2_up_action, AbstractAction released_2_up_action, AbstractAction pressed_2_down_action, AbstractAction released_2_down_action)
    {
    	getActionMap().put("pause", pause_action);
    	getActionMap().put("pressed 1_up", pressed_1_up_action);
    	getActionMap().put("released 1_up", released_1_up_action);
    	getActionMap().put("pressed 1_down", pressed_1_down_action);
    	getActionMap().put("released 1_down", released_1_down_action);
    	getActionMap().put("pressed 2_up", pressed_2_up_action);
    	getActionMap().put("released 2_up", released_2_up_action);
    	getActionMap().put("pressed 2_down", pressed_2_down_action);
    	getActionMap().put("released 2_down", released_2_down_action);
    }
}