package objects;

public class internalMsg {

	private int type;
	private Client client;
	private String content;
	
	public internalMsg(Client c, String m){
		type = Integer.parseInt(m.substring(0,3));
		client = c;
		content = m.substring(3);
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
}
