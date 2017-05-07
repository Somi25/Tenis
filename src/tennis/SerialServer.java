package tennis;

import static java.lang.Math.pow;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.org.apache.xpath.internal.operations.Bool;

public class SerialServer extends Network {

	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;

	SerialServer(Control c) {
		super(c);
	}

	private class ReceiverThread implements Runnable {

		public void run() {
			try {
				System.out.println("Waiting for Client");
				clientSocket = serverSocket.accept();
				System.out.println("Client connected.");
			} catch (IOException e) {
				System.err.println("Accept failed.");
				disconnect();
				return;
			}

			try {
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				in = new ObjectInputStream(clientSocket.getInputStream());
				out.flush();
			} catch (IOException e) {
				System.err.println("Error while getting streams.");
				disconnect();
				return;
			}

			try {
				while (true) {
					Key received = (Key) in.readObject();
					ctrl.keyReceived(received);
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				System.err.println("Client disconnected!");
			} finally {
				disconnect();
			}
		}
	}

	void sendStates(Ball ball_ins, Racket racketL, Racket racketR, Bool gameState) {
		if (out == null)
			return;
		try {
			out.writeObject(ball_ins);
			out.writeObject(racketL);
			out.writeObject(racketR);
			out.writeObject(gameState);
			out.flush();
		} catch (IOException ex) {
			System.err.println("Send error.");
		}
	}
	void sendScore(Scores toSend){
		try {
			out.writeObject(toSend);
			out.flush();
		} catch (IOException ex) {
			System.err.println("Send error.");
		}
	}

	@Override
	void connect(String ip) {
		this.connect(ip,"10007");
	}
	@Override
	void connect(String ip,String port) {
		disconnect();
		try {
			int portInt=10007;
			if(!port.isEmpty() && port.length()<6)
			{
				for(int i=0; i<port.length();i++)
				{
					portInt += (Math.round(pow(10,i)))*((int)port.charAt(port.length()-i-1)-(int)'0');
				}
			}
			else
			{
				return;
			}
			serverSocket = new ServerSocket(portInt);

			Thread rec = new Thread(new ReceiverThread());
			rec.start();
		} catch (IOException e) {
			System.err.println("Could not listen on port: 10007.");
		}
	}

	@Override
	void disconnect() {
		try {
			if (out != null)
				out.close();
			if (in != null)
				in.close();
			if (clientSocket != null)
				clientSocket.close();
			if (serverSocket != null)
				serverSocket.close();
		} catch (IOException ex) {
			Logger.getLogger(SerialServer.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
}
