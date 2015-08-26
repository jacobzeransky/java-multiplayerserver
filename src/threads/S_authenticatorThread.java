package threads;

import java.util.concurrent.LinkedBlockingDeque;

import modules.authenticatorModule;
import modules.databaseModule;
import exceptions.*;
import objects.internalMsg;

public class S_authenticatorThread extends Thread{

	private final LinkedBlockingDeque<internalMsg> auth;
	private final LinkedBlockingDeque<String> events;
	//private final authenticatorModule authmod;
	private boolean cont = true;
	
	public S_authenticatorThread(LinkedBlockingDeque<internalMsg> auth_q, LinkedBlockingDeque<String> event_q){
		super("mp-AuthenticatorThread");
		auth = auth_q;
		events = event_q;
		//authmod = new authenticatorModule();
		events.offer("Authenticator Thread started");
	}
	
	public void run(){
		internalMsg imsg;
		boolean uresp;
		String[] userdata;
		while (true){
			try {
				imsg = auth.take();
				userdata = imsg.getContent().split(":");
				
				if (imsg.getType() == 2){ // login	
					try{
						databaseModule dbmod = new databaseModule();
						uresp = dbmod.loginUser(userdata[0], userdata[1]);
						dbmod.close();
					} catch (IncorrectPasswordException e){
						imsg.getClient().output().println("1");
						events.put("Failed login, incorrect password for: "+userdata[0]);
						continue;
					} catch (UserException e){
						imsg.getClient().output().println("2");
						events.put("Failed login, user: "+userdata[0]+" not found");
						continue;
					} 
					imsg.getClient().output().println("0");
					imsg.getClient().loginUser(userdata[0], uresp);
					events.put(userdata[0] + " logged in successfully"+(uresp ? " with admin privileges" : ""));
				}
				else{ // new user
					try{
						databaseModule dbmod = new databaseModule();
						uresp = dbmod.createNewUser(userdata[0], userdata[1]);
						dbmod.close();
					} catch (UserException e){
						imsg.getClient().output().println("1");
						events.put("Failed creation, user: "+userdata[0]+" already exists");
						continue;
					}
					
					if (uresp){
						events.put("User: "+userdata[0]+" successfully created");
						imsg.getClient().output().println("0");
					}
					else{
						events.put("Database error, user: "+userdata[0]+" not created");
						imsg.getClient().output().println("2");
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("take interupt");
			}
			if (!cont){
				break;
			}
		}
	}
	
	public void shutdown(){
		cont = false;
		events.offer("Authenticator Thread interrupted, shutting down");
	}
}
