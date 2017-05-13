package tennis;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class SerialClient extends Network {
	
	private Float[] ball_coord = new Float[2];
	private Float[] racketL_coord = new Float[2];
	private Float[] racketR_coord = new Float[2];
	private Integer[] score = new Integer[2];
	
	Boolean sendConnected = false;
	Boolean startedSendConnection = false;
	Boolean listenConnected = false;
	Boolean startedListenConnection = false;
	
	public String joinedIP;
	SerialClient(Control c) {
		super(c);
		sendPort=10006;
		listenPort=10007;
	}

	private class ReceiverThread implements Runnable {

		public void run() {
			try {
				while (true) {
					String received = in.readLine();
					switch(received.charAt(0))
					{
					case 'B':
							ball_coord[0]=Float.parseFloat(received.substring(1).trim());
							while((received = in.readLine()) == null);
							ball_coord[1]=Float.parseFloat(received.trim());
							control.setBall_inst(ball_coord);
						break;
						
					case 'L':
						racketL_coord[1]=Float.parseFloat(received.substring(1).trim());
						control.setRacketL(racketL_coord);
						break;
						
					case 'R':
						racketR_coord[1]=Float.parseFloat(received.substring(1).trim());
						control.setRacketR(racketR_coord);
						break;
						
					case 'S':
						score[0]=Integer.parseInt(received.substring(1).trim());
						while((received = in.readLine()) == null);
						score[1]=Integer.parseInt(received.trim());
						control.setScore(score);
						break;
					case 'G':
						switch (Integer.parseInt(received.substring(1).trim()))
						{
						case 0: control.continueGame(); break;
						case 1: control.pauseGame(); break;
						default:
						}
						break;
					case 'E': control.exitGame(); break;
					}
				}
			} catch (Exception ex) {
				control.networkError(ex,"CLIENT_READOBJECT");
				System.out.println(ex.getMessage());
				System.err.println("Server disconnected!");
				disconnectListen();
				connect();
			}
		}
	}

	void send(Key toSend) {
		try {
			if (out == null)
				connect();
			String sentence;
			System.out.println("sendKey");
			
			sentence = 'K'+toSend.getName() + '\n' + Boolean.toString(toSend.getState())+ '\n';
			out.writeBytes(sentence);
			
		} catch (IOException ex) {
			control.networkError(ex,"CLIENT_SENDKEY");
			System.err.println("Send error.");
			disconnectSend();
		}
	}

	void connect(){
		connect(joinedIP);
	}
	void connect(String ip) {
		joinedIP=ip;
		try {
			if(!startedSendConnection && !sendConnected)
			{
				startedSendConnection=true;
				sendSocket = new Socket(ip,sendPort);
				out = new DataOutputStream(sendSocket.getOutputStream());
				
				sendConnected=true;
				startedSendConnection=false;
			}
			if(!startedListenConnection && !listenConnected)
			{
				startedListenConnection = true;
				System.out.println("out inicializalva");
				listenSocket = new Socket(ip,listenPort);
				in = new BufferedReader(new InputStreamReader(listenSocket.getInputStream()));
				
				listenConnected=true;
				startedListenConnection = false;
				
				Thread rec = new Thread(new ReceiverThread());
				rec.start();
			}
		} catch (IllegalArgumentException ex) {
			control.networkError(ex,"CLIENT_CONSTRUCT");
			System.err.println("One of the arguments are illegal");
		} catch (UnknownHostException ex) {
			control.networkError(ex,"CLIENT_CONNECT");
			System.err.println("Don't know about host");
		} catch (IOException ex) {
			control.networkError(ex,"CLIENT_CONNECT");
			System.err.println("Couldn't get I/O for the connection. ");
			JOptionPane.showMessageDialog(null, "Cannot connect to server!");
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
		} catch (IOException ex) {
			control.networkError(ex,"CLIENT_DISCONNECT");
			System.err.println("Error while closing conn.");
		}
		System.out.println("disconnectedSend");
	}
	@Override
	void disconnectListen() {
	try{
		listenConnected=false;
		if (in != null)
			in.close();
		if (listenSocket != null)
			listenSocket.close();
	} catch (IOException ex) {
		control.networkError(ex,"CLIENT_DISCONNECT");
		System.err.println("Error while closing conn.");
	}
	System.out.println("disconnectedListen");
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
