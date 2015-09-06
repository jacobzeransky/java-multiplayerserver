package threads;

import java.util.concurrent.LinkedBlockingDeque;

import exceptions.DatabaseError;
import exceptions.IncorrectPasswordException;
import exceptions.MultipleLoginException;
import exceptions.UserException;
import modules.userModule;
import objects.internalMsg;

public class S_authenticatorThread extends Thread{

	private final LinkedBlockingDeque<internalMsg> auth;
	private final LinkedBlockingDeque<String> events;
	private boolean cont = true;
	
	public S_authenticatorThread(LinkedBlockingDeque<internalMsg> auth_q, LinkedBlockingDeque<String> event_q){
		super("mp-AuthenticatorThread");
		auth = auth_q;
		events = event_q;
		events.offer("Authenticator Thread started");
	}
	
	public void run(){
		internalMsg imsg;
		boolean uresp = false;
		String[] userdata;
		while (true){
			try {
				imsg = auth.take();
				userdata = imsg.getContent().split(":");
				
				if (imsg.getType() == 2){ // login	
					try{
						userModule umod = new userModule();
						uresp = umod.loginUser(userdata[0], userdata[1]);
						umod.close();
					} catch (IncorrectPasswordException e){
						imsg.getClient().output().println("1");
						events.put("Failed login, incorrect password for: "+userdata[0]);
						continue;
					} catch (UserException e){
						imsg.getClient().output().println("2");
						events.put("Failed login, user: "+userdata[0]+" not found");
						continue;
					} catch (DatabaseError e) {
						imsg.getClient().output().println("3");
						events.put("Failed login, database error for: "+userdata[0]+"\n"+e.getMessage());		
						continue;
					} catch (MultipleLoginException e){
						imsg.getClient().output().println("4");
						events.put("Failed login, user: "+userdata[0]+" already logged in");		
						continue;
					}
					imsg.getClient().output().println("0");
					imsg.getClient().loginUser(userdata[0], uresp);
					events.put(userdata[0] + " logged in successfully"+(uresp ? " with admin privileges" : ""));
				}
				else if (imsg.getType() == 3){ // new user
					try{
						userModule umod = new userModule();
						uresp = umod.createNewUser(userdata[0], userdata[1]);
						umod.close();
					} catch (UserException e){
						imsg.getClient().output().println("1");
						events.put("Failed creation, user: "+userdata[0]+" already exists");
						continue;
					} catch (DatabaseError e) {
						events.put("Failed creation, user: "+userdata[0]+" due to database error\n"+e.getMessage());
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
				else{ // disconnect
					if (imsg.getClient().isLoggedIn()){
						try{
							userModule umod = new userModule();
							uresp = umod.logoutUser(imsg.getClient().getName());
							umod.close();
						} catch (DatabaseError e) {
							events.put("Failed logout, user: "+imsg.getClient().getName()+" due to database error\n"+e.getMessage());
							continue;
						}
					}
					
					events.put("User: "+imsg.getClient().getName()+" disconnected successfully");
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
