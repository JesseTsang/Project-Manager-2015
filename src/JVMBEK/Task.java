package JVMBEK;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
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
	ArrayList<User> _members = new ArrayList<User>();
	


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
	
	public int getDuration() {
		return _duration;
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
	
	public ArrayList<Integer> getPrecedingIds(){
		ArrayList<Integer> preceding_tasks = new ArrayList<Integer>();
		Statement stmt = null;
		try {
			stmt = DB.getInstance().createStatement();
			ResultSet id_set = stmt.executeQuery("SELECT preceding_task FROM task_sequence "
					+ "WHERE task_id ==" + _id);
			
			while(id_set.next()){
				preceding_tasks.add(id_set.getInt("preceding_task"));
			}
			
			
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		
		return preceding_tasks;
	}
	
	public String getPrecedingIdsAsString(){
		String preceding_tasks = "";
		Statement stmt = null;
		try {
			stmt = DB.getInstance().createStatement();
			ResultSet id_set = stmt.executeQuery("SELECT preceding_task FROM task_sequence "
					+ "WHERE task_id ==" + _id);
			
			while(id_set.next()){
				preceding_tasks += (id_set.getInt("preceding_task") + " ");
			}
			
			
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		
		return preceding_tasks;
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
	
	// Returns an ArrayList of ArrayList<Integer> containing all the possible paths leading up to the task
	//http://stackoverflow.com/questions/25147799/java-arraylist-of-arraylist
	
	static ArrayList<ArrayList<Integer>> outer = new ArrayList<ArrayList<Integer>>();
	static ArrayList<ArrayList<Integer>> outerPrecedingIds = new ArrayList<ArrayList<Integer>>();

	static int count = 0;

	public ArrayList<ArrayList<Integer>> getPathsLeadingToTask() {
		
	    ArrayList<Integer> inner = new ArrayList<Integer>(); 		
		ArrayList<Integer> innerPrecedingIds = this.getPrecedingIds();
		
		
		outerPrecedingIds.add(innerPrecedingIds);
		if(outerPrecedingIds.get(index) == _id) {
		    outer.add(inner);
		    count++;
		}

		// Checking if it actually has any preceding tasks, return 0 if it doesn't
			for (int task_id: outerPrecedingIds.get(count)){

				if(task_id == _id) {
				    outer.add(inner);
//				    count++;
				}
				else {
//					ArrayList<Integer> pathChains = new ArrayList<Integer>();
					Task[] t = new Task[innerPrecedingIds.size()];
					for(int i = 0; i < t.length; i++) {
						t[i] = this.getProject().getTaskById(innerPrecedingIds.get(i));
						inner.add(outerPrecedingIds.get(count).get(i));   
						this.getProject().getTaskById(innerPrecedingIds.get(i)).getPathsLeadingToTask();
					}					
				}

			}
		return outer;

	}
	
	static int divergenceCount;
	
//	public int getNumberOfDivergence() {
//		ArrayList<Integer> innerPrecedingIds = this.getPrecedingIds();
//		outerPrecedingIds.add(innerPrecedingIds);
//		
//		if(innerPrecedingIds.get(0) == _id){
//			   
//		}else{
//			Task[] t = new Task[innerPrecedingIds.size()];
//			for(int i = 0; i < t.length; i++) {
//				t[i] = this.getProject().getTaskById(task_id);
//				inner.add(task_id);     
//				t[i].getNumberOfDivergence();
//
//		}
//		
//		
//	}

	// Returns the total number of preceding tasks; recursive
	// DOES NOT WORK PROPERLY YET
	public int getTotalPrecedingTasks() {
		int temp = getNumberOfImmediatelyPrecedingTasks();
		
		if(temp == 0) {
			return temp;
		}
		else if(temp == 1) {
			return temp = 1;//this.getProject().getTaskById(0).getTotalPrecedingTasks();
		}
		else {
			int counter = 0;
			ArrayList<Integer> precedingIds = this.getPrecedingIds();
			for(int i: precedingIds) {
				counter += this.getProject().getTaskById(i).getTotalPrecedingTasks();
			System.out.println("counter: " + counter);
			}
			
			System.out.println("final counter: " + counter);
			return counter;
		}
	}
	
	// Returns the number of immediately preceding tasks, counting itself
	public int getNumberOfImmediatelyPrecedingTasks() {
		ArrayList<Integer> precedingIds = this.getPrecedingIds();
		if(precedingIds.get(0) == _id) {
			return 1;
		}
		else {
			return precedingIds.size();
		}
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
