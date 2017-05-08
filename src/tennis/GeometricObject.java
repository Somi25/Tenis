package tennis;

public abstract class GeometricObject {
	
	// p�lya m�retek
	protected final Integer xFieldMax	= 1280;
	protected final Integer xFieldMin	= 0;
	protected final Integer yFieldMax	= 620;
	protected final Integer yFieldMin	= 0;
	protected final Integer racketW		= 10;
	protected final Integer racketH		= 100;
	protected final Integer ballRad		= 10;
	
	
	protected Integer[] colourRGB = new Integer[3];
	protected Float[] coordinates = new Float[2];
	protected Float direction;		// [0, 2*pi) intervallum, 0 ir�ny: D-kr x tengelye (�ramutat� j�r�s�val ellent�tes ir�ny�)
	protected Float velocity;		// "pixel / time_tick" m�rt�kegys�g
	
	public GeometricObject(Integer Colour[]) throws Exception {
		if(Colour.length == 3 && 0<=Colour[0] && Colour[0]<256 && 0<=Colour[1] && Colour[1]<256 && 0<=Colour[2] && Colour[2]<256)
			colourRGB = Colour;
		else 
		 {
			Exception InvalidParameterException = null;
			throw InvalidParameterException;
		 }
		velocity = (float)0;
	}
	
	public Integer[] getColour() {
		return colourRGB;
	}
	public Float[] getCoordinates() {
		return coordinates;
	}
	public Float getDirection() {
		return direction;
	}
	public Float getVelocity() {
		return velocity;
	}
	
	
	// p�lya koordin�t�it konvert�lja GUI koordin�t�kra
	// p�ly�n y tengely lentr�l fel n�, GUI-n ford�tva
	public Integer[] getGUIcoords (){
		Integer[] coordsInt = new Integer[2];

		coordsInt[0] = Math.round(coordinates[0]);
		coordsInt[1] = yFieldMax - Math.round(coordinates[1]);
		
		return coordsInt;		
	}
	
	
	abstract void setCoordinates(Float[] coordinates) throws Exception;
	abstract void setDirection(Float Dir) throws Exception;
	public void setVelocity(Float Vel){
		velocity = Vel;
	}
	
	abstract public void time();
}