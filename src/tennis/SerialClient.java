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
	
	private Boolean gotBall0 = false;
	private Boolean gotBall1 = false;
	
	private Boolean gotScoreL = false;
	private Boolean gotScoreR = false;
	
	private Boolean alreadyListen = false;
	
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
						if(received.charAt(1) == '0')
						{
							ball_coord[0]=Float.parseFloat(received.substring(2).trim());
							gotBall0=true;
						}
						if(received.charAt(1) == '1')
						{
							ball_coord[1]=Float.parseFloat(received.substring(2).trim());
							gotBall1=true;
						}
						if(gotBall0 && gotBall1)
						{
							gotBall0=false; gotBall1=false; control.setBall_inst(ball_coord);
						}
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
						if(received.charAt(1) == 'L')
						{
							score[0]=Integer.parseInt(received.substring(2).trim());
							gotScoreL=true;
						}
						if(received.charAt(1) == 'R')
						{
							score[1]=Integer.parseInt(received.substring(2).trim());
							gotScoreR=true;
						}
						if(gotScoreL && gotScoreR)
						{
							gotScoreL=false; gotScoreR=false; control.setScore(score);
						}
						break;
					case 'G':
						switch (Integer.parseInt(received.substring(1).trim()))
						{
						case 0: control.continueGame(); break;
						case 1: control.pauseGame(); break;
						default:
						}
					case 'E': control.exitGame();
					}
				}
			} catch (Exception ex) {
				control.networkError(ex,"CLIENT_READOBJECT");
				System.out.println(ex.getMessage());
				System.err.println("Server disconnected!");
			} finally {
				disconnect();
			}
		}
	}

	void send(Key toSend) {
		try {
			if (out == null)
				connect();
			String sentence;
			System.out.println("sendKey");
			
			sentence = 'n'+toSend.getName();
			out.writeBytes(sentence + '\n');
			sentence = 's'+Boolean.toString(toSend.getState());
			out.writeBytes(sentence + '\n');
			
		} catch (IOException ex) {
			control.networkError(ex,"CLIENT_SENDKEY");
			System.err.println("Send error.");
			disconnect();
		}
	}

	void connect(){
		connect(joinedIP);
	}
	void connect(String ip) {
		disconnect();
		joinedIP=ip;
		try {
			if(!alreadyListen)
			{
				listenSocket = new Socket(ip,listenPort);
				in = new BufferedReader(new InputStreamReader(listenSocket.getInputStream()));
				
				alreadyListen = true;
				
				Thread rec = new Thread(new ReceiverThread());
				rec.start();
				}
			else
			{
				sendSocket = new Socket(ip,sendPort);
				out = new DataOutputStream(sendSocket.getOutputStream());
				System.out.println("out inicializalva");
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
	void disconnect() {
		try {
			if (out != null)
				out.close();
			if (sendSocket != null)
				sendSocket.close();
		} catch (IOException ex) {
			control.networkError(ex,"CLIENT_DISCONNECT");
			System.err.println("Error while closing conn.");
		}
		System.out.println("disconnected");
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
			if (sendSocket != null)
				sendSocket.close();
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
