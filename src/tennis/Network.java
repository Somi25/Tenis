package tennis;

abstract class Network {

	protected Control ctrl;

	Network(Control c) {
		ctrl = c;
	}

	abstract void connect(String ip);
	abstract void connect(String ip, String port);

	abstract void disconnect();

}
