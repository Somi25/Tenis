package tennis;

public class Goal {

	// pálya méretek
	protected final Integer xFieldMax;
	protected final Integer xFieldMin;
	protected final Float ballRad;
	
	public Goal(Integer xFieldMax, Integer xFieldMin, Float ballRad){
		this.xFieldMax = xFieldMax;
		this.xFieldMin = xFieldMin;
		this.ballRad = ballRad;
	}
	
	public int isGoal(Ball ball){
		// gól a bal oldali kapuban
		if(ball.getCoordinates()[0] - ballRad <= xFieldMin){
			return -1;
		}

		// gól a jobb oldali kapuban
		if(ball.getCoordinates()[0] + ballRad >= xFieldMax){
			return +1;
		}
		
		// nincs gól
		return 0;
	}

}
