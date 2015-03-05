package JVMBEK;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//DB is a singleton class used to abstract connections to
//the SQLite database library.
public class DB {
	//Singleton members
	private static DB _instance;
	
	//Database connection members
	private final static String DATABASE_FILE = "jdbc:sqlite:jvmbek";
	private Connection _connection;
	
	private DB()
	{
		_connection = null;
	    try 
	    {
	      _connection = DriverManager.getConnection(DATABASE_FILE);
	    } 
	    catch ( Exception e ) 
	    {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    
	    System.out.println("Opened database successfully");
	}

	public static DB getInstance() 
	{
		if (_instance == null) 
			_instance = new DB();
		
		return _instance;
	}
	
	public Connection getConnection() { return _connection; }
	
	public Statement createStatement() throws SQLException { 
		return _connection.createStatement(); 
	}
	
	//Closes all connections to the database
	public void shutdown() {
		try {
			_connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
