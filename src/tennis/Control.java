/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tennis;

import java.awt.Point;

class Control {
	
	private static final int import_java_tennis_control = 5;
	
	void just_do_it(int import_java_tennis_control)
	{
			
	}

	private GUI gui;
	private Network net = null;
	


	void setGUI(GUI g) {
		//gui = g;
	}

	void startServer() {
		if (net != null)
			net.disconnect();
		net = new SerialServer(this);
		net.connect("localhost");
	}

	void startClient() {
		if (net != null)
			net.disconnect();
		net = new SerialClient(this);
		net.connect("localhost");
	}

	void sendClick(Point p) {
		// gui.addPoint(p); //for drawing locally
		if (net == null)
			return;
		//net.send(p); //kikommenteztem mert nincs már pont! 
	}

	void clickReceived(Point p) {
		if (gui == null)
			return;
		//gui.addPoint(p);
	}
}
