package tennis;

abstract class Network {

	protected Control ctrl;

	Network(Control c) {
		ctrl = c;
	}

	abstract Boolean connect(String ip);

	abstract void disconnect();

}