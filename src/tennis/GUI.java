package tennis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.Timer;


public class GUI extends JFrame implements ActionListener
{
	private static final int WIDTH_WINDOW = 1280;
	private static final int HEIGHT_WINDOW = 720;
	private static final int WIDTH_MENU = 435;
	private static final int HEIGHT_MENU = 400;
	
	private static final int TIMER_DELAY = 20;
	private Timer time;
	
	private Control control;
	private Field field_panel;	
	private Menu menu;
	private String score;
	private String winner;
	
	private Pattern ip_pattern;
	
	private int	state = 0;
	private static final int OFFLINE = 1;
	private static final int ONLINE = 2;
	private static final int HOST = 3;
	private static final int CLIENT = 4;
	private static final int GAME = 5;
	
	public GUI(Control c)
	{		
		super("Tenisz");
		control=c;
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
				field_panel.racket_1(coords[0],coords[1]);

				// jobb ütő koordináták megszerzése
				coords = control.getRacketR().getGUIcoords();
				field_panel.racket_2(coords[0],coords[1]);

				// labda ütő koordináták megszerzése
				coords = control.getBall().getGUIcoords();
				field_panel.ball(coords[0],coords[1]);
				
				
				field_panel.setScore(control.getScores()[0] + " : " + control.getScores()[1]);
				// Bence vége
				winner = null;
				if(control.getScores()[0] == 5)
					{
						winner = "Bal oldali játékos";
					}
				
				if(control.getScores()[1] == 5)
				{
					winner = "Jobb oldali játékos";
				}

				// Bence kezd
				//if(y >= 400)
				if(winner != null)
				// Bence vége
				{
					Object[] button = {"Visszavágó", "Kilépés a menübe"};
					if(JOptionPane.showOptionDialog(field_panel, "A nyertes:  "+winner,"Vége a játéknak", JOptionPane.OK_OPTION,JOptionPane.PLAIN_MESSAGE, null, button, null) == 1)
					{
						time.stop();
						// Bence
						control.stopGame();
						// vége
						field_panel.setVisible(false);
						field_panel.score_panel.setVisible(false);
						menu.menu_main_panel.setVisible(true);
						control.resetGame();
					}
					else
					{
						control.resetGame();
					}
				}
		     }
		});
		
		//Billentyűzet esemény kezelés
		AbstractAction pause_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
        		control.keyStroke(new Key("Pause",true));
                time.stop();
                menu.pause_panel.setVisible(true);
            }
        };
        
        AbstractAction pressed_1_up_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
        		control.keyStroke(new Key("UP_Left",true));
            }
        };
		
        AbstractAction released_1_up_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
        		control.keyStroke(new Key("UP_Left",false));
            }
        };
		
        AbstractAction pressed_1_down_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
        		control.keyStroke(new Key("DOWN_Left",true));
            }
        };
        
        AbstractAction released_1_down_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
        		control.keyStroke(new Key("DOWN_Left",false));
            }
        };
        
        AbstractAction pressed_2_up_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
        		control.keyStroke(new Key("UP_Right",true));
            }
        };
		
        AbstractAction released_2_up_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
        		control.keyStroke(new Key("UP_Right",false));
            }
        };
        
        AbstractAction pressed_2_down_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
        		control.keyStroke(new Key("DOWN_Right",true));
            }
        };
        
        AbstractAction released_2_down_action = new AbstractAction (){
            @Override
            public void actionPerformed(ActionEvent e) {
        		control.keyStroke(new Key("DOWN_Right",false));
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
	
	/*protected void start()
	{
		field_panel.setVisible(true);
		field_panel.score_panel.setVisible(true);
		time.start();
	}*/
	

	public int getState(){
		return state;
	}
	
	//Menügomb esemény kezelés
	@Override
    public void actionPerformed(ActionEvent e) {
		control.buttonStroke(e.getActionCommand());
		String command = e.getActionCommand();
		
	    switch (command) {

        //Display panel one when I select the option on the menu bar
        case "Offline":
        	state = OFFLINE;
        	showOfflineMenu();
            break;
        
        case "Online":
			state = ONLINE;
        	showOnlineMenu();
            break;
            
        case "Kilépés":
        	System.exit(0);
            break;
        
        case "Host":
			state = HOST;
        	showHostMenu();
            break;
        
        case "Kliens":
			state = CLIENT;
			showClientMenu();
            break;
        
        case "Új játék":
        	if(state == OFFLINE)
			{
        		showGame();
				//start();
			}
			if(state == HOST)
			{
				showGame();
				showWaitingForConnect();
				control.startServer();
				//start();
			}
            break;
            
        case "Játék betöltése":
        	if(state == OFFLINE)
			{
				showGame();
				//start();
			}
			if(state == HOST)
			{
				showGame();
				showWaitingForConnect();
			}
            break;

        case "Vissza - offline":
        	if(state == OFFLINE)
			{
        		showMainMenu();
				state = 0;
			}
			if(state == HOST)
			{
				showOnlineMenu();
			}
			
            break;
            
        case "Vissza - online":
        	showMainMenu();
			state = 0;
            break;
            
        case "Játék mentése":
        	showGame();
			//time.start();
			control.continueGame();
            break;
            
        case "Vissza - pause":
        	showGame();
			//time.start();
        	control.continueGame();
			System.out.println("vissza pause");
            break;
            
        case "Kilépés a menübe":
        	time.stop();
        	control.stopGame();
			state = 0;
			showMainMenu();
            break;
            
        case "Csatlakozás":
		/*if(ip_pattern.matcher(menu.ip.getText()).matches())
        	{
				try {
					//control = new Control();
					startClient(menu.ip.getText());
				 } catch (Exception exp) {
					 //guibol hivni errort
		         }       	
        	}*/
        	break;
        	
        case "Vissza - client":
        	showOnlineMenu();
        	state = ONLINE;
        	break;
        default:
    }
  
    }
	
	public void showPauseMenu(){
		menu.setMenuVisibilityToFalse();
		menu.setLabelVisibilityToFalse();
		
        menu.pause_panel.setVisible(true);
	}
	public void showOfflineMenu(){
		menu.setMenuVisibilityToFalse();
		menu.setLabelVisibilityToFalse();
		
    	menu.menu_offline_panel.setVisible(true);
	}
	public void showOnlineMenu(){
		menu.setMenuVisibilityToFalse();
		menu.setLabelVisibilityToFalse();
		
		menu.menu_online_panel.setVisible(true);
	}
	public void showHostMenu(){
		menu.setMenuVisibilityToFalse();
		menu.setLabelVisibilityToFalse();
		
		menu.menu_offline_panel.setVisible(true);
	}
	public void showClientMenu(){
		menu.setMenuVisibilityToFalse();
		menu.setLabelVisibilityToFalse();
		
		menu.menu_client_panel.setVisible(true);
	}
	public void showGame(){
		menu.setMenuVisibilityToFalse();
		menu.setLabelVisibilityToFalse();
		field_panel.setVisible(true);
		field_panel.score_panel.setVisible(true);
		time.start();
	}
	public void showWaitingForConnect(){
		menu.setMenuVisibilityToFalse();
		menu.setLabelVisibilityToFalse();
		
		menu.host_wait_label.setVisible(true);
	}
	public void showMainMenu(){
		menu.setMenuVisibilityToFalse();
		menu.setLabelVisibilityToFalse();

		menu.menu_main_panel.setVisible(true);
	}
	public void showError(Exception ex, String Description)
	{
		
	}
}