package tennis;

public class Scores {
private int scoreL;
private int scoreR;
Scores(){scoreL=0;scoreR=0;}

public void increaseRight(){this.scoreR++;}
public void increaseLeft(){this.scoreL++;}
public void reset()
{
	this.scoreR=0;
	this.scoreL=0; 
}
public int[] getScores(){
	int[] scores=new int[2];
	scores[0]=scoreL;
	scores[1]=scoreR;
	return scores;
}

}
