package tennis;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.text.ParseException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

public class Menu extends JLabel
{	
	
	private JButton offline_button;
	private JButton online_button;
	private JButton exit_button;
	private JButton host_button;
	private JButton client_button;
	private JButton game_button;
	private JButton load_button;
	private JButton back_offline_button;
	private JButton back_online_button;
	private JButton ok_button;
	private JButton back_client_button;
	protected JLabel client_error_label;
	protected JLabel host_wait_label;
	private JLabel lost_label;
	protected JPanel menu_main_panel;
	protected JPanel menu_offline_panel;
	protected JPanel menu_online_panel;
	protected JPanel pause_panel;
	protected JPanel menu_client_panel;
	private JButton save_button;
	private JButton back_pause_button;
	private JButton menu_button;
	protected JFormattedTextField ip;
	
	public Menu(int WIDTH_MENU, int HEIGHT_MENU, int WIDTH_WINDOW) throws ParseException
	{		
		setLayout(null);
		try {
			setIcon(new ImageIcon(ImageIO.read( Thread.currentThread().getContextClassLoader().getResourceAsStream("Tenisz4.png") )));
		 } catch (IOException exp) {
             exp.printStackTrace();
         }
		
		//Fõmenü
		menu_main_panel = new JPanel();
		menu_main_panel.setLayout(null);
		menu_main_panel.setBounds(520-98, 310-40, WIDTH_MENU,HEIGHT_MENU);
		menu_main_panel.setBackground(Color.black);
		menu_main_panel.setVisible(true);
		add(menu_main_panel);
		
		
		//Almenü
		menu_offline_panel = new JPanel();
		menu_offline_panel.setLayout(null);
		menu_offline_panel.setBounds(520-98, 310-40, WIDTH_MENU, HEIGHT_MENU);
		menu_offline_panel.setBackground(Color.black);
		menu_offline_panel.setVisible(false);
		add(menu_offline_panel);
		
		menu_online_panel = new JPanel();
		menu_online_panel.setLayout(null);
		menu_online_panel.setBounds(520-98, 310-40, WIDTH_MENU, HEIGHT_MENU);
		menu_online_panel.setBackground(Color.black);
		menu_online_panel.setVisible(false);
		add(menu_online_panel);
		
		menu_client_panel = new JPanel();
		menu_client_panel.setLayout(null);
		menu_client_panel.setBounds(520-98, 310-40, WIDTH_MENU, HEIGHT_MENU);
		menu_client_panel.setBackground(Color.black);
		menu_client_panel.setVisible(false);
		add(menu_client_panel);
		
		//Szöveg
		client_error_label = new JLabel("Nem sikerült csatlakozni a szerverhez!");
		client_error_label.setForeground(Color.WHITE);
		client_error_label.setFont(new Font("szöveg", Font.BOLD, 24));
		client_error_label.setBounds(0, 340, 436, 40);
		menu_client_panel.add(client_error_label);
		client_error_label.setVisible(false);
		
		host_wait_label = new JLabel("Várakozás a kliensre!");
		host_wait_label.setForeground(Color.WHITE);
		host_wait_label.setFont(new Font("szöveg", Font.BOLD, 24));
		host_wait_label.setBounds(98, 340, 242, 40);
		menu_offline_panel.add(host_wait_label);
		host_wait_label.setVisible(false);
		
		lost_label = new JLabel("A kapcsolat megszakadt!");
		lost_label.setForeground(Color.WHITE);
		lost_label.setFont(new Font("szöveg", Font.BOLD, 24));
		lost_label.setBounds(77, 340, 284, 40);
		menu_main_panel.add(lost_label);
		lost_label.setVisible(false);
		
		//Nyomógombok
		offline_button = new JButton("Offline");
		offline_button.setBounds(98, 0, 240, 80);
		offline_button.setBackground(Color.WHITE);
		offline_button.setForeground(Color.BLACK);
		offline_button.setFont(new Font("Gomb", Font.BOLD, 24));
		offline_button.setActionCommand("Offline");
		menu_main_panel.add(offline_button);
		
		online_button = new JButton("Online");
		online_button.setBounds(98, 120, 240, 80);
		online_button.setBackground(Color.WHITE);
		online_button.setForeground(Color.BLACK);
		online_button.setFont(new Font("Gomb", Font.BOLD, 24));
		online_button.setActionCommand("Online");
		menu_main_panel.add(online_button);
		
		exit_button = new JButton("Kilépés");
		exit_button.setBounds(98, 240, 240, 80);
		exit_button.setBackground(Color.WHITE);
		exit_button.setForeground(Color.BLACK);
		exit_button.setFont(new Font("Gomb", Font.BOLD, 24));
		exit_button.setActionCommand("Kilépés");
		menu_main_panel.add(exit_button);
		
		host_button = new JButton("Host");
		host_button.setBounds(98, 0, 240, 80);
		host_button.setBackground(Color.WHITE);
		host_button.setForeground(Color.BLACK);	
		host_button.setFont(new Font("Gomb", Font.BOLD, 24));
		host_button.setActionCommand("Host");
		menu_online_panel.add(host_button);
		
		client_button = new JButton("Kliens");
		client_button.setBounds(98, 120, 240, 80);
		client_button.setBackground(Color.WHITE);
		client_button.setForeground(Color.BLACK);
		client_button.setFont(new Font("Gomb", Font.BOLD, 24));
		client_button.setActionCommand("Kliens");
		menu_online_panel.add(client_button);
		
		game_button = new JButton("Új játék");
		game_button.setBounds(98, 0, 240, 80);
		game_button.setBackground(Color.WHITE);
		game_button.setForeground(Color.BLACK);
		game_button.setFont(new Font("Gomb", Font.BOLD, 24));
		game_button.setActionCommand("Új játék");
		menu_offline_panel.add(game_button);
		
		load_button = new JButton("Játék betöltése");
		load_button.setBounds(98, 120, 240, 80);
		load_button.setBackground(Color.WHITE);
		load_button.setForeground(Color.BLACK);	
		load_button.setFont(new Font("Gomb", Font.BOLD, 24));
		load_button.setActionCommand("Játék betöltése");
		menu_offline_panel.add(load_button);
		
		back_offline_button = new JButton("Vissza");
		back_offline_button.setBounds(98, 240, 240, 80);
		back_offline_button.setBackground(Color.WHITE);
		back_offline_button.setForeground(Color.BLACK);
		back_offline_button.setFont(new Font("Gomb", Font.BOLD, 24));
		back_offline_button.setActionCommand("Vissza - offline");
		menu_offline_panel.add(back_offline_button);
		
		back_online_button = new JButton("Vissza");
		back_online_button.setBounds(98, 240, 240, 80);
		back_online_button.setBackground(Color.WHITE);
		back_online_button.setForeground(Color.BLACK);
		back_online_button.setFont(new Font("Gomb", Font.BOLD, 24));
		back_online_button.setActionCommand("Vissza - online");
		menu_online_panel.add(back_online_button);
		
		pause_panel = new JPanel();
		pause_panel.setLayout(null);
		pause_panel.setBounds(520, 310-160, WIDTH_MENU,HEIGHT_MENU);
		pause_panel.setBackground(Color.black);
		pause_panel.setVisible(false);
		
		save_button = new JButton("Játék mentése");
		save_button.setBounds(0, 0, 240, 80);
		save_button.setBackground(Color.WHITE);
		save_button.setForeground(Color.BLACK);
		save_button.setFont(new Font("Gomb", Font.BOLD, 24));
		save_button.setActionCommand("Játék mentése");
		pause_panel.add(save_button);
		
		back_pause_button = new JButton("Vissza");
		back_pause_button.setBounds(0, 120, 240, 80);
		back_pause_button.setBackground(Color.WHITE);
		back_pause_button.setForeground(Color.BLACK);
		back_pause_button.setFont(new Font("Gomb", Font.BOLD, 24));
		back_pause_button.setActionCommand("Vissza - pause");
		pause_panel.add(back_pause_button);		
		
		menu_button = new JButton("Kilépés a menübe");
		menu_button.setBounds(0, 240, 240, 80);
		menu_button.setBackground(Color.WHITE);
		menu_button.setForeground(Color.BLACK);
		menu_button.setFont(new Font("Gomb", Font.BOLD, 24));
		menu_button.setActionCommand("Kilépés a menübe");
		pause_panel.add(menu_button);
		
		//MaskFormatter ip_format = new MaskFormatter("###.###.###.###");
		ip = new JFormattedTextField(/*ip_format*/);
		ip.setBounds(98, 0, 240, 80);
		ip.setHorizontalAlignment(JTextField.CENTER);
		ip.setFont(new Font("Gomb", Font.BOLD, 24));
		menu_client_panel.add(ip);
		
		ok_button = new JButton("Csatlakozás");
		ok_button.setBounds(98, 120, 240, 80);
		ok_button.setBackground(Color.WHITE);
		ok_button.setForeground(Color.BLACK);	
		ok_button.setFont(new Font("Gomb", Font.BOLD, 24));
		ok_button.setActionCommand("Csatlakozás");
		menu_client_panel.add(ok_button);
		
		back_client_button = new JButton("Vissza");
		back_client_button.setBounds(98, 240, 240, 80);
		back_client_button.setBackground(Color.WHITE);
		back_client_button.setForeground(Color.BLACK);
		back_client_button.setFont(new Font("Gomb", Font.BOLD, 24));
		back_client_button.setActionCommand("Vissza - client");
		menu_client_panel.add(back_client_button);
		
		setVisible(true);
	}
	public void setMenuVisibilityToFalse()
	{
		menu_main_panel.setVisible(false);
		menu_offline_panel.setVisible(false);
		menu_online_panel.setVisible(false);
		menu_client_panel.setVisible(false);
	}
	public void setLabelVisibilityToFalse()
	{
		client_error_label.setVisible(false);
		host_wait_label.setVisible(false);
		lost_label.setVisible(false);
	}
	//gomb esemény aktiválás
	protected void add_action(GUI action)
	{
		offline_button.addActionListener(action);
		online_button.addActionListener(action);
		exit_button.addActionListener(action);
		host_button.addActionListener(action);
		client_button.addActionListener(action);
		game_button.addActionListener(action);
		load_button.addActionListener(action);
		back_offline_button.addActionListener(action);
		back_online_button.addActionListener(action);
		save_button.addActionListener(action);
		back_pause_button.addActionListener(action);
		menu_button.addActionListener(action);
		ok_button.addActionListener(action);
		back_client_button.addActionListener(action);
	}	
}