package connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class sqlConnection {
	
	private Connection connection = null;
	private Statement stmt = null;
	
	public sqlConnection ()
	{
		connection = connectionPool.getInstance().getConnection();	
	}

	public ResultSet executeSelect (String sql) {
		try {
			stmt = connection.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	public void closeStmt(){
		try {
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int executeStatement (String sql)
	{
		try {
			stmt = connection.createStatement();
			int result = stmt.executeUpdate(sql);
			stmt.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();    
		} 
		
		return 0;
	}
	
	public int executeUpdate (String sql)
	{
		try {
			stmt = connection.createStatement();
			int rowsEffected = stmt.executeUpdate(sql);
		//	connection.commit();
			stmt.close();
			return rowsEffected;
		} catch (SQLException e) {
			e.printStackTrace();
			return e.getErrorCode();
		} 
	}

	public boolean returnConnection() {
		return (connectionPool.getInstance().returnConnection(this.connection));
	}


	/*
	public boolean lockTable (String tableName) {

		String sql = "lock tables " + tableName + " write"; // in exclusive mode";

		try {
			Statement stmt = connection.createStatement();
			stmt.execute(sql);
			return true;
		} catch (SQLException e) {
			ExceptionHandler.display (this,"lockTable",e);       
	    }
		return false;
	}
	
	public boolean releaseLocks () {
		
		String release = "unlock tables";
		try {
			Statement stmt = connection.createStatement();
			stmt.execute(release);
			return true;
		} catch (SQLException e) {
			ExceptionHandler.display (this,"releaseLocks",e);       
	    }
		return false;
	}
*/
}
