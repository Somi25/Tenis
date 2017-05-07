package tennis;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.JComponent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.swing.ImageIcon;


public class GUI extends JFrame implements ActionListener
{
	private Control ctrl;
	private static final int WIDTH_WINDOW = 1280;
	private static final int HEIGHT_WINDOW = 720;
	private static final int WIDTH_MENU = 240;
	private static final int HEIGHT_MENU = 320;
	private static final int OFFLINE = 1;
	private static final int ONLINE = 2;
	private static final int HOST = 3;
	private static final int CLIENT = 4;
	private static final int GAME = 5;
	private static final int TIMER_DELAY = 20;
	private int	state = 0;
	private int i = 11;
	private int j = 11;
	private boolean pressed_1_up = false;
	private boolean pressed_1_down = false;
	private boolean pressed_2_up = false;
	private boolean pressed_2_down = false;
	
	private Timer time;

	
	private String score = "0 : 0";
	protected Field field_panel;
	//protected JPanel score_panel;
	//private JLabel score_label;
	
	protected Menu menu;
	
	
	public GUI(Control c)
	{		
		super("Tenisz");
		ctrl = c;
		setSize(WIDTH_WINDOW+6, HEIGHT_WINDOW+30);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setResizable(false);		
			
		time = new Timer(TIMER_DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				field_panel.ReFresh(i, j);
				if(i >= 400)
				{
					Object[] button = {"OK"};
					if(JOptionPane.showOptionDialog(field_panel, "Te nyertél","Vége a játéknak", JOptionPane.OK_OPTION,JOptionPane.PLAIN_MESSAGE, null, button, null) == 0)
					{
						time.stop();
						field_panel.setVisible(false);
						field_panel.score_panel.setVisible(false);
						menu.menu_main_panel.setVisible(true);
						i = 11;
						j=11;
					}
				}
		     }
		});	
		
		AbstractAction pause_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
            	 System.out.println("pause");
                 time.stop();
                 menu.pause_panel.setVisible(true);
            }
        };
        
        AbstractAction pressed_1_up_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(pressed_1_up == false)
            	{
	                System.out.println("pressed 1_up");
	                pressed_1_up = true;
	                i = i - 30;
            	}
            }
        };
		
        AbstractAction released_1_up_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("released 1_up");
                pressed_1_up = false;
            }
        };
		
        AbstractAction pressed_1_down_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(pressed_1_down == false)
            	{
            		System.out.println("pressed 1_down");
            		pressed_1_down = true;
            		i = i + 30;
            	}
            }
        };
        
        AbstractAction released_1_down_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
            		System.out.println("released 1_down");
            		pressed_1_down = false;
            }
        };
        
        AbstractAction pressed_2_up_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(pressed_2_up == false)
            	{
	                System.out.println("pressed 2_up");
	                pressed_2_up = true;
	                j = j - 30;
            	}
            }
        };
		
        AbstractAction released_2_up_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("released 2_up");
                pressed_2_up = false;
            }
        };
        
        AbstractAction pressed_2_down_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(pressed_2_down == false)
            	{
            		System.out.println("pressed 2_down");
            		pressed_2_down = true;
            		j = j + 30;
            		score = "0 : 1";
            		field_panel.score_label.setText(score);
            		field_panel.light_1_label.setVisible(true);
            	}
            }
        };
        
        AbstractAction released_2_down_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
            		System.out.println("released 2_down");
            		pressed_2_down = false;
            		field_panel.light_1_label.setVisible(false);
            }
        };

		
		
		menu = new Menu();
		//main_panel.add(menu);
		menu.add_action(this);
		field_panel = new Field();
		field_panel.add_action(pause_action, pressed_1_up_action, released_1_up_action, pressed_1_down_action, released_1_down_action, pressed_2_up_action, released_2_up_action, pressed_2_down_action, released_2_down_action);
		menu.add(field_panel);
		menu.add(field_panel.score_panel);
		field_panel.add(menu.pause_panel);
		setContentPane(menu);
		
		setVisible(true);
	}
	
	@Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {

            //Display panel one when I select the option on the menu bar
            case "Offline":
            	menu.menu_main_panel.setVisible(false);
            	menu.menu_offline_panel.setVisible(true);
            	state = OFFLINE;
                break;
            
            case "Online":
            	menu.menu_main_panel.setVisible(false);
				menu.menu_online_panel.setVisible(true);
				state = ONLINE;
                break;
                
            case "Kilépés":
            	System.exit(0);
                break;
            
            case "Host":
            	menu.host_wait_label.setVisible(true);
				menu.client_error_label.setVisible(false);
				state = HOST;
                break;
            
            case "Kliens":
            	menu.client_error_label.setVisible(true);
				menu.host_wait_label.setVisible(false);
				state = CLIENT;
                break;
            
            case "Új játék":
            	if(state == OFFLINE)
				{
					menu.menu_offline_panel.setVisible(false);
					field_panel.setVisible(true);
					field_panel.score_panel.setVisible(true);
					time.start();
				}
				if(state == HOST)
				{
					menu.host_wait_label.setVisible(true);
					//time.start();
				}
                break;
                
            case "Játék betöltése":
            	if(state == OFFLINE)
				{
					menu.menu_offline_panel.setVisible(false);
					field_panel.setVisible(true);
					field_panel.score_panel.setVisible(true);
					time.start();
				}
				if(state == HOST)
				{
					menu.host_wait_label.setVisible(true);
					//time.start();
				}
                break;

            case "Vissza - offline":
				menu.menu_offline_panel.setVisible(false);
				menu.menu_main_panel.setVisible(true);
				state = 0;
                break;
                
            case "Vissza - online":
            	menu.menu_online_panel.setVisible(false);
				menu.menu_main_panel.setVisible(true);
				menu.host_wait_label.setVisible(false);
				menu.client_error_label.setVisible(false);
				state = 0;
                break;
                
            case "Játék mentése":
            	menu.pause_panel.setVisible(false);
				time.start();
                break;
                
            case "Vissza - pause":
            	menu.pause_panel.setVisible(false);
				time.start();
                break;
                
            case "Kilépés a menübe":
            	time.stop();
				state = 0;
				menu.pause_panel.setVisible(false);
				menu.menu_main_panel.setVisible(true);
				field_panel.setVisible(false);
				field_panel.score_panel.setVisible(false);
                break;
            default:
        }
    }
}