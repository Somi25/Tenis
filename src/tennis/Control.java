/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tennis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidParameterException;

import javax.swing.Timer;

class Control {

	private GUI gui;
	private Network net = null;
	
	// alapértelmezett szín
	private final Integer[] defColour = {1,2,3};
	
	// paraméterek: pálya méret, ütõ méret, labda méret
	protected final Integer xFieldMax	= 1280;
	protected final Integer xFieldMin	= 0;
	protected final Integer yFieldMax	= 620;
	protected final Integer yFieldMin	= 0;
	protected final Float racketW		= 10f;
	protected final Float racketH		= 100f;
	protected final Float racketLx0		= (float)xFieldMin + 30;
	protected final Float racketRx0		= (float)xFieldMax - 30;
	protected final Float ballRad		= 10f;
	private  final int sampleTime		= 4;
	
	private Ball ball_inst;
	private Racket racketL;
	private Racket racketR;
	private Scores score;
	private Goal goal;
	private Boolean gameState;
	private Timer timer;

	
	// konstruktor
	public Control(){
		// objektumok létrehozása
		try {
			racketL = new Racket(defColour, new Float[] {racketLx0, (float)yFieldMax/2}, racketW, racketH);
			racketR = new Racket(defColour, new Float[] {racketRx0, (float)yFieldMax/2}, racketW, racketH);
			ball_inst = new Ball(defColour, new Float[] {(float)xFieldMax/2, (float)yFieldMax/2}, ballRad, racketL, racketR);
			goal = new Goal(xFieldMax, xFieldMin, ballRad);
			score = new Scores();
		} catch (InvalidParameterException e) {
       	 System.out.println("Paraméterhiba a konstruktornál");
         System.out.println(e.getMessage());
		}
		
		//Pálya kirajzolása 50Hz-el
		timer = new Timer(sampleTime, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				racketL.time();
				racketR.time();
				ball_inst.time();
				
				if(goal.isGoal(ball_inst) == -1){
					score.incScoreR();
					//startGame();
					timer.stop();
				}
				
				if(goal.isGoal(ball_inst) == +1){
					score.incScoreL();
					//startGame();	
					timer.stop();	
				}
			}
		});
		

	}
	
	// játék indítása (minden labdamenetet ez indít)
	public void startGame(){
		timer.start();
		ball_inst.setVelocity(-2.5f);
		try {
			//ball_inst.setDirection(3*(float)Math.PI/2+0.3f);
			ball_inst.setDirection(0.0f);
		} catch (InvalidParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	
	public void setBall_inst(Ball ball_inst) {
		this.ball_inst = ball_inst;
	}
	
	public void setGameState(Boolean gameState) {
		this.gameState = gameState;
	}
	
	public void setRacketL(Racket racketL) {
		this.racketL = racketL;
	}
	
	public void setRacketR(Racket racketR) {
		this.racketR = racketR;
	}
	
	public void setScore(Scores score) {
		this.score = score;
	}

	void setGUI(GUI g) {
		//gui = g;
	}

	void startServer() {
		if (net != null)
			net.disconnect();
		net = new SerialServer(this);
		if(net.connect("localhost"))
		{
			//Hívni a Guiból a játékot 
		}
		else
		{
			//Hívni a Guiból a hiba fv-t
		}
	}

	void startClient(String ip) {
		if (net != null)
			net.disconnect();
		net = new SerialClient(this);
		if(net.connect(ip))
		{
			//Hívni a Guiból a játékot 
		}
		else
		{
			//Hívni a Guiból a hiba fv-t
		}
	}

	void networkError(Exception ex,String where) {
		//TOVABBADHATO A GUINAK MINDEN,
		//még nem töröltem a kiíratásokat (error message)
	}
	
	Integer[] getScores(){
		return score.getScores();
	}
	
	void sendState() {
		((SerialServer)net).sendStates(ball_inst, racketL, racketR, gameState);
	}
	void sendScores() {
		((SerialServer)net).sendScore(score);
	}

	public void keyReceived(Key e){
		if(e.getName()=="W")
		{
			
		}
		if(e.getName()=="S")
		{
			
		}
		if(e.getName()=="UP")
		{
			
		}
		if(e.getName()=="DOWN")
		{
			
		}
		if(e.getName()=="P")
		{
			
		}
	}

}
