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
