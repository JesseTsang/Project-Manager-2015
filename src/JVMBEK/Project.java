package JVMBEK;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

//A class that encapsulates all data in a row of the "projects" table 
public class Project {
	private int _id;
	private String _name;
	private String _description;
	private Date _created_date;
	private Date _start_date;
	private ArrayList<Task> _tasks;
	static Date startDatefromDB;
	static Date endDatefromDB;
	
	// For member use, when selecting a task to view information for
	public static int selectedTaskId;

	public Project(int id, String name, String descr, Date created, Date start) {
		_id = id;
		_name = name;
		_description = descr;
		_created_date = created;
		_start_date = start;
		_tasks = new ArrayList<Task>();

		populateTasks();
	}

	// Loads all tasks assigned to this project in the database
	private void populateTasks() {
		try {
			// Get tasks for each project
			Statement task_stmt = DB.getInstance().createStatement();
			ResultSet task_set = task_stmt
					.executeQuery("SELECT * FROM tasks, project_tasks "
							+ "WHERE project_tasks.project_id == "
							+ _id
							+ " AND tasks.id == project_tasks.task_id;");

			while (task_set.next()) {
				TaskProgress prog = Task.StringToProgress(task_set
						.getString("progress"));
				
				startDatefromDB = task_set.getDate("task_start_date");
				endDatefromDB   = task_set.getDate("task_end_date");
			
				Task t = new Task(task_set.getInt("id"),
						task_set.getString("name"),
						task_set.getString("description"), 
						prog,
						//task_set.getInt("duration"),
						startDatefromDB,
						endDatefromDB,
						task_set.getInt("optimistic"),
						task_set.getInt("pessimistic"),
						task_set.getInt("estimate"),
						task_set.getInt("variance")
						);

				_tasks.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Members only to get the tasks they have been assigned to.
	// Used in MemberViewScree,java

	public static ArrayList<Task> getAssignedTasks(int user_id) {

		ArrayList<Task> assignedTasks = new ArrayList();

		try {

			// Get tasks for each member
			Statement member_stmt = DB.getInstance().createStatement();
			ResultSet task_set = member_stmt
					.executeQuery("SELECT * FROM tasks_members, tasks "
								+ "WHERE user_id == " + user_id
								+ " AND task_id = id");

			while (task_set.next()) {

				TaskProgress prog = Task.StringToProgress(task_set.getString("progress"));

				// TODO: get dates such as task_set.getDate("start_date"),
				Task t = new Task(task_set.getInt("id"),
								  task_set.getString("name"),
								  task_set.getString("description"), 
								  prog,						
								  task_set.getInt("duration"),
								  //startDatefromDB,
								  //endDatefromDB,
								  task_set.getInt("optimistic"),
								  task_set.getInt("pessimistic"),
								  task_set.getInt("estimate"),
								  task_set.getInt("variance")
						);

				assignedTasks.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return assignedTasks;
	}

	public Task getTaskById(int task_id){
		Task task = null;
		
		for (Task t : _tasks)
			if (t.getId() == task_id)
				task = t;		
		
		return task;
	}

	public int getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getDescription() {
		return _description;
	}
	
	public Date getStartDate(){
		return _start_date;
	}
	
	public Date getCreatedDate(){
		return _created_date;
	}
	
	public void setDescription(String desc) {
		
		_description = desc;
	}
	
	public ArrayList<Task> getTasks() {
		return _tasks;
	}

	@Override
	public String toString() {
		return _name;
	}
		
	public String getStartDateString(){
		String dateString = new SimpleDateFormat("dd-MM-yyyy").format(_start_date);
		return dateString;
	}
	
	public String getDateString(Date date)
	{
		String dateString = new SimpleDateFormat("dd-MM-yyyy").format(date);
		return dateString;
	}
	
	// **********************************************************************
	// TASK DATABASE FUNCTIONS
	// **********************************************************************
	public void deleteTask(int taskId) {
		Statement stmt = null;
		try {
			stmt = DB.getInstance().createStatement();
			String sql = "DELETE FROM tasks WHERE id = " + taskId + ";";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM project_tasks WHERE project_id = " + _id
					+ " AND task_id = " + taskId + ";";
			stmt.executeUpdate(sql);

			Task task = null;
			for (Task t : _tasks)
				if (t.getId() == taskId)
					task = t;
			_tasks.remove(task);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	//public void addTask(String name, String description, int duration, int optimistic, int pessimistic, double estimate, double variance) {
	public void addTask(String name, String description, Date taskStartDate, Date taskEndDate, int optimistic, int pessimistic, double estimate, double variance) {
		Statement stmt = null;
		try {
			stmt = DB.getInstance().createStatement();
			String sql = "INSERT INTO tasks  (name, description, progress, optimistic, pessimistic, estimate, variance, task_start_date, task_end_date) VALUES ('"
					+ name
					+ "', '"
					+ description					
					+ "', 'In Queue','" // Tasks are set as "In Queue" by default
					+ optimistic
					+ "', '"
					+ pessimistic
					+ "', '"
					+ estimate
					+ "', '"
					+ variance
					+ "', '"
					+ taskStartDate.getTime()
					+ "', '"
					+ taskEndDate.getTime()
					+ "');";
			stmt.executeUpdate(sql);

			// Grab latest autoincrement id
			sql = "SELECT last_insert_rowid() as id";
			ResultSet rs = stmt.executeQuery(sql);

			if (!rs.next()) {
				System.err.println("Error while creating table '" + name + "'");
				return;
			}

			int task_id = rs.getInt("id");

			sql = "INSERT INTO project_tasks VALUES (" + _id + ", " + task_id
					+ ")";
			stmt.executeUpdate(sql);

			_tasks.add(new Task(task_id, name, description,
					TaskProgress.IN_QUEUE, taskStartDate, taskEndDate, optimistic, pessimistic, estimate, variance));

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
 
	// Assigns a member to a task and returns a boolean based on whether or not
	// an error occurs
	public boolean assignMember(User member, int taskId) {
		int memberId = member.getId();

		Statement stmt = null;
		try {
			stmt = DB.getInstance().createStatement();

			String sql = "SELECT * FROM tasks_members WHERE user_id == "
					+ memberId + " AND task_id == " + taskId;
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				JOptionPane.showMessageDialog(null,
						"This member is already assigned to this task.",
						"Invalid Assignment", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			sql = "INSERT INTO tasks_members VALUES (" + taskId + ", "
					+ memberId + ")";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return true;
	}
}
