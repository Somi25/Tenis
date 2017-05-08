/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tennis;


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
	protected final Float racketLx0		= (float)xFieldMin + 30;
	protected final Float racketRx0		= (float)xFieldMax - 30;
	protected final Float ballRad		= 10f;
	
	private Ball ball_inst;
	private Racket racketL;
	private Racket racketR;
	private Scores score;
	private Boolean gameState;

	
	// konstruktor
	public Control(){
		// objektumok l�trehoz�sa
		try {
			racketL = new Racket(defColour, new Float[] {racketLx0, (float)yFieldMax/2}, racketW, racketH);
			racketR = new Racket(defColour, new Float[] {racketRx0, (float)yFieldMax/2}, racketW, racketH);
			ball_inst = new Ball(defColour, new Float[] {(float)xFieldMax/2, (float)yFieldMax/2}, ballRad, racketL, racketR);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
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
