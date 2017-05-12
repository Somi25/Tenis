package tennis;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

public class SerialServer extends Network {

	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private BufferedReader in = null;

	private ServerSocket serverSendSocket = null;
	private Socket sendSocket = null;
	private DataOutputStream out = null;
	
	private String keyName;
	private Boolean keyState;
	
	private Boolean gotKeyName = false;
	private Boolean gotKeyState = false;

	private Boolean alreadySend = false;

	private Thread rec;
	
SerialServer(Control c) {
		super(c);
	}
	
	private class ReceiverThread implements Runnable {

		public void run() {
			try {
				System.out.println("Waiting for Client ->to receive");
				clientSocket = serverSocket.accept();
				System.out.println("Client connected ->to receive");
			} catch (IOException ex) {
				control.networkError(ex,"SERVER_WAITING");
				System.err.println("Accept failed.");
				disconnect();
				return;
			}

			System.out.println("OK1");
			try {
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			} catch (IOException ex) {
				control.networkError(ex,"SERVER_CONSTRUCTOR");
				System.err.println("Error while getting streams.");
				disconnect();
				return;
			}

			try {
				String received;
				System.out.println("whileTrueig eljutott");
				while (true) {
					while((received = in.readLine()) != null)
					{
						System.out.println(received);
						if(received.charAt(0) == 'n')
						 {
							keyName=received.substring(1).trim();
							gotKeyName=true;
						 }
						if(received.charAt(0) == 's')
						{
							keyState=Boolean.parseBoolean(received.substring(1).trim());
							gotKeyState=true;
						}
						if(gotKeyName && gotKeyState)
						{
							System.out.println("key is OK");
							gotKeyName=false; gotKeyState=false;
							control.keyReceived(new Key(keyName,keyState));
							System.out.println(keyName+" "+keyState.toString());
						}
					}
				} 
			}catch (IOException ex) {
				//control.networkError(ex,"SERVER_READOBJECT");
				System.out.println(ex.getMessage());
				System.err.println("Client disconnected! - IO");
			} finally {
				disconnect();
			}
			System.out.println("OK3");
		}
	}

	void sendBall(Ball ball_ins)
	{
		if (out == null)
			return;
		try {
			String floatToString;
			
			floatToString = "B0"+Float.toString(ball_ins.getCoordinates()[0]) + '\n';
			out.writeBytes(floatToString);
			floatToString = "B1"+Float.toString(ball_ins.getCoordinates()[1]) + '\n';
			out.writeBytes(floatToString);
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_SENDSTATES");
			System.err.println("Send error.");
		}
	}
	void sendRacketL(Racket racketL)
	{
		if (out == null)
			return;
		try {
			String floatToString;

			floatToString = "L"+Float.toString(racketL.getCoordinates()[1]) + '\n';
			out.writeBytes(floatToString);
			
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_SENDSTATES");
			System.err.println("Send error.");
		}
	}
	void sendRacketR(Racket racketR)
	{
		if (out == null)
			return;
		try {
			String floatToString;	

			floatToString = "R"+Float.toString(racketR.getCoordinates()[1]) + '\n';
			out.writeBytes(floatToString);
			
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_SENDSTATES");
			System.err.println("Send error.");
		}
	}
	void sendScore(Scores toSend){
		try {
			String intToString;
			
			intToString = "SL" + Integer.toString(toSend.getScores()[0]) + '\n';
			out.writeBytes(intToString);
			intToString = "SR" + Integer.toString(toSend.getScores()[1]) + '\n';
			out.writeBytes(intToString);
			
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_SENDSCORE");
			System.err.println("Send error.");
		}
	}
	void sendPause(Integer toSend){
		try {
			String intToString;
			
			intToString = "G" + Integer.toString(toSend) + '\n';
			out.writeBytes(intToString);
			
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_SENDSCORE");
			System.err.println("Send error.");
		}
	}

	@Override
	void connect(String ip) {
		disconnect();
		try {
			if(!alreadySend)
			{
				serverSendSocket = new ServerSocket(10007);
				serverSocket = new ServerSocket(10006);
				System.out.println("Waiting for Client ->to send");
				sendSocket = serverSendSocket.accept();
				System.out.println("Connected Client ->to send");
				out = new DataOutputStream(sendSocket.getOutputStream());
				alreadySend = true;
				rec = new Thread(new ReceiverThread());
				rec.start();
			}
			else
			{
				rec.start();
			}
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_CONNECT");
			System.err.println("Could not listen on port: 10007.");
		}
	}

	@Override
	void disconnect() {
		try {
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
	void disconnectAll() {
		try {
			if (out != null)
				out.close();
			if (in != null)
				in.close();
			if (clientSocket != null)
				clientSocket.close();
			if (serverSocket != null)
				serverSocket.close();
			if (serverSendSocket != null)
				clientSocket.close();
			if (serverSocket != null)
				serverSocket.close();
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_DISCONNECT");
			Logger.getLogger(SerialServer.class.getName()).log(Level.SEVERE,null, ex);
		}
	}
}
