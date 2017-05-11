package tennis;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class Key implements Serializable {
	private String name;
	private Boolean state;
	
	Key(String key, Boolean state)
	{
		this.name=key; this.state=state;
	}
	public String getName() {
		return name;
	}
	public Boolean getState() {
		return state;
	}
	public void writeObject(ObjectOutputStream out) throws IOException
	{
		System.out.println("belepett");
        out.writeObject(this.name);
        out.writeObject(this.state);
	}
    private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException {
		System.out.println("olvas");
        this.name=(String)in.readObject();
        this.state=(Boolean)in.readObject();
		System.out.println("olvasVege");
    }
}
