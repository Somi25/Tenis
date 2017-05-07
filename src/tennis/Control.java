/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tennis;

import com.sun.org.apache.xpath.internal.operations.Bool;

class Control {

	private GUI gui;
	private Network net = null;
	
	private Ball ball_inst;
	private Racket racketL;
	private Racket racketR;
	private Scores score;
	private Bool gameState;
	
	public void setBall_inst(Ball ball_inst) {
		this.ball_inst = ball_inst;
	}
	public void setGameState(Bool gameState) {
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
		net.connect("localhost","port");//1 v 2 parameteru, portot le lehet vagni!
	}

	void startClient() {
		if (net != null)
			net.disconnect();
		net = new SerialClient(this);
		net.connect("localhost","port");//1 v 2 parameteru, portot le lehet vagni!
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
