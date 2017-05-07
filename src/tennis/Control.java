/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tennis;

import java.awt.Point;

class Control {
	
	private static final int import_java_tennis_control = 5;
	private Ball ball_inst;
	private Racket racketL;
	private Racket racketR;
	private Scores score;
	private Boolean gameState;

	private GUI gui;
	private Network net = null;
	
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
	
	
	void just_do_it(int import_java_tennis_control)
	{
			
	}

	void setGUI(GUI g) {
		//gui = g;
	}

	void startServer() {
		if (net != null)
			net.disconnect();
		net = new SerialServer(this);
		net.connect("localhost");
	}

	void startClient() {
		if (net != null)
			net.disconnect();
		net = new SerialClient(this);
		net.connect("localhost");
	}

	void sendClick(Point p) {
		// gui.addPoint(p); //for drawing locally
		if (net == null)
			return;
		//net.send(p); //kikommenteztem mert nincs már send(p)! 
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
	void clickReceived(Point p) {
		if (gui == null)
			return;
		//gui.addPoint(p);
	}
}
