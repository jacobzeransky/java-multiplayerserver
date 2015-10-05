package threads;

import java.util.concurrent.LinkedBlockingDeque;

import objects.internalMsg;

// SERVER-SIDE
// handles formatting chat messages from users
// could be expanded to log all messages

public class S_chatThread extends Thread{

	private final LinkedBlockingDeque<internalMsg> inc;
	private final LinkedBlockingDeque<String> outc;
	private final LinkedBlockingDeque<String> events;
	private boolean cont = true;
	
	public S_chatThread(LinkedBlockingDeque<internalMsg> inchat_q, LinkedBlockingDeque<String> outchat_q, LinkedBlockingDeque<String> event_q){
		super("mp-ChatThread");
		inc = inchat_q;
		outc = outchat_q;
		events = event_q;
		events.offer("Chat Thread started");
	}
	
	public void run(){
		internalMsg imsg;
		String chatmsg;
		while (true){
			try {
				imsg = inc.take();
				if (imsg.getType() != 6){
					events.put("Chat msg error: "+imsg.getContent());
				}
				chatmsg = imsg.getClient().getName() + ": "+imsg.getContent();
				
				// log chat msgs?
				
				outc.offer(chatmsg);
				
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
		events.offer("Chat Thread interrupted, shutting down");
	}
}
