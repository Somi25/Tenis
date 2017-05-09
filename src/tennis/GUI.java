package tennis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;
import javax.swing.Timer;


public class GUI extends JFrame implements ActionListener
{
	private static final int WIDTH_WINDOW = 1280;
	private static final int HEIGHT_WINDOW = 720;
	private static final int WIDTH_MENU = 435;
	private static final int HEIGHT_MENU = 400;
	
	private static final int TIMER_DELAY = 20;
	private Timer time;
	
	private int	state = 0;
	private static final int OFFLINE = 1;
	private static final int ONLINE = 2;
	private static final int HOST = 3;
	private static final int CLIENT = 4;
	private static final int GAME = 5;
	
	private boolean pressed_1_up = false;
	private boolean pressed_1_down = false;
	private boolean pressed_2_up = false;
	private boolean pressed_2_down = false;
	
	private int x = 11;
	private int y = 11;	
	
	private Control control;
	private Field field_panel;	
	private Menu menu;
	private String score;
	private String winner;
	
	private Pattern ip_pattern;
	
	public GUI()
	{		
		super("Tenisz");
		setSize(WIDTH_WINDOW+6, HEIGHT_WINDOW+30);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setResizable(false);		
			
		//Pálya kirajzolása 50Hz-el
		time = new Timer(TIMER_DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer[] coords = new Integer[2];				

				// Bence kezd
				// bal ütő koordináták megszerzése
				coords = control.getRacketL().getGUIcoords();
				x = coords[0];
				y = coords[1];			
				field_panel.racket_1(x,y);

				// jobb ütő koordináták megszerzése
				coords = control.getRacketR().getGUIcoords();
				x = coords[0];
				y = coords[1];			
				field_panel.racket_2(x,y);

				// labda ütő koordináták megszerzése
				coords = control.getBall().getGUIcoords();
				x = coords[0];
				y = coords[1];			
				field_panel.ball(x,y);
				
				
				field_panel.setScore(control.getScores()[0] + " : " + control.getScores()[1]);
				// Bence vége
				
				
				winner = "Bal oldali játékos";
				// Bence kezd
				//if(y >= 400)
				if(false)
				// Bence vége
				{
					Object[] button = {"Visszavágó", "Kilépés a menübe"};
					if(JOptionPane.showOptionDialog(field_panel, "A nyertes:  "+winner,"Vége a játéknak", JOptionPane.OK_OPTION,JOptionPane.PLAIN_MESSAGE, null, button, null) == 1)
					{
						time.stop();
						field_panel.setVisible(false);
						field_panel.score_panel.setVisible(false);
						menu.menu_main_panel.setVisible(true);
						y = 11;
						x=11;
					}
					else
					{
						y = 11;
					}
				}
		     }
		});	
		
		//Billentyűzet esemény kezelés
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
            		if(state == OFFLINE || state == HOST)
            		{
		                System.out.println("pressed 1_up");
		                pressed_1_up = true;
		                //moveRacket1_up();
		                // Bence kezd
		                control.getRacketL().setVelocity(1f);
		                // Bence vége
            		}
            	}
            }
        };
		
        AbstractAction released_1_up_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(state == OFFLINE || state == HOST)
        		{
	            	System.out.println("released 1_up");
	                pressed_1_up = false;
	                //stopRacket1_down();
	                // Bence kezd
	                control.getRacketL().setVelocity(0f);
	                // Bence vége
        		}
            }
        };
		
        AbstractAction pressed_1_down_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(pressed_1_down == false)
            	{
            		if(state == OFFLINE || state == HOST)
            		{
	            		System.out.println("pressed 1_down");
	            		pressed_1_down = true;
	            		//moveRacket1_down();
		                // Bence kezd
		                control.getRacketL().setVelocity(-1f);
		                // Bence vége
            		}
            	}
            }
        };
        
        AbstractAction released_1_down_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(state == OFFLINE || state == HOST)
        		{
            		System.out.println("released 1_down");
            		pressed_1_down = false;
            		//stopRacket1_down();
	                // Bence kezd
	                control.getRacketL().setVelocity(0f);
	                // Bence vége
        		}
            }
        };
        
        AbstractAction pressed_2_up_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(pressed_2_up == false)
            	{
            		if(state == OFFLINE || state == CLIENT)
            		{
		                System.out.println("pressed 2_up");
		                pressed_2_up = true;
		                //moveRacket2_up();
		                // Bence kezd
		                control.getRacketR().setVelocity(1f);
		                // Bence vége
            		}
            	}
            }
        };
		
        AbstractAction released_2_up_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(state == OFFLINE || state == CLIENT)
        		{
	                System.out.println("released 2_up");
	                pressed_2_up = false;
	                //stopRacket2_up();
	                // Bence kezd
	                control.getRacketR().setVelocity(0f);
	                // Bence vége
        		}
            }
        };
        
        AbstractAction pressed_2_down_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(pressed_2_down == false)
            	{
            		if(state == OFFLINE || state == CLIENT)
            		{
	            		System.out.println("pressed 2_down");
	            		pressed_2_down = true;
	            		//moveRacket2_down();
		                // Bence kezd
		                control.getRacketR().setVelocity(-1f);
		                // Bence vége
            		}
            	}
            }
        };
        
        AbstractAction released_2_down_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(state == OFFLINE || state == CLIENT)
        		{
            		System.out.println("released 2_down");
            		pressed_2_down = false;
            		//stopRacket2_down();
	                // Bence kezd
	                control.getRacketR().setVelocity(0f);
	                // Bence vége
        		}
            }
        };
        
        //ip cím formátum
        ip_pattern = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
		
		//Menü, pálya példányosítás
		try {
			menu = new Menu(WIDTH_MENU, HEIGHT_MENU, WIDTH_WINDOW);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		menu.add_action(this);
		field_panel = new Field(WIDTH_WINDOW);
		field_panel.add_action(pause_action, pressed_1_up_action, released_1_up_action, pressed_1_down_action, released_1_down_action, pressed_2_up_action, released_2_up_action, pressed_2_down_action, released_2_down_action);
		menu.add(field_panel);
		menu.add(field_panel.score_panel);
		field_panel.add(menu.pause_panel);
		setContentPane(menu);
		
		setVisible(true);
	}
	
	protected void start()
	{
		field_panel.setVisible(true);
		field_panel.score_panel.setVisible(true);
		time.start();
		// Bence kezd
		control.startGame();
		// Bence vége
	}
	
	//Menügomb esemény kezelés
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
            	//menu.host_wait_label.setVisible(true);
				//menu.client_error_label.setVisible(false);
            	menu.menu_online_panel.setVisible(false);
				menu.menu_offline_panel.setVisible(true);
				state = HOST;
                break;
            
            case "Kliens":
            	//menu.client_error_label.setVisible(true);
				menu.host_wait_label.setVisible(false);
				menu.menu_online_panel.setVisible(false);
				menu.menu_client_panel.setVisible(true);
				state = CLIENT;
                break;
            
            case "Új játék":
            	if(state == OFFLINE)
				{
            		control = new Control();
					menu.menu_offline_panel.setVisible(false);
					start();
				}
				if(state == HOST)
				{
					control = new Control();
					control.startServer();
					menu.menu_offline_panel.setVisible(false);
					start();
					menu.host_wait_label.setVisible(true);
				}
                break;
                
            case "Játék betöltése":
            	if(state == OFFLINE)
				{
            		control = new Control();
            		//control.load();
					menu.menu_offline_panel.setVisible(false);
					start();
				}
				if(state == HOST)
				{
					menu.host_wait_label.setVisible(true);
					//time.start();
				}
                break;

            case "Vissza - offline":
            	if(state == OFFLINE)
				{
            		menu.menu_offline_panel.setVisible(false);
    				menu.menu_main_panel.setVisible(true);
    				state = 0;
				}
				if(state == HOST)
				{
					menu.menu_offline_panel.setVisible(false);
					menu.menu_online_panel.setVisible(true);
					menu.host_wait_label.setVisible(false);
					menu.client_error_label.setVisible(false);
				}
				
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
				control = null;
                break;
                
            case "Csatlakozás":
			if(ip_pattern.matcher(menu.ip.getText()).matches())
            	{
					control = new Control();
					//control.startClient(menu.ip.getText());
            		//System.out.println(menu.ip.getText());
            		menu.client_error_label.setVisible(false);               	
            	}
			else
            	break;
            	
            case "Vissza - client":
            	menu.menu_client_panel.setVisible(false);
            	menu.menu_online_panel.setVisible(true);
            	menu.client_error_label.setVisible(false);
            	state = ONLINE;
            	break;
            default:
        }
    }
}