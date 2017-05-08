package tennis;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

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
						ctrl.setBall_inst((Ball) received); 
					 }
					if(received instanceof Racket)
					 {
						if(((Racket) received).getCoordinates()[0]<50)//szebbre!
						 {
							ctrl.setRacketL((Racket) received); 
						 }
						if(((Racket) received).getCoordinates()[0]>1220)//szebbre!
						 {
							ctrl.setRacketR((Racket) received);
						 }
					 }
					if(received instanceof Boolean)//game state (paused or not)
					 {
						ctrl.setGameState((Boolean) received);
					 }
					if(received instanceof Scores)//pontok
					 {
						ctrl.setScore((Scores) received);
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
	Boolean connect(String ip) {
		disconnect();
		try {
			socket = new Socket(ip,10007);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			out.flush();

			Thread rec = new Thread(new ReceiverThread());
			rec.start();
			return true;
		} catch (IllegalArgumentException e) {
			System.err.println("One of the arguments are illegal");
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection. ");
			JOptionPane.showMessageDialog(null, "Cannot connect to server!");
		} finally{
			disconnect();
			return false;
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
