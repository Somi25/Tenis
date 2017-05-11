package tennis;

abstract class Network {

	protected Control control;

	Network(Control c) {
		control = c;
	}

	abstract void connect(String ip);

	abstract void disconnect();

}