package JVMBEK;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

//A class that encapsulates all data from one row of the "tasks" table
public class Task {
	public static final String[] PROGRESS_STRINGS = { "In Queue",
			"In Progress", "Finished" };

	//Because the status of the progress we retrive from database will be in String,
	//therefore, we would need a function to convert that string result back to TaskProgress object.
	public static TaskProgress StringToProgress(String str) {
		for (int i = 0; i < PROGRESS_STRINGS.length; i++)
			if (PROGRESS_STRINGS[i].equalsIgnoreCase(str))
				return TaskProgress.values()[i];
		return TaskProgress.IN_QUEUE;
	}

	private int _id; 				//Task ID
	private String _name; 			//Task Name
	private int _duration; 			//Task Duration
	private String _description; 	//Task Description
	private TaskProgress _progress; //Task Progress status variable
	private int _optimistic;
	private int _pessimistic;
	private double _estimate;
	private double _variance;
	private Date _start_date; 		//Task Start Date
	private Date _end_date; 		//Task End Date
	ArrayList<User> _members = new ArrayList<User>(); //Storage for project members list
	private List predecessors; //Storage for the task that this task depends on.
	
	public Task(int id, String name, String description, TaskProgress progress, Date startDate, Date endDate, int optimistic, int pessimistic, double estimate, double variance)
	{
		_id = id;
		_name = name;
		_description = description;
		_progress = progress;
		_start_date = startDate;
		_end_date = endDate;
		_optimistic = optimistic;
		_pessimistic = pessimistic;
		_estimate = estimate;
		_variance = variance;
		
		_duration = getDuration();	
		
		this.predecessors = new java.util.ArrayList();
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
			if(_members != null) {
				_members.clear();
			}

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

	public void setTaskProgress(String progress) {
		// Making sure the given string is one of our presets
		if(!progress.equals("In Queue") && !progress.equals("In Progress") && !progress.equals("Finished")) {
			return;
		}
		Statement stmt = null;
		try {
			stmt = DB.getInstance().createStatement();
			String sql = "UPDATE tasks SET progress='" + progress 
					+ "' WHERE id='" + _id + "';";
			stmt.executeUpdate(sql);

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public int getId() {
		return _id;
	}
	
	public int getDuration() 
	{
		return _duration;
	}
	
	public int getOptimistic() {	
		return _optimistic;
	}
	
	public int getPessimistic() {
		return _pessimistic;
	}
	
	public double getEstimate() {
		return _estimate;
	}
	
	public double getVariance() {
		return _variance;
	}
	
	public String getName() {
		return _name;
	}

	public String getDescription() {
		return _description;
	}

	public TaskProgress getProgress() {
		return _progress;
	}
	
	public ArrayList<Integer> getPrecedingIds()
	{
		ArrayList<Integer> preceding_tasks = new ArrayList<Integer>();
		Statement stmt = null;
		
		try 
		{
			stmt = DB.getInstance().createStatement();
			ResultSet id_set = stmt.executeQuery("SELECT preceding_task FROM task_sequence "
					+ "WHERE task_id ==" + _id);
			
			while(id_set.next())
			{
				preceding_tasks.add(id_set.getInt("preceding_task"));
			}
					
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		
		return preceding_tasks;
	}
	
	public String getPrecedingIdsAsString()
	{
		String preceding_tasks = "";
		Statement stmt = null;
		
		try 
		{
			stmt = DB.getInstance().createStatement();
			ResultSet id_set = stmt.executeQuery("SELECT preceding_task FROM task_sequence "
					+ "WHERE task_id ==" + _id);
			
			while(id_set.next())
			{
				preceding_tasks += (id_set.getInt("preceding_task") + " ");
			}
			
			
		} 
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		
		return preceding_tasks;
	}
	
	public List getPredecessors()
	{
		predecessors = new java.util.ArrayList();	
		Statement stmt = null;
		
		try 
		{
			stmt = DB.getInstance().createStatement();
			ResultSet id_set = stmt.executeQuery("SELECT preceding_task, tasks.name, tasks.task_start_date, tasks.task_end_date, tasks.progress, "
											   + "tasks.description, tasks.optimistic, tasks.pessimistic, tasks.estimate, tasks.variance "
											   + "FROM task_sequence, tasks "
											   + "WHERE task_id ==" + _id + " "
											   + "AND preceding_task == tasks.id ");
			
			while(id_set.next())
			{
				int taskID = id_set.getInt("preceding_task");
				String taskName = id_set.getString("name");
				String taskDescription = id_set.getString("description");
				
				String taskProgressString = id_set.getString("progress");
				TaskProgress taskProgress = StringToProgress(taskProgressString);
				
				Date startDateFromDB = id_set.getDate("task_start_date");
				Date endDateFromDB   = id_set.getDate("task_end_date");
				int optimisticFromDB = id_set.getInt("optimistic");
				int pessimisticFromDB = id_set.getInt("pessimistic");
				int estimateFromDB = id_set.getInt("estimate");
				int varianceFromDB = id_set.getInt("variance");
				
				if (_id != taskID)
				{
					Task taskFromDB = new Task(taskID, taskName, taskDescription, taskProgress, 
							   				   startDateFromDB, endDateFromDB, optimisticFromDB, pessimisticFromDB, estimateFromDB, varianceFromDB);
					
					predecessors.add(taskFromDB);
				}
			}	
		} 
		catch (Exception e) 
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		return predecessors;
	}
	
	public boolean updateTaskSequence(){
		
		boolean hasNoFollowingTask = true;
				
		Statement stmt = null;
		try {
			stmt = DB.getInstance().createStatement();

			ResultSet id_set = stmt.executeQuery(
					"SELECT preceding_task FROM task_sequence "
					+ "WHERE preceding_task == " 
					+ _id
					+ " AND task_id != " 
					+ _id);
				//Find if the current task have a preceding task, skips when it is preceding itself
				//(Find if the current task have a task after it)
	
			while( id_set.next() ) {
			    // ResultSet processing here
			    hasNoFollowingTask = false;
			}

			if( hasNoFollowingTask ) {
			    // Empty result set, the task doesn't have a following task
				stmt.executeUpdate("DELETE FROM task_sequence WHERE task_id == " + _id);
				
			}			
			
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		
		return hasNoFollowingTask;

	}
	
	public ArrayList<User> getAssignedMembers() {
		loadMembers();
		return _members;
	}
	
	public Date getTaskStart() {
		Date taskStart = this.getProject().getStartDate();
		ArrayList<Integer> precedingIds = this.getPrecedingIds();
		if(precedingIds.size() > 1) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(taskStart);
			
			// Used to save the duration of chains of tasks linked together
			int chain = 0;
			
			Task temp = getLongestPrecedingTask();
			
			while(temp != null) {
				chain += temp.getDuration();
				temp = getLongestPrecedingTask();
			}
			
			// For multiple precedence (one task with two or more tasks directly preceding it),
			// take the preceding task that is the longest for calculations
			int longest = 0;

			for (int i : precedingIds) {
				if(longest < this.getProject().getTaskById(i).getDuration()) {
					longest = this.getProject().getTaskById(i).getDuration();
					chain += longest;
					
				}
				cal.add(Calendar.DATE, this.getProject().getTaskById(i).getDuration());
			}
			cal.add(Calendar.DATE, longest);
			taskStart = cal.getTime();
		}
		return taskStart;
	}
	
	// Returns the longest immediately-preceding task, null if none
	public Task getLongestPrecedingTask() {
		ArrayList<Integer> precedingIds = this.getPrecedingIds();
		if(precedingIds.size() != 0) {
			return null;
		}
		else {
			int longest = 0;
			int longestId = 0;
			for(int i : precedingIds) {
				if(longest < this.getProject().getTaskById(i).getDuration()) {
					longest = this.getProject().getTaskById(i).getDuration();
					longestId = i;
				}
			}
			return this.getProject().getTaskById(longestId);
		}
	}
	
	public Date getStartDate()
	{
		return _start_date;
	}
	
	public Date getEndDate()
	{
		return _end_date;
	}

	public int getPredecessorCount() 
	{
		return predecessors.size();
	}
}
