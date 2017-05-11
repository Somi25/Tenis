package tennis;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

public class SerialClient extends Network {

	private Socket socket = null;
	private DataOutputStream out = null;
	private BufferedReader in = null;
	
	private Float[] ball_coord = new Float[2];
	private Float[] racketL_coord = new Float[2];
	private Float[] racketR_coord = new Float[2];
	private Integer[] score = new Integer[2];
	
	private Boolean gotBall0 = false;
	private Boolean gotBall1 = false;

	private Boolean gotRacketL0 = false;
	private Boolean gotRacketL1 = false;

	private Boolean gotRacketR0 = false;
	private Boolean gotRacketR1 = false;
	
	private Boolean gotScoreL;
	private Boolean gotScoreR;
	
	SerialClient(Control c) {
		super(c);
	}

	private class ReceiverThread implements Runnable {

		public void run() {
			try {
				while (true) {
					String received = in.readLine();
					if(received.charAt(0) == 'B')
					 {
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
							gotBall0=false; gotBall1=false; control.setBall_inst();
						}
					 }
					else
					{
					if(received.charAt(0) == 'L')
					 {
						if(received.charAt(1) == '0')
						{
							racketL_coord[0]=Float.parseFloat(received.substring(2).trim());
							gotRacketL0=true;
						}
						if(received.charAt(1) == '1')
						{
							racketL_coord[0]=Float.parseFloat(received.substring(2).trim());
							gotRacketL1=true;
						}
						if(gotRacketL0 && gotRacketL1)
						{
							gotRacketL0=false; gotRacketL1=false; control.setRacketL();
						}
					 }
					else
					{
					if(received.charAt(0) == 'R')
					 {
						if(received.charAt(1) == '0')
						{
							racketR_coord[0]=Float.parseFloat(received.substring(2).trim());
							gotRacketR0=true;
						}
						if(received.charAt(1) == '1')
						{
							racketR_coord[0]=Float.parseFloat(received.substring(2).trim());
							gotRacketR1=true;
						}
						if(gotRacketR0 && gotRacketR1)
						{
							gotRacketR0=false; gotRacketR1=false; control.setRacketR();
						}
					 }
					else
					{
					if(received.charAt(0) == 's')
					 {
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
							gotScoreL=false; gotScoreR=false; control.setScore();
						}
					 }
					}
					}
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
		if (out == null)
			return;
		try {
			String sentence;
			System.out.println("sendKey");
			
			sentence = 'n'+toSend.getName();
			out.writeBytes(sentence + '\n');
			sentence = 's'+Boolean.toString(toSend.getState());
			out.writeBytes(sentence + '\n');
			
		} catch (IOException ex) {
			control.networkError(ex,"CLIENT_SENDKEY");
			System.err.println("Send error.");
		}
	}
	
	@Override
	Boolean connect(String ip) {
		disconnect();
		try {
			socket = new Socket(ip,10007);
			out = new DataOutputStream(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			Thread rec = new Thread(new ReceiverThread());
			rec.start();
			return true;
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
			control.networkError(ex,"CLIENT_DISCONNECT");
			System.err.println("Error while closing conn.");
		}
	}
}
