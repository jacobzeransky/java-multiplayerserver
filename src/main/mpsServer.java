package main;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import connection.connectionPool;
import objects.Client;
import objects.internalMsg;
import threads.S_authenticatorThread;
import threads.S_chatThread;
import threads.S_connectorThread;
import threads.S_delegatorThread;

public class mpsServer {
	public static void main(String[] args) throws InterruptedException{
		int port = 10355;
		
		ArrayList<Client> client_l = new ArrayList<Client>();
		LinkedBlockingQueue<Client> client_toadd_q = new LinkedBlockingQueue<Client>();
		LinkedBlockingDeque<internalMsg> auth_q = new LinkedBlockingDeque<internalMsg>();
		LinkedBlockingDeque<Client> lobby_q = new LinkedBlockingDeque<Client>();
		LinkedBlockingDeque<String> event_q = new LinkedBlockingDeque<String>();
		LinkedBlockingDeque<internalMsg> admin_q = new LinkedBlockingDeque<internalMsg>();
		LinkedBlockingDeque<internalMsg> inchat_q = new LinkedBlockingDeque<internalMsg>();
		LinkedBlockingDeque<String> outchat_q = new LinkedBlockingDeque<String>();
		
		S_connectorThread conn_t = new S_connectorThread(port, client_toadd_q, event_q);
		S_authenticatorThread auth_t = new S_authenticatorThread(auth_q, event_q);
		S_delegatorThread delg_t = new S_delegatorThread(client_l, inchat_q, outchat_q, client_toadd_q, auth_q, lobby_q, event_q, admin_q);
		S_chatThread chat_t = new S_chatThread(inchat_q, outchat_q, event_q);
		
		conn_t.start();
		auth_t.start();
		delg_t.start();
		chat_t.start();
		
		String eqmsg;
		while (true){
			eqmsg = event_q.take();
			System.out.println(eqmsg);
			if (eqmsg.equals("- - - SHUTDOWN - - -")){
				break;
			}
		}
		
		
		System.out.println("shtting down");
		conn_t.shutdown();
		auth_t.shutdown();
		delg_t.shutdown();
		chat_t.shutdown();
		
		connectionPool.getInstance().shutDown();

		/*
		try{
		for (int i=0;i<4;i++){
			
			System.out.println("take 1");
			while (event_q.peek() == null){
				//System.out.println(event_q.poll());
			}
			while (event_q.peek() != null){
				System.out.println("item: "+event_q.take());
			}
			conn_t.shutdown();
			auth_t.shutdown();
		}
		} catch (Exception e){
			e.printStackTrace();
			System.out.println(event_q.size());
		}
		*/
		
	}
}
