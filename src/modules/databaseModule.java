package modules;

import connection.sqlConnection;

// base class for gameModule and userModule
// allows connection to backend database through sqlConnection

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
}
