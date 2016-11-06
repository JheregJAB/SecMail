package edu.depaul.secmail;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
	private Socket clientSocket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	
	ClientHandler(Socket s) {
		this.clientSocket = s;
		try {
			out = new ObjectOutputStream(new DHEncryptionWriter(clientSocket));
			in = new ObjectInputStream(new DHEncryptionReader(clientSocket));
		} catch (IOException e) {
			//TODO: handle this. For now, just output error and abort
			System.err.println(e);
			Log.Error("Exception creating ClientHandler: ");
			Log.Error(e.toString());
			System.exit(10);
		}
		
		
	}
	
	public void run()
	{
		Log.Debug("Starting ClientHandler");
		
		//do stuff here
		//this is example code copied from java docs
		//basically just an echo server at this point.
		try {
			PacketHeader nextPacket = null;
			while ((nextPacket = (PacketHeader)in.readObject()) != null) {
		        if (nextPacket.getCommand() == Command.CLOSE)
		        	break; // leave the loop
		        else
		        	processPacket(nextPacket);
		    }
		} catch (IOException e) {
			Log.Error("Error while trying to read or write to socket");
			Log.Error(e.toString());
		} catch (ClassNotFoundException e)
		{
			Log.Error("Error while trying to get object from network. Class not found");
			Log.Error(e.toString());
		}
		
		//we're done. close the stuff
		try {
			Log.Debug("Closing connection to client " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			System.err.println(e);
			Log.Error(e.toString());
		}
	}
	
	private void processPacket(PacketHeader ph)
	{
		Log.Debug("Processing packet for command " + ph.getCommand());
		
		switch(ph.getCommand()){
			case CONNECT_TEST:
				handleTestConnection();
				break;
			case LOGIN:
				handleLogin();
				break;
			case PASSWORD:
				handlePassword();
				break;
			case EMAIL:
				handleEmail();
				break;
			case ERROR:
				handleError();
				break;
		default:
			break;
				
			
		}
	}
	
	private void handleTestConnection()
	{
		//create successful connection packet
		PacketHeader successfulTestPacket = new PacketHeader();
		successfulTestPacket.setCommand(Command.CONNECT_SUCCESS);
		
		try {
			out.writeObject(successfulTestPacket);
		} catch (Exception e)
		{
			Log.Error("Exception thrown." + e);
		}
	}
	
	private void handleLogin(){
		// Handle Login packet here
	}
	
	private void handlePassword(){
		// Handle Password packet here
	}
	
	private void handleEmail(){
		// Handle Email packet here
	}
	
	private void handleError(){
		// Handle Error packet here
	}
}