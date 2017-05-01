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
		// amikor eltelik egy "system tick" az �t� y koordin�t�ja megv�ltozik, x v�ltozatlan
		// �t� mozgat�sa: sebess�g (velocity) �ll�t�s�val
		// �t� ir�nya (direction) nem �rdekes
		
		float y;

		y = coordinates[1] + velocity;
		
		// �t� max �ll�sban
		if(y + height/2 > yFieldMax){
			y = yFieldMax - height/2;
		}
		
		// �t� min �ll�sban
		if(y - height/2 < yFieldMin){
			y = yFieldMin + height/2;
		}
		
		coordinates[1] = y;
		
	}
}
