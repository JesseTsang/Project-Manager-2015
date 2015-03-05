package JVMBEK;

//Class that encapsulates all date from a row in the "users" table
public class User {

	private int _id;
	private String _fname;
	private String _lname;
	private UserRole _role; 
	
	public User(int id, String fname, String lname, UserRole role)
	{
		_id = id;
		_fname = fname;
		_lname = lname;
		_role = role;
	}
	
	public int getId() { return _id; }
	
	public UserRole getRole() {
		return _role;
	}
	
	public String getUserName() {
		return _fname + " " + _lname;
	}
}
