package tennis;

public class Ball extends GeometricObject{

	private final Float radius;
	private final Float twist;		// csavar�s m�rt�ke
	
	// ezek nem ide kellenek, csak hogy leforduljon a k�d:
	private Racket racketL;
	private Racket racketR;
	
	public Ball(Integer colour[], Float coordinates[], Float rad, Float twi){
		
		super(colour,coordinates);
		radius = rad;
		twist = twi;
		
	}
	
	public Float getRadius() {
		return radius;
	}
	
	public void time(){
		float x = coordinates[0];
		float y = coordinates[1];

		x = (float) (x + velocity * Math.cos(direction));
		y = (float) (y + velocity * Math.sin(direction));

		coordinates[0] = x;
		coordinates[0] = y;

		// t�rt�nt �tk�z�s?
		coordinates = crashUp(coordinates);
		coordinates = crashDown(coordinates);
		coordinates = crashLeft(coordinates);
		coordinates = crashRight(coordinates);
		
	}
	
	private Float[] crashUp(Float[] coord){
		// �tk�z�s p�lya tetej�n: labdaY + sug�r > yMax
		float dif = (coord[1] + radius) - yFieldMax;
		
		if(dif >= 0){
			// �tk�z�s t�rt�nt
			
			// �j y koordin�ta		
			coord[1] = coord[1] - 2*dif;
			
			// �j ir�ny (t�kr�z�s x tengelyre)
			direction = (float) (-direction % (2*Math.PI));
		}
		
		return coord;
	}
	
	private Float[] crashDown(Float[] coord){
		// �tk�z�s p�lya alj�n: labdaY - sug�r < yMin
		float dif = yFieldMin - (coord[1] - radius);
		
		if(dif >= 0){
			// �tk�z�s t�rt�nt
			
			// �j y koordin�ta		
			coord[1] = coord[1] + 2*dif;
			
			// �j ir�ny (t�kr�z�s x tengelyre)
			direction = (float) (-direction % (2*Math.PI));
		}
		return coord;
	}
	
	private Float[] crashRight(Float[] coord){
		// �tk�z�s jobb oldali �t�vel
		return coord;
	}
	
	private Float[] crashLeft(Float[] coord){
		// �tk�z�s bal oldali �t�vel
		Float[] racketCoord = racketL.getCoordinates();		// ez m�g �gy nem j� -> "igazi �t�" koordin�t�ja kell
		float dif = (racketCoord[0] + racketL.getWidth() / 2) - (coord[0] - radius);
		
		if(		(dif >= 0) &&
				(coord[1] <= (racketCoord[1] + racketL.getHeight())) && 
				(coord[1] >= (racketCoord[1] - racketL.getHeight()))
		){
			// �tk�z�s t�rt�nt
			
			// �j x koordin�ta
			coord[0] = coord[0] + 2*dif;
			
			// �j ir�ny (t�kr�z�s y tengelyre + csavar�s)
			direction = (float) ((-direction + Math.PI) % (2*Math.PI)) + twist * racketL.getVelocity();
		}
		
		
		return coord;
	}
}
