package modules;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import exceptions.IncorrectPasswordException;
import exceptions.UserException;

public class userModule extends databaseModule {
	
	public userModule(){
		super();
	}

	public boolean loginUser(String ud, String pd) throws IncorrectPasswordException, UserException{
		if (userExists(ud)){
			sql = "select password from user_profile where u_name = '"+ud+"'";
			ResultSet r = connection.executeSelect(sql);
			
			try {
				if (r.next()){
					boolean ret = true;
					System.out.println(r.getString(1) +" = "+pd);
					if (r.getString(1).equals(pd)){
						
						//TODO: admin stuff?
						
						sql = "update user_state set status=1, log_time = '"+(new Timestamp(jdate.getTime())).toString()+"' where u_name = '"+ud+"'";
						if (connection.executeStatement(sql) != 1){
							System.out.println("error loging in user");
							return false;
						}
					}
					else{
						throw new IncorrectPasswordException();
					}
					connection.closeStmt();
					return ret;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				connection.closeStmt();
				e.printStackTrace();
				return false;
			}
			return false;
		}
		throw new UserException();
	}
	
	public boolean logoutUser(String ud){
		sql = "update user_state set status=0, log_time = '"+(new Timestamp(jdate.getTime())).toString()+"' where u_name = '"+ud+"'";
		
		if (connection.executeStatement(sql) != 1){
			System.out.println("error loging user");
			return false;
		}
		
		return true;
	}
	
	public boolean createNewUser(String ud, String pd) throws UserException{

		if (userExists(ud)){
			throw new UserException();
		}	
		
		sql = "insert into mpdb.user_state (u_name, log_time) values ('"+ud+"', '"+(new Timestamp(jdate.getTime())).toString()+"')";
		
		if (connection.executeStatement(sql) == 0){
			System.out.println("error adding user");
			return false;
		}
		sql = "insert into mpdb.user_profile (u_name, password, creation_date) values ('"+ud+"', '"+pd+"', '"+(new Timestamp(jdate.getTime())).toString()+"')";
		if (connection.executeStatement(sql) == 0){
			System.out.println("error adding user");
			return false;
		}
		// TODo: database error, return false
		return true;
	}	
	
	private boolean userExists(String u) {
		sql = "select * from user_state where u_name = '"+u+"'";
		ResultSet r = connection.executeSelect(sql);
		
		try {
			if (r.next()){
				connection.closeStmt();
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			connection.closeStmt();
			e.printStackTrace();
		}
		return false;
	}
}
