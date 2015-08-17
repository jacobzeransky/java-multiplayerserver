package modules;

import java.util.ArrayList;

import exceptions.*;

public class authenticatorModule {
	private ArrayList<String> users;
	private ArrayList<String> passwords;
	
	public authenticatorModule(){
		users = new ArrayList<String>();
		passwords = new ArrayList<String>();
		users.add("detra");
		passwords.add("rawr");
	}
	
	public boolean authenticate(String ud, String pd) throws IncorrectPasswordException, UserException{
		for (int i=0; i<users.size(); i++){
			if (users.get(i).equals(ud)){
				if (passwords.get(i).equals(pd)){
					// TODO: admin privs 
					return true;
				}
				else{
					throw new IncorrectPasswordException();
				}
			}
		}
		throw new UserException();
	}
	
	public boolean createNewUser(String ud, String pd) throws UserException{
		for (String u: users){
			if (u.equals(ud)){
				throw new UserException();
			}
		}
		
		users.add(ud);
		passwords.add(pd);
		// TODo: database error, return false
		return true;
	}
}
