package JVMBEK;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//A class that encapsulates all data from one row of the "tasks" table
public class Task {
	public static final String[] PROGRESS_STRINGS = { "In Queue",
			"In Progress", "Finished" };

	public static TaskProgress StringToProgress(String str) {
		for (int i = 0; i < PROGRESS_STRINGS.length; i++)
			if (PROGRESS_STRINGS[i].equalsIgnoreCase(str))
				return TaskProgress.values()[i];
		return TaskProgress.IN_QUEUE;
	}

	private int _id;
	private String _name;
	private int _duration;
	private String _description;
	private String _comment;
	private TaskProgress _progress;
//	private Date _start_date;
//	private Date _end_date;
	ArrayList<User> _members;
	
	//String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
	
//	Date date = ...; // wherever you get this from
//	 
//	Calendar cal = Calendar.getInstance();
//	cal.setTime(date);
//	cal.add(Calendar.DATE, 10); // add 10 days
//	 
//	date = cal.getTime();

	public Task(int id, String name, String description, TaskProgress progress, int dur) {
		_id = id;
		_name = name;
		_description = description;
		_progress = progress;
		// _comment = comment;
		_duration = dur;
	}

	public Project getProject() {
		Project p = null;

		try {
			Statement project_stmt = DB.getInstance().createStatement();
			ResultSet project_set = project_stmt
					.executeQuery("SELECT * FROM projects, project_tasks "
							+ "WHERE project_id == id AND task_id ==" + _id);

			while (project_set.next()) {

				p = new Project(project_set.getInt("id"),
						project_set.getString("name"),
						project_set.getString("description"),
						project_set.getDate("date_created"),
						project_set.getDate("start_date")
						);
				

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return p;
	}

	// Loads all members assigned to this project in the database
	private void loadMembers() {
		try {
			_members.clear();

			// Get members for each task
			Statement member_stmt = DB.getInstance().createStatement();
			ResultSet member_set = member_stmt
					.executeQuery("SELECT id, fname, lname FROM users, tasks_members "
							+ "WHERE user_id == id AND task_id ==" + _id);

			while (member_set.next()) {

				User m = new User(member_set.getInt("id"),
						member_set.getString("fname"),
						member_set.getString("lname"), UserRole.MEMBER);

				_members.add(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public String getDescription() {
		return _description;
	}

	public String getComment() {
		return _comment;
	}

	public TaskProgress getProgress() {
		return _progress;
	}
	
//	public Date getStartDate(){
//		return _start_date;
//	}
//	
//	public Date getEndDate(){
//		return _end_date;
//	}
		

	public ArrayList<User> getAssignedMembers() {
		loadMembers();
		return _members;
	}
	
//	public Date addDaysToDate(int duration){
//		Date end_date;	
//	
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(this._start_date);
//		cal.add(Calendar.DATE, duration);//add duration to the saved date
//		 
//		end_date = cal.getTime();
//		return end_date;
//	}
	
	public String dateToString(){
		String dateString = new SimpleDateFormat("dd-MM-yyyy").format(this);
		return dateString;
	}

}
