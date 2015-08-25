package threads;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import objects.*;

public class S_delegatorThread extends Thread{

	private final ArrayList<Client> clients;
	private final LinkedBlockingDeque<internalMsg> auth;
	private final LinkedBlockingDeque<Client> lfg;
	private final LinkedBlockingDeque<String> events;
	private final LinkedBlockingDeque<internalMsg> adminq;
	private boolean cont = true;
	
	public S_delegatorThread(ArrayList<Client> client_l, LinkedBlockingDeque<internalMsg> auth_q, LinkedBlockingDeque<Client> lobby_q/*, HashMap<Integer, >gametheads*/, LinkedBlockingDeque<String> event_q, LinkedBlockingDeque<internalMsg> admin_q){
		super("mp-DelegatorThread");
		clients = client_l;
		auth = auth_q;
		lfg = lobby_q;
		events = event_q;
		adminq = admin_q;
		events.offer("Delegator Thread started");
	}
	
	public void run(){
		String msg;
		internalMsg imsg;
		ArrayList<Client> toRemove = new ArrayList<Client>();
		while (true){
			for (Client cl : clients){
				
				try {
					msg = cl.input().readLine();
					events.offer("new msg: "+msg);
					if (msg != null){
						
						imsg = new internalMsg(cl, msg);
						System.out.println("error type: "+imsg.getType());
						
						switch (imsg.getType()){
						case 0:		//join lobby
							lfg.offer(cl);
							events.offer(cl.getName() +" joined lobby");
							break;
						case 1:		// perform game move
							
							break;
						case 2:		// login/authenticate
							if (cl.getName().charAt(4) == ':'){
								events.offer("Error: "+cl.getName() +" is attempting to login again");
							}
							else{
								auth.offer(imsg);
								events.offer(cl.getName() +" is attempting to login");
							}
							break;
						case 3:		// new user
							auth.offer(imsg);
							events.offer(cl.getName() +" is attempting to create a new user");
							break;
						case 4:		// admin command
							adminq.offer(imsg);
							break;
						case 5:		// disconnect
							toRemove.add(cl);
							events.offer(cl.getName() +" is disconnecting");
							break;
						}
						
					}
				} catch (IOException e) {
					events.offer(cl.getName() + " IO error: "+ e.toString());
				}
				
				if (!cont){
					break;
				}
			}
			
			if (toRemove.size() != 0){
				for (Client cl : toRemove){
					try {
						cl.disconnectClient();
					} catch (IOException e) {
						events.offer("Error disconnecting "+cl.getName());
						e.printStackTrace();
					}
				}
				clients.removeAll(toRemove);
				toRemove = new ArrayList<Client>();
			}
			
			if (!cont){
				break;
			}
		}
	}
	
	public void shutdown(){
		cont = false;
		events.offer("Delegator Thread interrupted, shutting down");
	}
}
