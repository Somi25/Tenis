package tennis;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class Key {
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
        out.writeObject(this.name);
        out.writeObject(this.state);
	}
    private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException {
        in.defaultReadObject();
    }
}
