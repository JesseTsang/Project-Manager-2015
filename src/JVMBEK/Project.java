package JVMBEK;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

//A class that encapsulates all data in a row of the "projects" table 
public class Project {
	private int _id;
	private String _name;
	private String _description;
	private Date _created;
	private ArrayList<Task> _tasks;

	public Project(int id, String name, String descr, Date created) {
		_id = id;
		_name = name;
		_description = descr;
		_created = created;
		_tasks = new ArrayList<Task>();

		populateTasks();
	}

	// Loads all tasks assigned to this project in the database
	private void populateTasks() {
		try {
			// Get tasks for each project
			Statement task_stmt = DB.getInstance().createStatement();
			ResultSet task_set = task_stmt
					.executeQuery("SELECT id, name, description, comment, start_date, "
							+ "duration, progress FROM tasks, project_tasks "
							+ "WHERE project_tasks.project_id == "
							+ _id
							+ " AND tasks.id == project_tasks.task_id;");

			while (task_set.next()) {
				TaskProgress prog = Task.StringToProgress(task_set
						.getString("progress"));

				// TODO: get dates such as task_set.getDate("start_date"),
				Task t = new Task(task_set.getInt("id"),
						task_set.getString("name"),
						task_set.getString("description"), prog,
						task_set.getDate("start_date"),
						task_set.getInt("duration"));

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

				TaskProgress prog = Task.StringToProgress(task_set
						.getString("progress"));

				// TODO: get dates such as task_set.getDate("start_date"),
				Task t = new Task(task_set.getInt("id"),
						task_set.getString("name"),
						task_set.getString("description"), prog,
						task_set.getDate("start_date"),
						task_set.getInt("duration"));

				assignedTasks.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return assignedTasks;
	}

	// public ArrayList<Task>

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

	public void addTask(String name, String description, int duration) {
		Date date = new Date();
		Statement stmt = null;
		try {
			stmt = DB.getInstance().createStatement();
			String sql = "INSERT INTO tasks  (name, description, start_date, duration) VALUES ('"
					+ name
					+ "', '"
					+ description
					+ "', "
					+ date.getTime()
					+ ", " + duration + ");";
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
					TaskProgress.IN_QUEUE, date, duration));

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
