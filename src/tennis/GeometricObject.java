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
	protected Float direction;						// [0, 2*pi) intervallum, 0 irány: D-kr x tengelye (óramutató járásával ellentétes irányú)
	protected Float velocity;						// "pixel / time_tick" mértékegység
	
	public GeometricObject(Integer Colour[],Float Coord[]) {
		if(Colour.length == 3)
			colourRGB=Colour;
		if(Coord.length == 2)
			coordinates=Coord;
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
	
	public void setCoordinates(Float[] coordinates) {
		this.coordinates = coordinates;
	}
	public void setDirection(Float Dir){
		direction = Dir;
	}
	public void setVelocity(Float Vel){
		velocity = Vel;
	}
	
	abstract public void time();
}