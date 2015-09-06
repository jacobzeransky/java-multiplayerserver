package modules;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import exceptions.DatabaseError;
import exceptions.IncorrectPasswordException;
import exceptions.MultipleLoginException;
import exceptions.UserException;

public class userModule extends databaseModule {
	
	public userModule(){
		super();
	}

	public boolean loginUser(String ud, String pd) throws IncorrectPasswordException, UserException, DatabaseError, MultipleLoginException{
		if (userExists(ud)){
			sql = "select password from user_profile where u_name = '"+ud+"'";
			ResultSet r = connection.executeSelect(sql);
			
			try {
				if (r.next()){
					boolean ret = false;
					System.out.println(r.getString(1) +" = "+pd);
					if (r.getString(1).equals(pd)){
						
						//TODO: admin stuff?
						
						sql = "select * from user_state where u_name = '"+ud+"' and status=1";
						r = connection.executeSelect(sql);
						
						try {
							if (r.next()){
								throw new MultipleLoginException();
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							connection.closeStmt();
							throw new DatabaseError("loginUser: "+e.getMessage());
						}
						
						sql = "update user_state set status=1, log_time = '"+(new Timestamp(jdate.getTime())).toString()+"' where u_name = '"+ud+"'";
						if (connection.executeStatement(sql) != 1){
							throw new DatabaseError("Login User, status not changed");
						}
						
						sql = "select admin from user_profile where u_name = '"+ud+"'";
						r = connection.executeSelect(sql);
						
						try {
							if (r.next()){
								ret = r.getBoolean(1);
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							connection.closeStmt();
							throw new DatabaseError("loginUser: "+e.getMessage());
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
				throw new DatabaseError("loginUser: "+e.getMessage());
			}
			throw new DatabaseError("End of function reached");
		}
		throw new UserException();
	}
	
	public boolean logoutUser(String ud) throws DatabaseError{
		sql = "update user_state set status=0, log_time = '"+(new Timestamp(jdate.getTime())).toString()+"' where u_name = '"+ud+"'";
		
		if (connection.executeStatement(sql) != 1){
			throw new DatabaseError("Logout User, status not changed");
		}
		
		return true;
	}
	
	public boolean createNewUser(String ud, String pd) throws UserException, DatabaseError{

		if (userExists(ud)){
			throw new UserException();
		}	
		
		sql = "insert into mpdb.user_state (u_name, log_time) values ('"+ud+"', '"+(new Timestamp(jdate.getTime())).toString()+"')";
		
		if (connection.executeStatement(sql) == 0){
			throw new DatabaseError("Creating user, state entry void");
		}
		sql = "insert into mpdb.user_profile (u_name, password, creation_date) values ('"+ud+"', '"+pd+"', '"+(new Timestamp(jdate.getTime())).toString()+"')";
		if (connection.executeStatement(sql) == 0){
			throw new DatabaseError("Creating user, profile entry void");
		}
		
		return true;
	}	
	
	private boolean userExists(String u) throws DatabaseError{
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
			throw new DatabaseError("userExists: "+e.getMessage());
		}
		return false;
	}
}
