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

	private ServerSocket serverListenSocket = null;
	private ServerSocket serverSendSocket = null;
	
	private Key got = new Key();
	
	private Boolean gotKeyName = false;
	private Boolean gotKeyState = false;

	private Boolean alreadySend = false;

	private Thread rec;
	
SerialServer(Control c) {
		super(c);
		sendPort=10007;
		listenPort=10006;
	}
	
	private class ReceiverThread implements Runnable {

		public void run() {
			try {
				System.out.println("Waiting for Client ->to receive");
				listenSocket = serverListenSocket.accept();
				System.out.println("Client connected ->to receive");
			} catch (IOException ex) {
				control.networkError(ex,"SERVER_WAITING");
				System.err.println("Accept failed.");
				disconnect();
				return;
			}

			System.out.println("OK1");
			try {
				in = new BufferedReader(new InputStreamReader(listenSocket.getInputStream()));
			} catch (IOException ex) {
				control.networkError(ex,"SERVER_CONSTRUCTOR");
				System.err.println("Error while getting streams.");
				disconnect();
				return;
			}

			try {
				String received;
				while (true) {
					while((received = in.readLine()) != null)
					{
						System.out.println(received);
						switch(received.charAt(0))
						{
						case 'n':
							got.setName(received.substring(1).trim());
							gotKeyName=true;
							break;
						case 's':
							got.setState(Boolean.parseBoolean(received.substring(1).trim()));
							gotKeyState=true;
							break;
						case 'E': control.exitGame();
						default:
						}
						if(gotKeyName && gotKeyState)
						{
							gotKeyName=false; gotKeyState=false;
							System.out.println("got key");
							control.keyReceived(got);
						}
					}
				} 
			}catch (IOException ex) {
				//control.networkError(ex,"SERVER_READOBJECT");
				System.out.println(ex.getMessage());
				System.err.println("Client disconnected! - IO");
			}
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
	
	void connect() {
		disconnect();
		try {
			if(!alreadySend)
			{
				serverSendSocket = new ServerSocket(sendPort);
				serverListenSocket = new ServerSocket(listenPort);
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
			if (listenSocket != null)
				listenSocket.close();
			if (serverListenSocket != null)
				serverListenSocket.close();
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_DISCONNECT");
			Logger.getLogger(SerialServer.class.getName()).log(Level.SEVERE,null, ex);
		}
	}
	@Override	
	void disconnectAll() {
		try {
			if (out != null)
				out.close();
			if (in != null)
				in.close();
			if (listenSocket != null)
				listenSocket.close();
			if (serverListenSocket != null)
				serverListenSocket.close();
			if (serverSendSocket != null)
				listenSocket.close();
			if (serverListenSocket != null)
				serverListenSocket.close();
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_DISCONNECT");
			Logger.getLogger(SerialServer.class.getName()).log(Level.SEVERE,null, ex);
		}
	}
	@Override
	void sendExit()
	{
		try {
			out.writeBytes("E\n");
		} catch (IOException ex) {
			control.networkError(ex,"CLIENT_SENDKEY");
			System.err.println("Send error.");
			disconnect();
		}
	}
}
