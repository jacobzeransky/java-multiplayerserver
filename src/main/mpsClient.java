package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import objects.internalMsg;


public class mpsClient {
	Socket ssocket;
	PrintWriter out;
	BufferedReader in;
	String hostname;
	int portnumber;
	
	public mpsClient(String host, int port){
		hostname = host;
		portnumber = port;
	}
	
	public boolean connect(){

		try {
			ssocket = new Socket(hostname, portnumber);
			out = new PrintWriter(ssocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(ssocket.getInputStream()));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean disconnect(){
		try {
			ssocket.close();
			out = null;
			in = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
