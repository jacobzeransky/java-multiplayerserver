package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;


public class connectionPool {

	// JDBC driver name and database URL
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	private static final String DB_URL = "jdbc:mysql://localhost:10333/mpdb";

	 //  Database credentials
	private static final String USER = "root";
	private static final String PASS = "r00tp455";
	
	private static Integer _maxNumbeOfConnections = 25;	
	private static connectionPool _instance = null;

	private Vector<Connection> _connectionPool = null;

	private static int cID = 0;
	
	private connectionPool() 
	{		
		_connectionPool = new Vector<Connection>(_maxNumbeOfConnections);
		
		for ( int i=0; i<_maxNumbeOfConnections; i++ )
		{
			_connectionPool.add(getDefaultConnection());
		}
	}

	public static synchronized connectionPool getInstance()
	{
		if ( _instance == null )
		{			
			synchronized (connectionPool.class) 
			{
				if (_instance == null) {
					_instance = new connectionPool();
				}
			}
		}

		return _instance;
	}

	public Connection getConnection ()
	{		
		return _connectionPool.get(cID++);
	}

	public boolean returnConnection (Connection connection)
	{
		cID-=1;

		_connectionPool.set(cID, connection);
		return true;
	}
	
	public boolean shutDown ()
	{			
		for ( int i = _maxNumbeOfConnections-1; i >=0; i-- )
		{
			try {
				_connectionPool.get(i).close();
				_connectionPool.remove(i);
			} catch (SQLException e) {
				e.printStackTrace(); 
				return false;
			}
		}
		
		_connectionPool = null;
		_instance = null;
		
		return true;
	}	

	private Connection getDefaultConnection ()
	{	
		Connection conn = null;	    
	    try {
	    	Class.forName(JDBC_DRIVER).newInstance();
	    	conn = DriverManager.getConnection(DB_URL,USER,PASS);
	    	conn.setAutoCommit(true);
	    } catch (SQLException e) {
	    	e.printStackTrace();     
	    } catch (InstantiationException e) {
	    	e.printStackTrace();     
	    } catch (IllegalAccessException e) {
	    	e.printStackTrace();     
	    } catch (ClassNotFoundException e) {
	    	e.printStackTrace();    
	    }

	    return conn;
	}
	
	/* Static methods 
	
	public static Connection getConnection()
	{	
		Connection conn = null;	    
	    try {
	    	Class.forName(_jdbcDriver).newInstance();
	    	conn = DriverManager.getConnection(_dbURL,_username,_password);
	    	conn.setAutoCommit(true);
	    } catch (SQLException e) {
	    	e.printStackTrace();     
	    } catch (InstantiationException e) {
	    	e.printStackTrace();      
	    } catch (IllegalAccessException e) {
	    	e.printStackTrace();      
	    } catch (ClassNotFoundException e) {
	    	e.printStackTrace();      
	    }

	    return conn;
	}*/
	
	public static boolean closeConnection (Connection connection)
	{
		try
		{
			connection.close();
			connection = null;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();  
		}
		
		return false;
	}
}
