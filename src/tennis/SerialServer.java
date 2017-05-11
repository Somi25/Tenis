package tennis;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerialServer extends Network {

	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private DataOutputStream out = null;
	private BufferedReader in = null;
	
	private String keyName;
	private Boolean keyState;
	
	private Boolean gotKeyName = false;
	private Boolean gotKeyState = false;

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
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				out = new DataOutputStream(clientSocket.getOutputStream());
			} catch (IOException ex) {
				control.networkError(ex,"SERVER_CONSTRUCTOR");
				System.err.println("Error while getting streams.");
				disconnect();
				return;
			}

			try {
				String received;
				while (true) {
					System.out.println("OK2");
					while((received = in.readLine()) != null)
					{
						if(received.charAt(0) == 'n')
						 {
							keyName=received.substring(1).trim();
							gotKeyName=true;
						if(received.charAt(0) == 's')
						{
							keyState=Boolean.parseBoolean(received.substring(1).trim());
							gotKeyState=true;
						}
							if(gotKeyName && gotKeyState)
							{
								gotKeyName=false; gotKeyState=false; control.keyReceived();
							}
						 }
					}
				} 
			}catch (IOException ex) {
				//control.networkError(ex,"SERVER_READOBJECT");
				System.out.println(ex.getMessage());
				System.err.println("Client disconnected! - IO");
			} catch (ClassNotFoundException ex) {
				//control.networkError(ex,"SERVER_READOBJECT");
				System.out.println(ex.getMessage());
				System.err.println("Client disconnected! - CLASS");
			} finally {
				//disconnect();
			}
			System.out.println("OK3");
		}
	}

	void sendStates(Ball ball_ins, Racket racketL, Racket racketR) {
		if (out == null)
			return;
		try {
			String floatToString;
			
			floatToString = "B0"+Float.toString(ball_ins.getCoordinates()[0]) + '\n';
			out.writeBytes(floatToString);
			floatToString = "B1"+Float.toString(ball_ins.getCoordinates()[1]) + '\n';
			out.writeBytes(floatToString);


			floatToString = "L0"+Float.toString(racketL.getCoordinates()[0]) + '\n';
			out.writeBytes(floatToString);
			floatToString = "L1"+Float.toString(racketL.getCoordinates()[1]) + '\n';
			out.writeBytes(floatToString);
			

			floatToString = "R0"+Float.toString(racketR.getCoordinates()[0]) + '\n';
			out.writeBytes(floatToString);
			floatToString = "R1"+Float.toString(racketR.getCoordinates()[1]) + '\n';
			out.writeBytes(floatToString);
			
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_SENDSTATES");
			System.err.println("Send error.");
		}
	}
	void sendScore(Scores toSend){
		try {
			String intToString;
			
			intToString = "sL" + Integer.toString(toSend.getScores()[0]) + '\n';
			out.writeBytes(intToString);
			intToString = "sR" + Integer.toString(toSend.getScores()[1]) + '\n';
			out.writeBytes(intToString);
			
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
