package threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import objects.Client;

public class S_connectorThread extends Thread{

	private final int portNumber;
	private final ArrayList<Client> clients;
	private final LinkedBlockingDeque<String> events;
	private boolean cont = true;
	
	public S_connectorThread(int port, ArrayList<Client> client_l, LinkedBlockingDeque<String> event_q){
		super("mp-ConnectorThread");
		portNumber = port;
		clients = client_l;
		events = event_q;
		events.offer("Connector Thread started");
	}
	
	public void run(){
		Socket cls;
		try (ServerSocket serverSocket = new ServerSocket(portNumber)){
			serverSocket.setSoTimeout(500);
			System.out.println("addr: "+serverSocket.getInetAddress());
			while (true){
				
				try{
					cls = serverSocket.accept();
				} catch (SocketTimeoutException e) {
					if (cont){
						System.out.println("Timing out, continue");
						continue;
					}
					else{
						break;
					}
				}
				
				clients.add(new Client(cls));
				events.offer(cls.getInetAddress().toString()+" connected");
				if (!cont){
					break;
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			System.out.println(events.offer("Connector IO error: "+e.getMessage()));
		}
	}
	
	public void shutdown(){
		//Thread.currentThread().interrupt();
		cont = false;
		events.offer("Connector Thread interrupted, shutting down");
	}
}
