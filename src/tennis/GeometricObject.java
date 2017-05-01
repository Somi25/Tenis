package tennis;

public abstract class GeometricObject {
	
private Integer[] colour = new Integer[3];
private Float[] coordinates = new Float[2];
private Float direction = null;
private Float velocity = null;

abstract void time();
public GeometricObject(Integer Colour[],Float[] Coord) {
	if(Colour.length==3)
		colour=Colour;
	if(Coord.length==3)
		coordinates=Coord;
	velocity=(float) 0;
	direction=(float) 0;
}

void setDirection(Float Dir){
	direction=Dir;
}

void setVelocity(Float Vel){
	velocity=Vel;
}

}