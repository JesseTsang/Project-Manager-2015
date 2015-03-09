package JVMBEK;
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
	
	public Task(int id, String name, String description, TaskProgress progress, Date start, int dur) {
		_id = id;
		_name = name;
		_description = description;
		_progress = progress;
		//_comment = comment;
		_start_date = start;
		_duration = dur;
	}
	
	public int getId() { return _id; }
	public String getName() { return _name; }
	public String getDescription() { return _description; }
	public String getComment() { return _comment; }
	public TaskProgress getProgress() { return _progress; }
}