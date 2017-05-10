package tennis;

import java.security.InvalidParameterException;

public class Ball extends GeometricObject{

	private final Float radius;
	private final Float twist = 0.25f;							// csavarás mértéke
	public static final Float maxRand = (float)(5 / 180 * Math.PI);	// random szög mértéke: 5 fok
	
	// ütõk
	private Racket racketL;
	private Racket racketR;
	
	
	
	public Ball(Integer colour[], Float coordinates[], Float rad, Racket racketL, Racket racketR) throws InvalidParameterException{

		super(colour);

		this.racketL = racketL;
		this.racketR = racketR;
		this.coordinates = coordinates;

		if(rad<Math.min(yFieldMax,xFieldMax)/2 && rad>0)
		{
			this.radius=rad;
		}
		else
		{
			throw new InvalidParameterException();
		}
		setDirection((float)0);
		
		
	}
	
	public Float getRadius() {
		return radius;
	}


	@Override
	public void setCoordinates(Float[] coordinates) throws InvalidParameterException {
		
		//if(coordinates[0]>this.radius+xFieldMin && coordinates[0]<xFieldMax-this.radius)
		{
			this.coordinates[0] = coordinates[0];
		}
		//else
		{
			//throw new InvalidParameterException();
		}
		//if(coordinates[1]>this.radius+yFieldMin && coordinates[1]<yFieldMin-this.radius)
		{
			this.coordinates[1] = coordinates[1];
		}
		//else
		{
			//throw new InvalidParameterException();
		}
	}
	@Override
	public void setDirection(Float Dir) throws InvalidParameterException {
		
		if(Dir<0 || Dir>2*Math.PI){
			throw new InvalidParameterException();
		}else{
			this.direction=Dir;
		}
	}

	
	public void time(){
		float x = coordinates[0];
		float y = coordinates[1];

		x = (float) (x + velocity * Math.cos(direction));
		y = (float) (y + velocity * Math.sin(direction));

		coordinates[0] = x;
		coordinates[1] = y;

		// történt ütközés?
		coordinates = crashUp(coordinates);
		coordinates = crashDown(coordinates);
		coordinates = crashLeft(coordinates);
		coordinates = crashRight(coordinates);
      	//System.out.println(velocity);
		
		
		// maximális irány szög beállítása
		maxDir();
	}
	
	private void maxDir(){
		float dirX = (float)direction;
		
		
		// nagyobb mint 45 fok ÉS kisebb mint 90 -> legyen 45 fok
		if(direction > (float)(Math.PI / 4) && direction <= (float)(Math.PI / 2)){
			direction = (float)(Math.PI / 4) + maxRand*((float)Math.random()-0.5f);
			System.out.println("old: " + dirX / Math.PI * 180);
			System.out.println("new: " + direction / Math.PI * 180);
		}
		
		// kisebb mint -45 fok (315 fok) ÉS kisebb mint -90 (270) -> legyen -45 fok (315 fok)
		if(direction < (float)(Math.PI * 7/4) && direction >= (float)(Math.PI * 3/2)){
			direction = (float)(Math.PI * 7/4) + maxRand*((float)Math.random()-0.5f);
			System.out.println("old: " + dirX / Math.PI * 180);
			System.out.println("new: " + direction / Math.PI * 180);
		}
		
		// ha kisebb mint 135 fok ÉS nagyobb mint 90 fok -> legyen 135 fok
		if(direction < (float)(Math.PI * 3/4) && direction > (float)(Math.PI / 2)){
			direction = (float)(Math.PI * 3/4) + maxRand*((float)Math.random()-0.5f);
			System.out.println("old: " + dirX / Math.PI * 180);
			System.out.println("new: " + direction / Math.PI * 180);
		}
		
		// ha nagyobb mint 225 fok ÉS kisebb mint 270 fok -> legyen 225 fok
		if(direction > (float)(Math.PI * 5/4) && direction < (float)(Math.PI * 3/2)){
			direction = (float)(Math.PI * 5/4) + maxRand*((float)Math.random()-0.5f);
			System.out.println("old: " + dirX / Math.PI * 180);
			System.out.println("new: " + direction / Math.PI * 180);
		}
		
	}
	
	private Float[] crashUp(Float[] coord){
		// ütközés pálya tetején: labdaY + sugár > yMax
		float dif = (coord[1] + radius) - yFieldMax;
		
		if(dif >= 0){
			// ütközés történt
			
			// új y koordináta		
			coord[1] = coord[1] - 2*dif;
			
			// új irány (tükrözés x tengelyre)
			direction = (float) (-direction % (2*Math.PI));
	      	 //System.out.println("crashUp");
		}
		
		return coord;
	}
	
	private Float[] crashDown(Float[] coord){
		// ütközés pálya alján: labdaY - sugár < yMin
		float dif = yFieldMin - (coord[1] - radius);
		
		if(dif >= 0){
			// ütközés történt
			
			// új y koordináta		
			coord[1] = coord[1] + 2*dif;
			
			// új irány (tükrözés x tengelyre)
			direction = (float) (-direction % (2*Math.PI));
	     	 //System.out.println("crashDown");
		}
     	 
		return coord;
	}
	
	private Float[] crashRight(Float[] coord){
		// ütközés jobb oldali ütõvel
		Float[] racketCoord = racketR.getCoordinates();
		float dif = -((racketCoord[0] - racketR.getWidth() / 2) - (coord[0] + radius));
		
		// ütközés vizsgálata
		if(		(dif >= 0) &&
				(coord[1] <= (racketCoord[1] + racketR.getHeight() / 2)) && 
				(coord[1] >= (racketCoord[1] - racketR.getHeight() / 2))
		){
			// "normál" ütközés történt
			
			// új x koordináta
			coord[0] = coord[0] - 2*dif;
			
			// új irány (tükrözés y tengelyre + csavarás)
			direction = (float)((-direction + Math.PI) % (2*Math.PI)) - twist * racketR.getVelocity() + maxRand*((float)Math.random()-0.5f);
	    	//System.out.println("crashRight normal, velo: " + racketR.getVelocity());
		}else if(	(dif >= 0) &&
					(coord[1] <= (racketCoord[1] + racketR.getHeight() / 2 + radius / 2)) && 
					(coord[1] >= (racketCoord[1] - racketR.getHeight() / 2 - radius / 2))
		){
			// "sarok" ütközés történt
			
			Float cornerDir = 0f;
			
			// ferde pattanás -> melyik sarokról pattant
			if(coord[1] >= (racketCoord[1] + racketR.getHeight() / 2)){
				// felsõ sarok
				cornerDir = + (float)(Math.PI / 8);
			}else{
				// alsó sarok
				cornerDir = - (float)(Math.PI / 8);
			}
			
			// új x koordináta
			coord[0] = coord[0] - 2*dif;
			
			// új irány (tükrözés y tengelyre + csavarás)
			direction = (float) ((-direction + Math.PI) % (2*Math.PI)) - twist * racketR.getVelocity() - cornerDir + maxRand*((float)Math.random()-0.5f);
	    	//System.out.println("crashRight corner, velo: " + racketR.getVelocity());
		}
		
		return coord;
	}
	
	private Float[] crashLeft(Float[] coord){
		// ütközés bal oldali ütõvel
		Float[] racketCoord = racketL.getCoordinates();
		float dif = (racketCoord[0] + racketL.getWidth() / 2) - (coord[0] - radius);
		
		// ütközés vizsgálata
		if(		(dif >= 0) &&
				(coord[1] <= (racketCoord[1] + racketL.getHeight() / 2)) && 
				(coord[1] >= (racketCoord[1] - racketL.getHeight() / 2))
		){
			// "normál" ütközés történt
			
			// új x koordináta
			coord[0] = coord[0] + 2*dif;
			
			// új irány (tükrözés y tengelyre + csavarás)
			direction = (float) ((-direction + Math.PI) % (2*Math.PI)) + twist * racketL.getVelocity() + maxRand*((float)Math.random()-0.5f);
	    	//System.out.println("crashLeft normal, velo: " + racketL.getVelocity());
		}else if(	(dif >= 0) &&
					(coord[1] <= (racketCoord[1] + racketL.getHeight() / 2 + radius / 2)) && 
					(coord[1] >= (racketCoord[1] - racketL.getHeight() / 2 - radius / 2))
		){
			// "sarok" ütközés történt
			
			Float cornerDir = 0f;
			
			// ferde pattanás -> melyik sarokról pattant
			if(coord[1] >= (racketCoord[1] + racketL.getHeight() / 2)){
				// felsõ sarok
				cornerDir = + (float)(Math.PI / 8);
			}else{
				// alsó sarok
				cornerDir = - (float)(Math.PI / 8);
			}
			
			// új x koordináta
			coord[0] = coord[0] + 2*dif;
			
			// új irány (tükrözés y tengelyre + csavarás)
			direction = (float) ((-direction + Math.PI) % (2*Math.PI)) + twist * racketL.getVelocity() + cornerDir + maxRand*((float)Math.random()-0.5f);
	    	//System.out.println("crashLeft corner, velo: " + racketL.getVelocity());
		}
		
		
		return coord;
	}
}
