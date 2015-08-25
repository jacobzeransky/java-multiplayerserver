package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingDeque;

import objects.internalMsg;


public class mpsClient {
	private Socket ssocket;
	private PrintWriter out;
	private BufferedReader in;
	private String hostname;
	private int portnumber;
	private LinkedBlockingDeque<String> event_q;
	
	public mpsClient(String host, int port){
		hostname = host;
		portnumber = port;
		event_q = new LinkedBlockingDeque<String>();
	}
	
	public boolean connect(){

		try {
			ssocket = new Socket(hostname, portnumber);
			out = new PrintWriter(ssocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(ssocket.getInputStream()));
			event_q.offer("Connected");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			event_q.offer("Connection error: "+e);
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			event_q.offer("Connection error: "+e);
			return false;
		}
		return true;
	}
	
	public boolean disconnect(){
		try {
			out.println("005");
			ssocket.close();
			out = null;
			in = null;
			event_q.offer("Disconnected");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			event_q.offer("Disconnection error: "+e);
			return false;
		}
		return true;
	}
	
	public int login(String u, char[] p){
		String msg = "002" + u + ":";
		for (int i=0;i<p.length;i++){
			msg += p[i];
		}
		out.println(msg);
		String respmsg;
		while (true){
			try{
				respmsg = in.readLine();
				
				if (respmsg != null){
					
					return Integer.parseInt(respmsg);
				}
			} catch (IOException e) {
				event_q.offer("Login error: "+e);
			}
		}
		
	}
	
 /*   public static void main(String[] args) {
        DesktopApplicationContext.main(clientView.class, args);
        
        try (   Socket ssocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(ssocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(ssocket.getInputStream()));
            ) {
                BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));
                String fromServer;
                String fromUser;
     
                while ((fromServer = in.readLine()) != null) {
                    System.out.println("Server: " + fromServer);
                    if (fromServer.equals("Bye."))
                        break;
                     
                    fromUser = stdIn.readLine();
                    if (fromUser != null) {
                        System.out.println("Client: " + fromUser);
                        out.println(fromUser);
                    }
                }
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host " + hostName);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
                System.exit(1);
            }
    }*/
}
