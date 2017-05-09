package tennis;

import java.security.InvalidParameterException;

public class Ball extends GeometricObject{

	private final Float radius;
	private final Float twist = 0.25f;		// csavarás mértéke
	
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
		
		if(coordinates[0]>this.radius+xFieldMin && coordinates[0]<xFieldMax-this.radius)
		{
			this.coordinates[0] = coordinates[0];
		}
		else
		{
			throw new InvalidParameterException();
		}
		if(coordinates[1]>this.radius+yFieldMin && coordinates[1]<yFieldMin-this.radius)
		{
			this.coordinates[1] = coordinates[1];
		}
		else
		{
			throw new InvalidParameterException();
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
	      	 System.out.println("crashUp");
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
	     	 System.out.println("crashDown");
		}
     	 
		return coord;
	}
	
	private Float[] crashRight(Float[] coord){
		// ütközés jobb oldali ütõvel
   	 //System.out.println("crashRiht");
		return coord;
	}
	
	private Float[] crashLeft(Float[] coord){
		// ütközés bal oldali ütõvel
		Float[] racketCoord = racketL.getCoordinates();		// ez még így nem jó -> "igazi ütõ" koordinátája kell
		float dif = (racketCoord[0] + racketL.getWidth() / 2) - (coord[0] - radius);
		
		if(		(dif >= 0) &&
				(coord[1] <= (racketCoord[1] + racketL.getHeight())) && 
				(coord[1] >= (racketCoord[1] - racketL.getHeight()))
		){
			// ütközés történt
			
			// új x koordináta
			coord[0] = coord[0] + 2*dif;
			
			// új irány (tükrözés y tengelyre + csavarás)
			direction = (float) ((-direction + Math.PI) % (2*Math.PI)) + twist * racketL.getVelocity();
	    	 System.out.println("crashLeft");
		}
		
		
		return coord;
	}
}
