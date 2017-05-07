package tennis;
// @TODO:
// pálya méreteket konstruktorba állítani gui fv-nyel

public abstract class GeometricObject {
	
	// pálya méretek
	protected final Integer xFieldMax = 200;
	protected final Integer xFieldMin = 0;
	protected final Integer yFieldMax = 500;
	protected final Integer yFieldMin = 0;
	
	protected Integer[] colourRGB = new Integer[3];
	protected Float[] coordinates = new Float[2];
	protected Float direction;		// [0, 2*pi) intervallum, 0 irány: D-kr x tengelye (óramutató járásával ellentétes irányú)
	protected Float velocity;		// "pixel / time_tick" mértékegység
	
	public GeometricObject(Integer Colour[]) throws Exception {
		if(Colour.length == 3 && 0<Colour[0] && Colour[0]<256 && 0<Colour[1] && Colour[1]<256 && 0<Colour[2] && Colour[2]<256)
			colourRGB=Colour;
		else 
		 {
			Exception InvalidParameterException = null;
			throw InvalidParameterException;
		 }
		velocity = (float)0;
		direction = (float)0;
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
	
	abstract void setCoordinates(Float[] coordinates) throws Exception;
	abstract void setDirection(Float Dir) throws Exception;
	public void setVelocity(Float Vel){
		velocity = Vel;
	}
	
	abstract public void time();
}