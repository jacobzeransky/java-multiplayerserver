package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingDeque;

import threads.C_delegatorThread;


public class mpsClient {
	private Socket ssocket;
	private PrintWriter out;
	private BufferedReader in = null;
	private String hostname;
	private int portnumber;
	private LinkedBlockingDeque<String> event_q;
	private LinkedBlockingDeque<String> chat_q;
	private LinkedBlockingDeque<Integer> response_q;
	private C_delegatorThread cdelg_t;
	//private LinkedBlockingDeque<String> chat_q;
	
	public mpsClient(String host, int port){
		hostname = host;
		portnumber = port;
		event_q = new LinkedBlockingDeque<String>();
		chat_q = new LinkedBlockingDeque<String>();
		response_q = new LinkedBlockingDeque<Integer>();
		
		
	}
	
	public boolean connect(){

		try {
			ssocket = new Socket(hostname, portnumber);
			out = new PrintWriter(ssocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(ssocket.getInputStream()));
			
			
			cdelg_t = new C_delegatorThread(in, event_q, chat_q, response_q);
			cdelg_t.start();
			event_q.offer("Connected");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			event_q.offer("Connection error: "+e.getMessage());
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			event_q.offer("Connection error: "+e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean disconnect(){
		try {
			out.println("005");
			cdelg_t.shutdown();
			ssocket.close();
			out = null;
			in = null;			
			event_q.offer("Disconnected");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			event_q.offer("Disconnection error: "+e.getMessage());
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

		event_q.offer("Sending credentials...");
		int resp;
		while (true){
			try{
		
				resp = response_q.take();
	
				if (resp == 0){
					event_q.offer("Login successful");
				}
				else{
					event_q.offer("Login unsuccessful");
				}
				return resp;
				
			} catch (InterruptedException e) {
				event_q.offer("Login error: "+e.getMessage());
				return 5;
			}
		}
		
	}
	
	public int createUser(String u, char[] p){
		String msg = "003" + u + ":";
		for (int i=0;i<p.length;i++){
			msg += p[i];
		}
		out.println(msg);
		event_q.offer("Sending new user information...");
		int resp;
		while (true){			
			try{
				resp = response_q.take();

				if (resp == 0){
					event_q.offer("User creation successful");
				}
				else{
					event_q.offer("User creation unsuccessful");
				}
				return resp;
				
			} catch (InterruptedException e) {
				event_q.offer("Creation error: "+e.getMessage());
				return 5;
			}
		}
	}
	
	public boolean chatMessage(String m){
		String msg = "006"+m;
		out.println(msg);
		event_q.offer("Sending chat message...");

		return true;
	}
	
	public String getEvent(){
		return event_q.poll();
	}
	
	public String getChat(){
		return chat_q.poll();
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
