package tennis;

public class Scores {
private int scoreL;
private int scoreR;
Scores(){scoreL=0;scoreR=0;}
public int getScoreL() {
	return scoreL;
}
public int getScoreR() {
	return scoreR;
}
public void setScoreL(int scoreL) {
	this.scoreL = scoreL;
}
public void setScoreR(int scoreR) {
	this.scoreR = scoreR;
}
public int[] getScores(){
	int[] scores=new int[2];
	scores[0]=getScoreL();
	scores[1]=getScoreR();
	return scores;
}

}
