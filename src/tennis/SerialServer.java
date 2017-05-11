package tennis;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

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
			} catch (IOException ex) {
				control.networkError(ex,"SERVER_WAITING");
				System.err.println("Accept failed.");
				disconnect();
				return;
			}

			System.out.println("OK1");
			try {
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				in = new ObjectInputStream(clientSocket.getInputStream());
				out.flush();
			} catch (IOException ex) {
				control.networkError(ex,"SERVER_CONSTRUCTOR");
				System.err.println("Error while getting streams.");
				disconnect();
				return;
			}

			try {
				while (true) {
					System.out.println("OK2");
					Object received =in.readObject();
					System.out.println("OK23");
					if(received != null) 
					{
						control.keyReceived((Key)received);
						System.out.println("OK4");
					}
				}
			} catch (Exception ex) {
				control.networkError(ex,"SERVER_READOBJECT");
				System.out.println(ex.getMessage());
				System.err.println("Client disconnected!");
			} finally {
				disconnect();
			}
			System.out.println("OK3");
		}
	}

	void sendStates(Ball ball_ins, Racket racketL, Racket racketR, Boolean gameState) {
		if (out == null)
			return;
		try {
			out.writeObject(ball_ins);
			out.writeObject(racketL);
			out.writeObject(racketR);
			out.writeObject(gameState);
			out.flush();
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_SENDSTATES");
			System.err.println("Send error.");
		}
	}
	void sendScore(Scores toSend){
		try {
			out.writeObject(toSend);
			out.flush();
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_SENDSCORE");
			System.err.println("Send error.");
		}
	}

	@Override
	Boolean connect(String ip) {
		disconnect();
		try {
			serverSocket = new ServerSocket(10007);

			Thread rec = new Thread(new ReceiverThread());
			rec.start();
			return true;
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_CONNECT");
			System.err.println("Could not listen on port: 10007.");
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
			if (clientSocket != null)
				clientSocket.close();
			if (serverSocket != null)
				serverSocket.close();
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_DISCONNECT");
			Logger.getLogger(SerialServer.class.getName()).log(Level.SEVERE,null, ex);
		}
	}
}
