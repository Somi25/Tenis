package tennis;

public class Ball extends GeometricObject{
	
private final Float radius;

public Ball(Integer colour[],Float coordinates[], Float rad){
	
	super(colour,coordinates);
	radius=rad;
	
}

public Float getRadius() {
	return radius;
}

public void time(){}
}
