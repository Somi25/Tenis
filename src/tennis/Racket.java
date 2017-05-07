package tennis;

import java.security.InvalidParameterException;

public class Racket extends GeometricObject{
	
	private final Float width;
	private final Float height;
	
	public Racket(Integer colour[],Float coordinates[], Float wid, Float hei) throws Exception{

		super(colour);
		
		Exception InvalidParameterException = null;
		if(wid<250 && wid>1)
		{
			this.height=hei;
		}
		else
		{
			throw InvalidParameterException;
		}
		if(hei<100 && hei>1)
		{
			this.width=wid;
		}
		else
		{
			throw InvalidParameterException;
		}
		if(coordinates[0]>wid/2 && coordinates[0]<500-wid/2)
		{
			this.coordinates[0] = coordinates[0];
		}
		else
		{
			throw InvalidParameterException;
		}
		setCoordinates(coordinates);
		setDirection(null);
	}

	@Override
	void setCoordinates(Float[] coordinates) throws Exception {
		if(coordinates[1]<(this.height/2) && coordinates[1]>(200-(this.height/2)))
		{
			this.coordinates[1]=coordinates[1];
		}
		else 
		 {
			Exception InvalidParameterException = null;
			throw InvalidParameterException;
		 }
		
	}
	@Override
	void setDirection(Float Dir) throws Exception {
		this.direction=(float) (Math.PI/2);
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
