package modules;

import connection.sqlConnection;


public abstract class databaseModule {

	protected sqlConnection connection = null;
	protected String sql = null;
	protected java.util.Date jdate = new java.util.Date();
	
	protected databaseModule(){
		connection = new sqlConnection();
	}
	
	public void close(){
		if ( connection != null ) {
			 connection.returnConnection();
			 connection = null;
		}
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
