package threads;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import objects.Client;
import objects.internalMsg;

public class S_delegatorThread extends Thread{

	private final ArrayList<Client> clients;
	private final LinkedBlockingQueue<Client> clients_toadd;
	private final LinkedBlockingDeque<internalMsg> auth;
	private final LinkedBlockingDeque<Client> lfg;
	private final LinkedBlockingDeque<String> events;
	private final LinkedBlockingDeque<internalMsg> adminq;
	private boolean cont = true;
	
	// chat broadcast related
	private final LinkedBlockingDeque<String> fromchatthread;
	private final LinkedBlockingDeque<internalMsg> tochatthread;
	
	public S_delegatorThread(ArrayList<Client> client_l, LinkedBlockingDeque<internalMsg> itochatthread, LinkedBlockingDeque<String> ifromchatthread, LinkedBlockingQueue<Client> client_toadd_q, LinkedBlockingDeque<internalMsg> auth_q, LinkedBlockingDeque<Client> lobby_q/*, HashMap<Integer, >gametheads*/, LinkedBlockingDeque<String> event_q, LinkedBlockingDeque<internalMsg> admin_q){
		super("mp-DelegatorThread");
		clients = client_l;
		clients_toadd = client_toadd_q;
		auth = auth_q;
		lfg = lobby_q;
		events = event_q;
		adminq = admin_q;
		fromchatthread = ifromchatthread;
		tochatthread = itochatthread;
		events.offer("Delegator Thread started");
	}
	
	public void run(){
		String msg;
		internalMsg imsg;
		ArrayList<Client> toRemove = new ArrayList<Client>();
		
		// chat related broadcast structures
		ArrayList<String> chatmsgs = new ArrayList<String>();
		
		while (true){
			
			chatmsgs.clear();
			// chat related
			while (fromchatthread.size() > 0){
				chatmsgs.add(fromchatthread.poll());
			}
			
			for (Client cl : clients){
				
				//
				// concurrency issue, putting chat broadcast within this loop for the moment
				//
				
				for (String cm : chatmsgs){
					events.offer("Sending chat: "+cm+" to "+cl.getName());
					cl.output().println("9"+cm);
				}
				
				// end broadcast section
				
				try {
					if (!cl.input().ready()){
						continue;
					}
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
						/*	if (cl.isLoggedIn()){
								events.offer("Error: "+cl.getName() +" is attempting to login again");
							}
							else{*/
								auth.offer(imsg);
								events.offer(cl.getName() +" is attempting to login");
							
							break;
						case 3:		// new user
							auth.offer(imsg);
							events.offer(cl.getName() +" is attempting to create a new user");
							break;
						case 4:		// admin command
							adminq.offer(imsg);
							break;
						case 5:		// disconnect
							auth.offer(imsg);
							toRemove.add(cl);
							events.offer(cl.getName() +" is disconnecting");
							break;
						case 6:		//chat
							
							tochatthread.offer(imsg);
							events.offer(cl.getName() +" sent chat");
							break;
						}
						
					}
				} catch (IOException e) {
					events.offer(cl.getName() + " IO error: "+ e.toString());
					auth.offer(new internalMsg(cl, "005"));
					toRemove.add(cl);
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
				//toRemove = new ArrayList<Client>();
				toRemove.clear();
			}
			
			if (clients_toadd.size() != 0){
				clients.addAll(clients_toadd);
				clients_toadd.clear();
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
