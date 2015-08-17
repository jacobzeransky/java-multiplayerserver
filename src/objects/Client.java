package objects;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;



public class Client {
	
	private String user;
	private PrintWriter sockout;
	private BufferedReader sockin;
	private boolean admin = false;
	
	public Client(Socket s) throws IOException{
		user = "TEMP: "+s.getInetAddress().toString();
		sockout = new PrintWriter(s.getOutputStream(), true); 
		sockin = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}
	
	public String getName(){
		return user;
	}
	
	public void loginUser(String u, boolean a){
		user = u;
		admin = a;
	}
	
	public PrintWriter output(){
		return sockout;
	}
	
	public BufferedReader input(){
		return sockin;
	}
	
	public boolean admin(){
		return admin;
	}

}
