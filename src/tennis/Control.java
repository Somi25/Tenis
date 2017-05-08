/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tennis;

import com.sun.org.apache.xpath.internal.operations.Bool;

class Control {

	private GUI gui;
	private Network net = null;
	
	Integer[] colour_ = new Integer[3];
	Float[] coordinates_ = new Float[2];
	Float rad_ = 3.2f;
	
	private Ball ball_inst;// = new Ball(colour_, coordinates_, rad_);
	private Racket racketL;
	private Racket racketR;
	private Scores score;
	private Boolean gameState;
	
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

	String getScores()
	{
		Integer[] scores;
		scores=score.getScores();
		return scores[0].toString() + " + " + scores[1].toString();
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
