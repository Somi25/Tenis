/*
 * To change this template, choose Tools | Templates

 * 
 * and open the template in the editor.
 */

/* @todo:
 ment�s
 kerek labda ?
 netes gombnyom�s
 easter egg?
 ai?
 sz�g t�lfordul�s 
 * */
 

package tennis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidParameterException;

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
	private Boolean gameState;
	private Timer timer;
	
	private int whoStart = 0;
	private int pause = 0;

	
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
				}
				
				if(goal.isGoal(ball_inst) == +1){
					stopGame();
					score.incScoreL();
					serveL();
				}
				
				racketL.time();
				racketR.time();
				ball_inst.time();
				
				if(goal.isGoal(ball_inst) == -1){
					stopGame();
					score.incScoreR();
					serveR();
				}
				
				if(goal.isGoal(ball_inst) == +1){
					stopGame();
					score.incScoreL();
					serveL();
				}
			}
		});
		

	}
	
	public void resetGame(){
		score.reset();
		whoStart = 0;
		pause = 0;
		System.out.println("reset");
		racketL.setCoordinates(new Float[] {racketLx0, (float)yFieldMax/2});
		racketR.setCoordinates(new Float[] {racketRx0, (float)yFieldMax/2});
		ball_inst.setCoordinates(new Float[] {(float)xFieldMax/2, (float)yFieldMax/2});
	}
	
	// j�t�k ind�t�sa (minden labdamenetet ez ind�t)
	public void startGame(int whoPressed){
		if(whoStart != 0 && whoPressed != whoStart || pause == 1){
			return;
		}
		System.out.println("start" );
		
		if(!timer.isRunning()){
			try {
				// labda ir�ny�nak be�ll�t�sa
				if(whoStart == 0){
					// �ll�s: 0:0 v�letlen kezd�s
					if(Math.random() < 0.5){
						ball_inst.setDirection(0.0f);
						System.out.println("rand jobb");
					}else{
						ball_inst.setDirection((float)Math.PI);
						System.out.println("rand bal");
					}
					
				}else if(whoStart == +1){
					// bal oldali j�t�kos kezd
					ball_inst.setDirection(0.0f);
					System.out.println("szerva jobb");
					
				}else if(whoStart == -1){
					// jobb oldali j�t�kos kezd
					ball_inst.setDirection((float)Math.PI);
					System.out.println("szerva bal");
					
				}
				
			} catch (InvalidParameterException e) {
				// TODO Auto-generated catch block
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
	}
	
	public void continueGame(){
		pause = 0;
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
			//H�vni a Guib�l a j�t�kot 
		}
		else
		{
			//H�vni a Guib�l a hiba fv-t
		}
	}

	void startClient(String ip) {
		if (net != null)
			net.disconnect();
		net = new SerialClient(this);
		if(net.connect(ip))
		{
			//H�vni a Guib�l a j�t�kot 
		}
		else
		{
			//H�vni a Guib�l a hiba fv-t
		}
	}

	void networkError(Exception ex,String where) {
		//TOVABBADHATO A GUINAK MINDEN,
		//m�g nem t�r�ltem a ki�rat�sokat (error message)
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
	
	public void keyStroke(Key e)
	{
		Boolean pressed=e.getState();
		switch(e.getName())
		{
		case "UP_Left": 	if(pressed) {} 
							else		{}
		break;
		case "DOWN_Left": 	if(pressed) {}
							else		{}
		break;
		
		case "Pause": 		if(pressed) {} 
							else		{}
		break;

		case "UP_Right": 	if(pressed) {} 
							else		{}
		break;
		case "DOWN_Right": 	if(pressed) {}
							else		{}
		break;
		}
	}
}
