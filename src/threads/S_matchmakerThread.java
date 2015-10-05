package threads;

import java.util.EnumMap;
import java.util.concurrent.LinkedBlockingDeque;

import objects.Client;
import objects.Game;

// SERVER-SIDE
// handles matchmaking between clients in lobby (lfg)

public class S_matchmakerThread extends Thread{

	private final EnumMap<Game, LinkedBlockingDeque<Client>> lfg;
	private final LinkedBlockingDeque<String> events;
	private boolean cont = true;
	
	public S_matchmakerThread(EnumMap<Game, LinkedBlockingDeque<Client>> lobbyq_m, LinkedBlockingDeque<String> event_q){
		super("mp-MatchmakerThread");
		lfg = lobbyq_m;
		events = event_q;
		
		events.offer("Matchmaker Thread started");
	}
	
	public void run(){
		LinkedBlockingDeque<Client> lobby;
		Client p1, p2;
		while (true){
			for (Game g : Game.values()){
				lobby = lfg.get(g);
				
				if (lobby.size() > 1){
					p1 = lobby.poll();
					p2 = lobby.poll();
					
					// gamethread structures, map with keys = gameid?
				}
				
				if (!cont){
					break;
				}
			}
			
			if (!cont){
				break;
			}
		}
	}
	
	public void shutdown(){
		cont = false;
		events.offer("Matchmaker Thread interrupted, shutting down");
	}
}
