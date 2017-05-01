package tennis;

public abstract class GeometricObject {
	
private Integer[] colourRGB = new Integer[3];
private Float[] coordinates = new Float[2];
private Float direction;
private Float velocity;

public GeometricObject(Integer Colour[],Float Coord[]) {
	if(Colour.length==3)
		colourRGB=Colour;
	if(Coord.length==3)
		coordinates=Coord;
	velocity=(float) 0;
	direction=(float) 0;
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
	direction=Dir;
}
public void setVelocity(Float Vel){
	velocity=Vel;
}

abstract void time();
}