package threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;

public class C_delegatorThread extends Thread{

	private final BufferedReader in;
	private final LinkedBlockingDeque<String> events;
	private final LinkedBlockingDeque<String> chats;
	private final LinkedBlockingDeque<Integer> responses;
	private boolean cont = true;
	
	public C_delegatorThread(BufferedReader input_c, LinkedBlockingDeque<String> event_q, LinkedBlockingDeque<String> chat_q, LinkedBlockingDeque<Integer> resp_q){
		super("mp-Cient-DelegatorThread");
		in = input_c;
		events = event_q;
		chats = chat_q;
		responses = resp_q;
		events.offer("Client Delegator Thread started");
	}
	
	public void run(){
		String msg;
		while (true){

			try {

				msg = in.readLine();
				if (msg != null){
					events.offer("new msg: "+msg);
					if (msg.charAt(0) == '9'){ // chat msg
						chats.offer(msg.substring(1));
					}
					else{
						responses.offer(Integer.parseInt(msg));	
					}
				}
			} catch (IOException e) {
				if (cont){
					events.offer("Client IO error: "+ e.toString());
				}
			}
			
			if (!cont){
				break;
			}
		}
	}
	
	public void shutdown(){
		cont = false;
		events.offer("Client Delegator Thread interrupted, shutting down");
	}
}

