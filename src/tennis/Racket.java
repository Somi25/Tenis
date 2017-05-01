package tennis;

public class Racket extends GeometricObject{
	
	private final Float width;
	private final Float height;
	
	public Racket(Integer colour[],Float coordinates[], Float wid, Float hei){
		
		super(colour,coordinates);
		width = wid;
		height = hei;
		
	}
	
	public Float getHeight() {
		return height;
	}
	public Float getWidth() {
		return width;
	}
	
	public void time(){
		// amikor eltelik egy "system tick" az ütõ y koordinátája megváltozik, x változatlan
		// ütõ mozgatása: sebesség (velocity) állításával
		// ütõ iránya (direction) nem érdekes
		
		float y;

		y = coordinates[1] + velocity;
		
		// ütõ max állásban
		if(y + height/2 > yFieldMax){
			y = yFieldMax - height/2;
		}
		
		// ütõ min állásban
		if(y - height/2 < yFieldMin){
			y = yFieldMin + height/2;
		}
		
		coordinates[1] = y;
		
	}
}
