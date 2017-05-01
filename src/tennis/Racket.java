package tennis;

public class Racket extends GeometricObject{
	
private final Float width;
private final Float height;

public Racket(Integer colour[],Float coordinates[], Float wid, Float hei){
	
	super(colour,coordinates);
	width=wid;
	height=hei;
	
}

public Float getHeight() {
	return height;
}
public Float getWidth() {
	return width;
}

void time(){}
}
