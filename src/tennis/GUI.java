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
import javax.swing.Timer;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.swing.ImageIcon;


public class GUI extends JFrame
{
	private static final int WIDTH_WINDOW = 1280;
	private static final int HEIGHT_WINDOW = 720;
	private static final int WIDTH_MENU = 240;
	private static final int HEIGHT_MENU = 320;
	private static final int OFFLINE = 1;
	private static final int HOST = 2;
	private static final int GAME = 3;
	private static final int TIMER_DELAY = 20;
	private int	state = 0;
	private int i = 11;
	private int j = 11;
	private boolean pressed_1_up = false;
	private boolean pressed_1_down = false;
	private boolean pressed_2_up = false;
	private boolean pressed_2_down = false;
	
	private Timer time;
	private JPanel pause_panel;
	private JLabel main_panel;
	private JLabel light_1_label;
	private JLabel light_2_label;
	
	private String score = "0 : 0";
	private DrawPanel field_panel;
	private JLabel score_label;
	
	public GUI()
	{		
		super("Tenisz");
		setSize(WIDTH_WINDOW+6, HEIGHT_WINDOW+30);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setResizable(false);
		try {
			main_panel = new JLabel(new ImageIcon(ImageIO.read( Thread.currentThread().getContextClassLoader().getResourceAsStream("Tenisz4.png") )));
		 } catch (IOException exp) {
             exp.printStackTrace();
         }
		setContentPane(main_panel);
		
		
		//Alap panel
		main_panel.setLayout(null);
		
		//Eredményjelzõ
		JPanel score_panel = new JPanel();
		score_panel.setLayout(null);
		score_panel.setBounds(0, 0, WIDTH_WINDOW,80);
		score_panel.setBackground(Color.black);
		score_panel.setVisible(false);
		main_panel.add(score_panel);
		
		//Fõmenü
		JPanel menu_main_panel = new JPanel();
		menu_main_panel.setLayout(null);
		menu_main_panel.setBounds(520, 310-40, WIDTH_MENU,HEIGHT_MENU);
		menu_main_panel.setBackground(Color.black);
		main_panel.add(menu_main_panel);
		
		//Almenü
		JPanel menu_offline_panel = new JPanel();
		menu_offline_panel.setLayout(null);
		menu_offline_panel.setBounds(520, 310-40, WIDTH_MENU, HEIGHT_MENU);
		menu_offline_panel.setBackground(Color.black);
		menu_offline_panel.setVisible(false);
		main_panel.add(menu_offline_panel);
		
		JPanel menu_online_panel = new JPanel();
		menu_online_panel.setLayout(null);
		menu_online_panel.setBounds(520-98, 310-40, 435, HEIGHT_MENU+54);
		menu_online_panel.setBackground(Color.black);
		menu_online_panel.setVisible(false);
		main_panel.add(menu_online_panel);
				
		//Pálya
		field_panel = new DrawPanel();
		field_panel.setLayout(null);
		field_panel.setBounds(0, 80, WIDTH_WINDOW,640);
		field_panel.setBackground(Color.black);
		field_panel.setFocusable(true);
		
		field_panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pause");
		field_panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "pressed 1_up");
		field_panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "released 1_up");
		field_panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "pressed 1_down");
		field_panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "released 1_down");
		field_panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "pressed 2_up");
		field_panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "released 2_up");
		field_panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "pressed 2_down");
		field_panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "released 2_down");
		field_panel.getActionMap().put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("pause");
                time.stop();
                pause_panel.setVisible(true);
            }
        });
		
		field_panel.getActionMap().put("pressed 1_up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(pressed_1_up == false)
            	{
	                System.out.println("pressed 1_up");
	                pressed_1_up = true;
	                i = i - 30;
            	}
            }
        });
		
		field_panel.getActionMap().put("released 1_up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("released 1_up");
                pressed_1_up = false;
            }
        });
		
		field_panel.getActionMap().put("pressed 1_down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(pressed_1_down == false)
            	{
            		System.out.println("pressed 1_down");
            		pressed_1_down = true;
            		i = i + 30;
            	}
            }
        });
        
        field_panel.getActionMap().put("released 1_down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            		System.out.println("released 1_down");
            		pressed_1_down = false;
            }
        });
        
        field_panel.getActionMap().put("pressed 2_up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(pressed_2_up == false)
            	{
	                System.out.println("pressed 2_up");
	                pressed_2_up = true;
	                j = j - 30;
            	}
            }
        });
		
		field_panel.getActionMap().put("released 2_up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("released 2_up");
                pressed_2_up = false;
            }
        });
        
        field_panel.getActionMap().put("pressed 2_down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(pressed_2_down == false)
            	{
            		System.out.println("pressed 2_down");
            		pressed_2_down = true;
            		j = j + 30;
            		score = "0 : 1";
            		score_label.setText(score);
            		light_1_label.setVisible(true);
            	}
            }
        });
        
        field_panel.getActionMap().put("released 2_down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            		System.out.println("released 2_down");
            		pressed_2_down = false;
            		light_1_label.setVisible(false);
            }
        });
        
		field_panel.setVisible(false);
		main_panel.add(field_panel);
		
		pause_panel = new JPanel();
		pause_panel.setLayout(null);
		pause_panel.setBounds(520, 310-160, WIDTH_MENU,HEIGHT_MENU);
		pause_panel.setBackground(Color.black);
		pause_panel.setVisible(false);
		field_panel.add(pause_panel);
		
		//Szövegek
		JLabel client_error_label = new JLabel("Nem sikerült csatlakozni a szerverhez!");
		client_error_label.setForeground(Color.WHITE);
		client_error_label.setFont(new Font("szöveg", Font.BOLD, 24));
		client_error_label.setBounds(0, 345, 436, 40);
		menu_online_panel.add(client_error_label);
		client_error_label.setVisible(false);
		
		JLabel host_wait_label = new JLabel("Várakozás a kliensre!");
		host_wait_label.setForeground(Color.WHITE);
		host_wait_label.setFont(new Font("szöveg", Font.BOLD, 24));
		host_wait_label.setBounds(98, 345, 242, 40);
		menu_online_panel.add(host_wait_label);
		host_wait_label.setVisible(false);
		
		score_label = new JLabel("0 : 0");
		score_label.setBounds(WIDTH_WINDOW/2-81, 0, /*156*/162, 80);
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
		
		//Nyomógombok
		JButton offline_button = new JButton("Offline");
		offline_button.setBounds(0, 0, 240, 80);
		offline_button.setBackground(Color.WHITE);
		offline_button.setForeground(Color.BLACK);
		offline_button.setFont(new Font("Gomb", Font.BOLD, 24));
		offline_button.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					menu_main_panel.setVisible(false);
					menu_offline_panel.setVisible(true);
					state = OFFLINE;
					
				}
			});
		menu_main_panel.add(offline_button);
		
		JButton online_button = new JButton("Online");
		online_button.setBounds(0, 120, 240, 80);
		online_button.setBackground(Color.WHITE);
		online_button.setForeground(Color.BLACK);
		online_button.setFont(new Font("Gomb", Font.BOLD, 24));
		online_button.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					menu_main_panel.setVisible(false);
					menu_online_panel.setVisible(true);
					
				}
			});
		menu_main_panel.add(online_button);
		
		JButton exit_button = new JButton("Kilépés");
		exit_button.setBounds(0, 240, 240, 80);
		exit_button.setBackground(Color.WHITE);
		exit_button.setForeground(Color.BLACK);
		exit_button.setFont(new Font("Gomb", Font.BOLD, 24));
		exit_button.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					System.exit(0);
				}
			});
		menu_main_panel.add(exit_button);
		
		JButton host_button = new JButton("Host");
		host_button.setBounds(98, 0, 240, 80);
		host_button.setBackground(Color.WHITE);
		host_button.setForeground(Color.BLACK);	
		host_button.setFont(new Font("Gomb", Font.BOLD, 24));
		host_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				host_wait_label.setVisible(true);
				client_error_label.setVisible(false);
				/*menu_online_panel.setVisible(false);
				menu_offline_panel.setVisible(true);*/
				state = HOST;
				
			}
		});
		menu_online_panel.add(host_button);
		
		JButton client_button = new JButton("Kliens");
		client_button.setBounds(98, 120, 240, 80);
		client_button.setBackground(Color.WHITE);
		client_button.setForeground(Color.BLACK);
		client_button.setFont(new Font("Gomb", Font.BOLD, 24));
		client_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				client_error_label.setVisible(true);
				host_wait_label.setVisible(false);
			}
		});
		menu_online_panel.add(client_button);
		
		JButton game_button = new JButton("Új játék");
		game_button.setBounds(0, 0, 240, 80);
		game_button.setBackground(Color.WHITE);
		game_button.setForeground(Color.BLACK);
		game_button.setFont(new Font("Gomb", Font.BOLD, 24));
		game_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{				
				if(state == OFFLINE)
				{
					menu_offline_panel.setVisible(false);
					field_panel.setVisible(true);
					score_panel.setVisible(true);
					time.start();
				}
				if(state == HOST)
				{
					host_wait_label.setVisible(true);
					//time.start();
				}
			}
		});
		menu_offline_panel.add(game_button);
		
		
		
		time = new Timer(TIMER_DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReFresh(i, j);
				if(i >= 400)
				{
					Object[] button = {"OK"};
					if(JOptionPane.showOptionDialog(field_panel, "Te nyertél","Vége a játéknak", JOptionPane.OK_OPTION,JOptionPane.PLAIN_MESSAGE, null, button, null) == 0)
					{
						time.stop();
						field_panel.setVisible(false);
						score_panel.setVisible(false);
						menu_main_panel.setVisible(true);
						i = 11;
						j=11;
					}
				}
		     }
		});
		
		JButton load_button = new JButton("Játék betöltése");
		load_button.setBounds(0, 120, 240, 80);
		load_button.setBackground(Color.WHITE);
		load_button.setForeground(Color.BLACK);	
		load_button.setFont(new Font("Gomb", Font.BOLD, 24));
		load_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				if(state == OFFLINE)
				{
					menu_offline_panel.setVisible(false);
					field_panel.setVisible(true);
					score_panel.setVisible(true);
					time.start();
				}
				if(state == HOST)
				{
					host_wait_label.setVisible(true);
					//time.start();
				}
			}
		});
		menu_offline_panel.add(load_button);
		
		JButton save_button = new JButton("Játék mentése");
		save_button.setBounds(0, 0, 240, 80);
		save_button.setBackground(Color.WHITE);
		save_button.setForeground(Color.BLACK);
		save_button.setFont(new Font("Gomb", Font.BOLD, 24));
		save_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				pause_panel.setVisible(false);
				time.start();
			}
		});
		pause_panel.add(save_button);
		
		JButton back_pause_button = new JButton("Vissza");
		back_pause_button.setBounds(0, 120, 240, 80);
		back_pause_button.setBackground(Color.WHITE);
		back_pause_button.setForeground(Color.BLACK);
		back_pause_button.setFont(new Font("Gomb", Font.BOLD, 24));
		back_pause_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				pause_panel.setVisible(false);
				time.start();
			}
		});
		pause_panel.add(back_pause_button);
		
		JButton back_offline_button = new JButton("Vissza");
		back_offline_button.setBounds(0, 240, 240, 80);
		back_offline_button.setBackground(Color.WHITE);
		back_offline_button.setForeground(Color.BLACK);
		back_offline_button.setFont(new Font("Gomb", Font.BOLD, 24));
		back_offline_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				if(state == OFFLINE)
				{
					menu_offline_panel.setVisible(false);
					menu_main_panel.setVisible(true);
				}
				else
					menu_offline_panel.setVisible(false);
					menu_online_panel.setVisible(true);
			}
		});
		menu_offline_panel.add(back_offline_button);
		
		JButton back_online_button = new JButton("Vissza");
		back_online_button.setBounds(98, 240, 240, 80);
		back_online_button.setBackground(Color.WHITE);
		back_online_button.setForeground(Color.BLACK);
		back_online_button.setFont(new Font("Gomb", Font.BOLD, 24));
		back_online_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				menu_online_panel.setVisible(false);
				menu_main_panel.setVisible(true);
				host_wait_label.setVisible(false);
				client_error_label.setVisible(false);
			}
		});
		menu_online_panel.add(back_online_button);
		
		JButton menu_button = new JButton("Kilépés a menübe");
		menu_button.setBounds(0, 240, 240, 80);
		menu_button.setBackground(Color.WHITE);
		menu_button.setForeground(Color.BLACK);
		menu_button.setFont(new Font("Gomb", Font.BOLD, 24));
		menu_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				time.stop();
				state = 0;
				pause_panel.setVisible(false);
				field_panel.setVisible(false);
				score_panel.setVisible(false);
				menu_main_panel.setVisible(true);
			}
		});
		pause_panel.add(menu_button);
		
		setVisible(true);
	}
	
	private class DrawPanel extends JPanel
	{
		private List<Point> ball;
		private List<Point> racket_1;
		private List<Point> racket_2;

	        public DrawPanel() {
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
            	g.fillOval(ball.x, ball.y, 20, 20);
            }
            
            for (Point racket_1 : racket_1)
            {
            	g.fillRect(racket_1.x, racket_1.y, 10, 100);
            }
            
            for (Point racket_2 : racket_2)
            {
            	g.fillRect(racket_2.x, racket_2.y, 10, 100);
            }
        }

        public void ball(int x, int y) {
        	field_panel.ball.clear();
        	ball.add(new Point(x, y));        	
        	repaint();
        }
        
        public void racket_1(int x, int y) {
        	field_panel.racket_1.clear();
        	racket_1.add(new Point(x, y));
        	repaint();
        }
        
        public void racket_2(int x, int y) {
        	field_panel.racket_2.clear();
        	racket_2.add(new Point(x, y));
            repaint();
        }
	}
	
	private void ReFresh(int i, int j)
	{
		//System.out.println("timer");
		field_panel.ball(i+30,i+30);
		field_panel.racket_1(30,i);
		field_panel.racket_2(WIDTH_WINDOW-10-30,j);
	}
}
