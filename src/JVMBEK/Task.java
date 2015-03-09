package JVMBEK;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

//A class that encapsulates all data from one row of the "tasks" table
public class Task {
	public static final String[] PROGRESS_STRINGS = { "In Queue", "In Progress", "Finished" };
	
	public static TaskProgress StringToProgress(String str) {
		for(int i = 0; i < PROGRESS_STRINGS.length; i++)
			if(PROGRESS_STRINGS[i].equalsIgnoreCase(str))
				return TaskProgress.values()[i];
		return TaskProgress.IN_QUEUE;
	}
	
	private int _id;
	private String _name;
	private String _description;
	private String _comment;
	private TaskProgress _progress;
	private Date _start_date;
	private int _duration;
	ArrayList<User> _members;

	
	public Task(int id, String name, String description, TaskProgress progress, Date start, int dur) {
		_id = id;
		_name = name;
		_description = description;
		_progress = progress;
		//_comment = comment;
		_start_date = start;
		_duration = dur;		
		_members = new ArrayList<User>();
		

	}
	
	//Loads all members assigned to this project in the database
		private void loadMembers() {
			try
			{
				_members.clear();
				
				//Get members for each task
				Statement member_stmt = DB.getInstance().createStatement();
				ResultSet member_set = member_stmt.executeQuery( "SELECT id, fname, lname FROM users, tasks_members "
						+ "WHERE user_id == id AND task_id ==" + _id);
				
				while(member_set.next()) {

					User m = new User(member_set.getInt("id"), member_set.getString("fname"), member_set.getString("lname"), UserRole.MEMBER);
					
					_members.add(m);
				}
			}
			catch( Exception e) {
				e.printStackTrace();
			}
		}

	public int getId() { return _id; }
	public String getName() { return _name; }
	public String getDescription() { return _description; }
	public String getComment() { return _comment; }
	public TaskProgress getProgress() { return _progress; }
	public ArrayList<User> getAssignedMembers() { loadMembers(); return _members; }

}
