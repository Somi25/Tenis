package tennis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidParameterException;
import java.io.*;
import java.util.ArrayList; 

import javax.swing.Timer;

class Control {

	private GUI gui;
	private Network net = null;
	
	// alap�rtelmezett sz�n
	private final Integer[] defColour = {1,2,3};
	
	// param�terek: p�lya m�ret, �t� m�ret, labda m�ret
	protected final Integer xFieldMax	= 1280;
	protected final Integer xFieldMin	= 0;
	protected final Integer yFieldMax	= 620;
	protected final Integer yFieldMin	= 0;
	protected final Float racketW		= 10f;
	protected final Float racketH		= 100f;
	protected final Float racketLx0		= (float)xFieldMin + 15;
	protected final Float racketRx0		= (float)xFieldMax - 15;
	protected final Float ballRad		= 10f;
	private  final int sampleTime		= 4;
	private final Float ballDefVel		= 1.5f;
	
	private Ball ball_inst;
	private Racket racketL;
	private Racket racketR;
	private Scores score;
	private Goal goal;
	private Timer timer;
	
	private int whoStart = 0;
	private int pause = 0;
	private int continueBallDir = 0;

	private static final int OFFLINE = 1;
	private static final int ONLINE = 2;
	private static final int HOST = 3;
	private static final int CLIENT = 4;
	private static final int GAME = 5;
	

	private boolean pressed_1_up = false;
	private boolean pressed_1_down = false;
	private boolean pressed_2_up = false;
	private boolean pressed_2_down = false;

	
	// konstruktor
	public Control(){
		// objektumok l�trehoz�sa
		try {
			racketL = new Racket(defColour, new Float[] {racketLx0, (float)yFieldMax/2}, racketW, racketH);
			racketR = new Racket(defColour, new Float[] {racketRx0, (float)yFieldMax/2}, racketW, racketH);
			ball_inst = new Ball(defColour, new Float[] {(float)xFieldMax/2, (float)yFieldMax/2}, ballRad, racketL, racketR);
			goal = new Goal(xFieldMax, xFieldMin, ballRad);
			score = new Scores();
		} catch (InvalidParameterException e) {
         System.out.println(e.getMessage());
		}
		
		// objektumok mozgat�sa 250 Hz-el
		timer = new Timer(sampleTime, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(goal.isGoal(ball_inst) == -1){
					stopGame();
					score.incScoreR();
					serveR();
					
					// pont elk�ld�se g�ln�l
					if(gui.getState() == HOST){
						sendScores();
					}
				}
				
				if(goal.isGoal(ball_inst) == +1){
					stopGame();
					score.incScoreL();
					serveL();
					
					// pont elk�ld�se g�ln�l
					if(gui.getState() == HOST){
						sendScores();
					}
				}
				
				racketL.time();
				racketR.time();
				ball_inst.time();
				
				if(goal.isGoal(ball_inst) == -1){
					stopGame();
					score.incScoreR();
					serveR();
					
					// pont elk�ld�se g�ln�l
					if(gui.getState() == HOST){
						sendScores();
					}
				}
				
				if(goal.isGoal(ball_inst) == +1){
					stopGame();
					score.incScoreL();
					serveL();
					
					// pont elk�ld�se g�ln�l
					if(gui.getState() == HOST){
						sendScores();
					}
				}
			}
		});
		

	}
	
	public void resetGame(){
		if(timer.isRunning()){
			timer.stop();
		}
		score.reset();
		whoStart = 0;
		pause = 0;
		//System.out.println("reset Game");
		racketL.setCoordinates(new Float[] {racketLx0, (float)yFieldMax/2});
		racketR.setCoordinates(new Float[] {racketRx0, (float)yFieldMax/2});
		ball_inst.setCoordinates(new Float[] {(float)xFieldMax/2, (float)yFieldMax/2});
	}
	
	// j�t�k ind�t�sa (minden labdamenetet ez ind�t)
	public void startGame(int whoPressed){
		if(whoStart != 0 && whoPressed != whoStart || pause == 1){
			return;
		}
		//System.out.println("start" );
		
		if(!timer.isRunning()){
			try {
				// labda ir�ny�nak be�ll�t�sa
				if(continueBallDir == 1){
					// nem kell ir�nyt v�ltoztatni
				}else if(whoStart == 0){
					// �ll�s: 0:0 v�letlen kezd�s
					if(Math.random() < 0.5){
						ball_inst.setDirection(0.0f);
						//System.out.println("rand jobb");
					}else{
						ball_inst.setDirection((float)Math.PI);
						//System.out.println("rand bal");
					}
					
				}else if(whoStart == +1){
					// bal oldali j�t�kos kezd
					ball_inst.setDirection(0.0f);
					//System.out.println("szerva jobb");
					
				}else if(whoStart == -1){
					// jobb oldali j�t�kos kezd
					ball_inst.setDirection((float)Math.PI);
					//System.out.println("szerva bal");
				}
				
			} catch (InvalidParameterException e) {
				e.printStackTrace();
			}
			
			// labda sebess�g be�ll�t�sa
			ball_inst.setVelocity(ballDefVel);
			
			// id�z�t� ind�t�sa
			timer.start();
		}
	}
	
	public void pauseGame(){
		timer.stop();
		pause = 1;
		gui.showPauseMenu();
	}
	
	public void continueGame(){

		if(gui.getState() == CLIENT)
		{
			gui.hidePauseMenu();
			pause = 0;
			((SerialClient)net).send(new Key("Pause",false));
		}
		else
		{	
			pause = 0;
			continueBallDir = 1;
			whoStart = 0;
			if(gui.getState() == HOST)
				((SerialServer)net).sendPause(0);
		}
	}
	
	public void continueGamefromClient()
	{
		gui.hidePauseMenu();
		pause = 0;
		continueBallDir = 1;
		whoStart = 0;
	}
	
	public void stopGame(){
		timer.stop();
	}
	
	public void serveL(){
		whoStart = -1;
		racketL.setCoordinates(new Float[] {racketLx0, (float)yFieldMax/2});
		racketR.setCoordinates(new Float[] {racketRx0, (float)yFieldMax/2});
		ball_inst.setCoordinates(new Float[] {racketLx0 + racketW/2 + ballRad, (float)yFieldMax/2});
	}
	
	public void serveR(){
		whoStart = +1;
		racketL.setCoordinates(new Float[] {racketLx0, (float)yFieldMax/2});
		racketR.setCoordinates(new Float[] {racketRx0, (float)yFieldMax/2});
		ball_inst.setCoordinates(new Float[] {racketRx0 - racketW/2 - ballRad, (float)yFieldMax/2});
	}
	
	public Racket getRacketL(){
		return racketL;
	}

	public Racket getRacketR(){
		return racketR;
	}
	
	public Ball getBall(){
		return ball_inst;
	}
	
	
	public void setBall_inst(Float[] ball_crds) {
		this.ball_inst.setCoordinates(ball_crds);
	}
	
	public void setRacketL(Float[] racketL_crds) {
		
		this.racketL.setCoordinates(new Float[]{racketLx0, racketL_crds[1]});
		
	}
	
	public void setRacketR(Float[] racketR_crds) {
		
		this.racketR.setCoordinates(new Float[]{racketRx0, racketR_crds[1]});
		
	}
	
	public void setScore(Integer[] scores) {
		this.score.setScores(scores);
	}

	
	void setGUI(GUI g) {
		gui = g;
	}

	void startServer() {
		if (net != null)
			net.disconnect();
		net = new SerialServer(this);
		((SerialServer)net).connect();
	}

	void startClient(String ip) throws IOException {
		if (net != null)
			net.disconnect();
		net = new SerialClient(this);
		((SerialClient)net).connect(ip);
	}

	void networkError(Exception ex,String where) {
		gui.ResetGui();
	}
	
	Integer[] getScores(){
		return score.getScores();
	}
	
	void hostBall() {
		((SerialServer)net).sendBall(ball_inst);
	}
	void hostRacketLChanged()
	{
		((SerialServer)net).sendRacketL(racketL);
	}
	void hostRacketRChanged()
	{
		((SerialServer)net).sendRacketR(racketR);
	}
	void sendScores() {
		((SerialServer)net).sendScore(score);
	}
	
	public void delNet(){
		if(net != null){
			net = null;
		}
	}

	
	public void keyReceived(Key e){
		
		Boolean pressed = e.getState();
		if(pause == 0 )
		{
			System.out.println(e.getName()+" "+pressed.toString());
			switch(e.getName())
			{
			case "UP_Right":
				if(pressed)
				{
					if(pressed_2_up == false)
					{
		                pressed_2_up = true;
		                getRacketR().setVelocity(1f);
		        		startGame(+1);
		    		}
				}
				else
				{
		            pressed_2_up = false;
		            if(!pressed_2_down){
		            	getRacketR().setVelocity(0f);
		            }
				}
				break;
			case "DOWN_Right":
				if(pressed)
				{
					
					if(pressed_2_down == false)
					{
		        		pressed_2_down = true;
		                getRacketR().setVelocity(-1f);
		        		startGame(+1);
					}
				}
				else
				{
		    		pressed_2_down = false;
		            if(!pressed_2_up)
		            {
		            	getRacketR().setVelocity(0f);
		            }
				}
				break;
			case "Pause":
				if(pressed)
				{
					pauseGame();
					gui.showPauseMenu();
				}
				else
				{
					continueGame();
				}
				break;
			}
		}
		else
			if( e.getName().equals("Pause") && !pressed) continueGame();
	}
	
	public void keyStroke(Key e)
	{
		Boolean pressed=e.getState();
		switch(e.getName()){
		case "UP_Left": 	if(pressed){
					        	if(pressed_1_up == false)
					        	{
					        		if(gui.getState() == OFFLINE || gui.getState() == HOST)
					        		{
						                pressed_1_up = true;
						                getRacketL().setVelocity(1f);
						        		startGame(-1);
					        		}
					        	}
							}else{
				            	if(gui.getState() == OFFLINE || gui.getState() == HOST)
				        		{
					                pressed_1_up = false;
					                if(!pressed_1_down){
					                	getRacketL().setVelocity(0f);
					                }
				        		}
							}
		break;
		
		case "DOWN_Left": 	if(pressed){
					        	if(pressed_1_down == false)
					        	{
					        		if(gui.getState() == OFFLINE || gui.getState() == HOST)
					        		{
					            		pressed_1_down = true;
						                getRacketL().setVelocity(-1f);
						        		startGame(-1);
					        		}
					        	}
							}else{
				            	if(gui.getState() == OFFLINE || gui.getState() == HOST)
				        		{
				            		pressed_1_down = false;
					                if(!pressed_1_up){
					                	getRacketL().setVelocity(0f);
					                }
				        		}
							}
		break;
		
		case "Pause": 		if(pressed){
			        			if(gui.getState() == CLIENT)
			        				if(pause != 1)
			        					((SerialClient)net).send(new Key("Pause",true));
			        			if(gui.getState() == HOST)
			        				if(pause != 1)
			        					((SerialServer)net).sendPause(1);
								pauseGame();
							}
		break;

		case "UP_Right": 	if(pressed){
					        	if(pressed_2_up == false)
					        	{
					                pressed_2_up = true;
					        		if(gui.getState() == OFFLINE)
					        		{
						                pressed_2_up = true;
						                getRacketR().setVelocity(1f);
						        		startGame(+1);
					        		}
					        		else
					        			if(gui.getState() == CLIENT)
					        			{
					        				((SerialClient)net).send(new Key("UP_Right",true));
					        			}
					        	}
							}else{
				                pressed_2_up = false;
								if(gui.getState() == OFFLINE)
				        		{
					                if(!pressed_2_down){
					                	getRacketR().setVelocity(0f);
					                }
				        		}
				        		else
				        			if(gui.getState() == CLIENT)
				        			{
				        				((SerialClient)net).send(new Key("UP_Right",false));
				        			}
							}
		break;
		
		case "DOWN_Right": 	if(pressed){
								if(pressed_2_down == false)
					        	{
				            		pressed_2_down = true;
					        		if(gui.getState() == OFFLINE)
					        		{
						                getRacketR().setVelocity(-1f);
						        		startGame(+1);
					        		}
					        		else
					        			if(gui.getState() == CLIENT)
					        			{
					        				((SerialClient)net).send(new Key("DOWN_Right",true));
					        			}
					        	}
							}
							else
							{
			            		pressed_2_down = false;
								if(gui.getState() == OFFLINE)
				        		{
					                if(!pressed_2_up){
					                	getRacketR().setVelocity(0f);
					                }
				        		}
				        		else
				        			if(gui.getState() == CLIENT)
				        			{
				        				((SerialClient)net).send(new Key("DOWN_Right",false));
				        			}
							}
		break;
		
		}
	}

	public void saveFile() throws IOException{
		//System.out.println("save file");
	
		FileWriter writeObj;
		PrintWriter printObj;
	
		writeObj = new FileWriter("savefile.txt" , false);
		printObj = new PrintWriter(writeObj);
		printObj.println("Save Tennis game");		
		printObj.println(whoStart);		
		printObj.println(score.getScores()[0]);		
		printObj.println(score.getScores()[1]);
		printObj.close();
		
	}

	public void loadFile() throws IOException{
		//System.out.println("load file");

		//String myDirectory = System.getProperty("user.dir");
		String fullDirectory = "savefile.txt";
		String input_line = null;
		
		ArrayList<String> textItems = new ArrayList<String>(); //create array         list

		BufferedReader re = new BufferedReader(new FileReader(fullDirectory));
		while((input_line = re.readLine()) != null){
			textItems.add(input_line); //add item to array list
		}
		re.close();
			
		
		if(textItems.size() != 4 || !textItems.get(0).equals("Save Tennis game")){
			throw new IOException(new Throwable("rossz a f�jl tartalma"));
		}else{
			Integer[] scoreRead = new Integer[2];
			
			whoStart = Integer.parseInt(textItems.get(1).trim());
			scoreRead[0] = Integer.parseInt(textItems.get(2).trim());
			scoreRead[1] = Integer.parseInt(textItems.get(3).trim());
			score.setScores(scoreRead);
		}
		
		if(whoStart == -1){
			serveL();
		}
		
		if(whoStart == +1){
			serveR();
		}
	}
	
	void disconnect()
	{
		if(net != null){
			net.sendExit();
			net.disconnect();
		}
	}
	void exitGame() //recieved exit -  a kommentezett r�sszel kell egyel�nek lennie!
	{
		net.disconnect();
		/*case "Kil�p�s a men�be":
	    	time.stop();
			control.stopGame();
			control.disconnect();
			control.resetGame();
			state = 0;
			menu.save_button.setVisible(true);
			menu.pause_panel.setVisible(false);
			menu.menu_main_panel.setVisible(true);
			field_panel.setVisible(false);
			field_panel.score_panel.setVisible(false);
	        */
	}
	
	public void waitClient(){
		pause = 1;
	}
	
	public void clientConnected(){
		if(gui.clientConnected())	
			pause = 0;
	}
}

