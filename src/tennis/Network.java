package tennis;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

abstract class Network {

	protected Control control;

	protected Socket sendSocket;
	protected Socket listenSocket;
	
	protected Integer sendPort;
	protected Integer listenPort;

	protected DataOutputStream out;
	protected BufferedReader in;
	
	Network(Control c) {
		control = c;
	}

	abstract void disconnect();
	abstract void disconnectAll();

}