package modules;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import connection.connectionPool;
import connection.sqlConnection;
import exceptions.IncorrectPasswordException;
import exceptions.UserException;


public class databaseModule {

	private sqlConnection connection = null;
	String sql = null;
	
	public databaseModule(){
		connection = new sqlConnection();
	}
	
	public void close(){
		if ( connection != null ) {
			 connection.returnConnection();
			 connection = null;
		}
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
	
	public boolean createNewUser(String ud, String pd) throws UserException{

		if (userExists(ud)){
			throw new UserException();
		}
		
		java.util.Date jdate = new java.util.Date();
		
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
	
	public static void main(String args[]){
		databaseModule dbmod = new databaseModule();
		
		try{
		//	System.out.println(dbmod.createNewUser("joebob", "password"));
			try {
				System.out.println(dbmod.loginUser("joebob", "password"));
			} catch (IncorrectPasswordException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UserException e){
			System.out.println("user already exists/user not found");
		}
		dbmod.close();
		connectionPool.getInstance().shutDown();
	}
	/*
	public boolean returnConnection ()
	{
		if ( connection != null ) {
			 connection.returnConnection();
			 connection = null;
		}
		return true;
	}
	
	// JDBC driver name and database URL
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost/mpdb";

	   //  Database credentials
	   static final String USER = "root";
	   static final String PASS = "";
	   
	   public static void main(String[] args) {
	   Connection conn = null;
	   Statement stmt = null;
	   try{
	      //STEP 2: Register JDBC driver
	      Class.forName("com.mysql.jdbc.Driver");

	      //STEP 3: Open a connection
	      System.out.println("Connecting to database...");
	      conn = DriverManager.getConnection(DB_URL,USER,PASS);

	      //STEP 4: Execute a query
	      System.out.println("Creating statement...");
	      stmt = conn.createStatement();
	      String sql;
	     // sql = "INSERT INTO mpdb.user_state (us_name, status, log_time) VALUES ('detra', 0, '2015-08-23 18:45:00')";
	     // System.out.println("adding: "+stmt.execute(sql));
	      sql = "select * from user_state";
	      ResultSet rs = stmt.executeQuery(sql);

	      //STEP 5: Extract data from result set
	      while(rs.next()){
	         //Retrieve by column name
	         String name  = rs.getString("us_name");
	         int status = rs.getInt("status");
	         Timestamp oftime = rs.getTimestamp("log_time");

	         //Display values
	         System.out.print("Name: " + name);
	         System.out.print(", Status: " + status);
	         System.out.println(", on/off time: " + oftime);
	      }
	      //STEP 6: Clean-up environment
	      rs.close();
	      stmt.close();
	      conn.close();
	   }catch(SQLException se){
	      //Handle errors for JDBC
	      se.printStackTrace();
	   }catch(Exception e){
	      //Handle errors for Class.forName
	      e.printStackTrace();
	   }finally{
	      //finally block used to close resources
	      try{
	         if(stmt!=null)
	            stmt.close();
	      }catch(SQLException se2){
	      }// nothing we can do
	      try{
	         if(conn!=null)
	            conn.close();
	      }catch(SQLException se){
	         se.printStackTrace();
	      }//end finally try
	   }//end try
	   System.out.println("Goodbye!");
	}//end main
	*/
	
}
