package tennis;

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
}
