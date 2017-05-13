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

	Boolean sendConnected = false;
	Boolean startedSendConnection = false;
	Boolean listenConnected = false;
	Boolean startedListenConnection = false;
	
SerialServer(Control c) {
		super(c);
		sendPort=10007;
		listenPort=10006;
	}
	private class connectThread implements Runnable {

	public void run() {
			try {
				System.out.println("Waiting for Client ->to send");
				sendSocket = serverSendSocket.accept();
				
				sendConnected=true;
				startedSendConnection=false;
				out = new DataOutputStream(sendSocket.getOutputStream());
				System.out.println("Client connected ->to send");
			} catch (IOException e) {
				control.networkError(e,"SERVER_WAITING");
				System.err.println("Accept failed.send");
				disconnectSend();
			}
		}
	}
	private class ReceiverThread implements Runnable {

		public void run() {
			try {
				System.out.println("Waiting for Client ->to receive");
				listenSocket = serverListenSocket.accept();
				listenConnected = true;
				startedListenConnection =false;
				System.out.println("Client connected ->to receive");
			} catch (IOException ex) {
				control.networkError(ex,"SERVER_WAITING");
				System.err.println("Accept failed.listen");
				disconnectListen();
			}

			try {
				in = new BufferedReader(new InputStreamReader(listenSocket.getInputStream()));
			} catch (IOException ex) {
				control.networkError(ex,"SERVER_CONSTRUCTOR");
				System.err.println("Error in = new BufferedReader");
				disconnectListen();
				return;
			}

			try {
				String received;
				while (true) {
					while((received = in.readLine()) == null);
					switch(received.charAt(0))
					{
					case 'K':
						got.setName(received.substring(1).trim());
						received = in.readLine();
						got.setState(Boolean.parseBoolean(received.trim()));
						control.keyReceived(got);
						break;
					case 'E':
						control.exitGame();
						break;
					default: break;
					}
				} 
			}catch (IOException ex) {
				//control.networkError(ex,"SERVER_READOBJECT");
				System.out.println(ex.getMessage());
				System.err.println("Client disconnected! - RecThread.Vege");
				disconnectListen();
				connect();
			}
		}
	}

	void sendBall(Ball ball_ins)
	{
		if (out == null)
			connect();
		try {
			String floatToString;
			
			floatToString = "B"+Float.toString(ball_ins.getCoordinates()[0]) + '\n' + Float.toString(ball_ins.getCoordinates()[1]) + '\n';
			out.writeBytes(floatToString);

		} catch (IOException ex) {
			control.networkError(ex,"SERVER_SENDSTATES");
			System.err.println("Send error.");
			disconnectSend();
		}
	}
	void sendRacketL(Racket racketL)
	{
		if (out == null)
			connect();
		try {
			String floatToString;

			floatToString = "L"+Float.toString(racketL.getCoordinates()[1]) + '\n';
			out.writeBytes(floatToString);
			
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_SENDSTATES");
			System.err.println("Send error.");
			disconnectSend();
		}
	}
	void sendRacketR(Racket racketR)
	{
		if (out == null)
			connect();
		try {
			String floatToString;	

			floatToString = "R"+Float.toString(racketR.getCoordinates()[1]) + '\n';
			out.writeBytes(floatToString);
			
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_SENDSTATES");
			System.err.println("Send error.");
			disconnectSend();
		}
	}
	void sendScore(Scores toSend){
		try {
			if (out == null)
				connect();
			String intToString;
			
			intToString = "S" + Integer.toString(toSend.getScores()[0]) + '\n'+ Integer.toString(toSend.getScores()[1]) + '\n';
			out.writeBytes(intToString);
			
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_SENDSCORE");
			System.err.println("Send error.");
			disconnectSend();
		}
	}
	void sendPause(Integer toSend){
		try {
			if (out == null)
				connect();
			String intToString;
			
			intToString = "G" + Integer.toString(toSend) + '\n';
			out.writeBytes(intToString);
			
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_SENDSCORE");
			System.err.println("Send error.");
			disconnectSend();
		}
	}
	
	void connect() {
		try {
			if(!startedListenConnection && !listenConnected)
			{
				startedListenConnection=true;
				serverListenSocket = new ServerSocket(listenPort);
				Thread listen = new Thread(new ReceiverThread());
				listen.start();
			}
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_CONNECT");
			System.err.println("Could not listen on port: 10007.");
			disconnectListen();
		}
		try{
			if(!startedSendConnection && !sendConnected)
			{
				startedSendConnection=true;
				serverSendSocket = new ServerSocket(sendPort);
				Thread sendConnect = new Thread(new connectThread());
				sendConnect.start();
			}
		}catch (IOException ex) {
			control.networkError(ex,"SERVER_CONNECT");
			System.err.println("Could not send on port: 10006.");
			disconnectSend();
		}
	}

	@Override
	void disconnectListen() {
		try {
			listenConnected = false;
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
	void disconnectSend() {
		try {
			
			sendConnected=false;
			
			if (out != null)
				out.close();
			if (sendSocket != null)
				sendSocket.close();
			if (serverSendSocket != null)
				serverSendSocket.close();
		} catch (IOException ex) {
			control.networkError(ex,"SERVER_DISCONNECT");
			Logger.getLogger(SerialServer.class.getName()).log(Level.SEVERE,null, ex);
		}
	}
	
	@Override	
	void disconnect() {
		disconnectListen();
		disconnectSend();
	}
	@Override
	void sendExit()
	{
		try {
			if(out==null)
				connect();
			out.writeBytes("E\n");
		} catch (IOException ex) {
			control.networkError(ex,"CLIENT_SENDKEY");
			System.err.println("Send error.");
			disconnectSend();
		}
	}
}
