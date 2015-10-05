package objects;

// formats messages received from client sockets for internal use
// passed to appropriate thread through communication queue

public class internalMsg {

	private int type;
	private Client client;
	private int gameid;
	private String content;
	
	public internalMsg(Client c, String m){
		type = Integer.parseInt(m.substring(0,3));
		client = c;
		if (type == 8){ // game move message
			String[] data = m.substring(3).split(":");
			gameid = Integer.parseInt(data[0]);
			content = data[1];
		}
		else{
			content = m.substring(3);
		}
	}
	
	public int getType(){
		return type;
	}
	
	public Client getClient(){
		return client;
	}
	
	public String getContent(){
		return content;
	}
	
	public int getGameid(){
		return gameid;
	}
}
