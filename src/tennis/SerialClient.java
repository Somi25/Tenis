package tennis;

import java.awt.Point;
import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

import com.sun.org.apache.xpath.internal.operations.Bool;

public class SerialClient extends Network {

	private Socket socket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;

	SerialClient(Control c) {
		super(c);
	}

	private class ReceiverThread implements Runnable {

		public void run() {
			try {
				while (true) {
					Object received = in.readObject();
					if(received instanceof Ball)
					 {
						
					 }
					if(received instanceof Scores)
					 {
						
					 }
					if(received instanceof Racket)
					 {
						
					 }
					if(received instanceof Racket)
					 {
						
					 }
					if(received instanceof Boolean)
					 {
						
					 }
					
					
					
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				System.err.println("Server disconnected!");
			} finally {
				disconnect();
			}
		}
	}

	void send(Key toSend) {
		if (out == null)
			return;
		try {
			out.writeObject(toSend);
			out.flush();
		} catch (IOException ex) {
			System.err.println("Send error.");
		}
	}
	
	@Override
	void connect(String ip) {
		disconnect();
		try {
			socket = new Socket(ip, 10007);

			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			out.flush();

			Thread rec = new Thread(new ReceiverThread());
			rec.start();
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection. ");
			JOptionPane.showMessageDialog(null, "Cannot connect to server!");
		}
	}

	@Override
	void disconnect() {
		try {
			if (out != null)
				out.close();
			if (in != null)
				in.close();
			if (socket != null)
				socket.close();
		} catch (IOException ex) {
			System.err.println("Error while closing conn.");
		}
	}
}
