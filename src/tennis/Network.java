package tennis;

abstract class Network {

	protected Control control;

	Network(Control c) {
		control = c;
	}

	abstract Boolean connect(String ip);

	abstract void disconnect();

}