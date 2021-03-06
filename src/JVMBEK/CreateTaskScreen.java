package JVMBEK;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class CreateTaskScreen extends Screen {
	public final static String IDENTIFIER = "CREATETASK";
	public static final int WIDTH = 1176;//original 500
	public static final int HEIGHT = 624;//original 550

	private JTextField tfTaskName;
	private JTextArea taDescription;
	private JTextField tfDuration;
	private JTextField tfOptimistic;
	private JTextField tfPessimistic;
	private JCheckBox cbFirstTask;
	private JTable tblTasks;
	private DefaultTableModel _model;
	
	private JTextField tfTaskStartDate;	//in dd-MM-yyyy
	private JTextField tfTaskEndDate;	//in dd-MM-yyyy
	
	private long taskDuration;
	Date taskStartDate, taskEndDate;

	public CreateTaskScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	public void SetupGUI() {
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();

		JLabel lblTaskName = new JLabel("Task Name:");
		JLabel lblDescription = new JLabel("Task Description:");
		JLabel lblFirstTaskCheckBox = new JLabel(/* "Check if no preceding tasks:" */);
		JLabel lblPrecedingTasks = new JLabel("Select Preceding Tasks:");
		JLabel lblDuration = new JLabel("Task Duration (in Days):");
		JLabel lblOptimistic = new JLabel("Optimistic:");
		JLabel lblPessimistic = new JLabel("Pessimistic:");
		JLabel lblTaskStartDate = new JLabel("Start Date (DD-MM-YYYY):");
		JLabel lblTaskEndDate = new JLabel("End Date (DD-MM-YYYY):");
		
		tblTasks = new JTable();
		tblTasks.setPreferredScrollableViewportSize(new Dimension(490, 210));//350, 150
		tblTasks.setFillsViewportHeight(true);
		
		JScrollPane tasksScrollPane = new JScrollPane(tblTasks);
		tasksScrollPane.setOpaque(true);

		tfTaskName = new JTextField(10);
		taDescription = new JTextArea();
		JScrollPane descriptionScrollPane = new JScrollPane(taDescription);
		descriptionScrollPane.setOpaque(true);
		tfDuration = new JTextField(10);
		tfOptimistic = new JTextField(10);
		tfPessimistic = new JTextField(10);
		tfTaskStartDate = new JTextField(10);
		tfTaskEndDate = new JTextField(10);

		JButton btnCreate = new JButton("Create Task");
		JButton btnCancel = new JButton("Cancel");
		northPanel.add(lblTaskName);
		northPanel.add(tfTaskName);
		northPanel.add(lblTaskStartDate);
		northPanel.add(tfTaskStartDate);
		northPanel.add(lblTaskEndDate);
		northPanel.add(tfTaskEndDate);
		northPanel.add(lblOptimistic);
		northPanel.add(tfOptimistic);
		northPanel.add(lblPessimistic);
		northPanel.add(tfPessimistic);
		northPanel.add(lblFirstTaskCheckBox);
		cbFirstTask = new JCheckBox("No Preceding Task", false);
		northPanel.add(cbFirstTask);
		northPanel.setLayout(new GridLayout(7, 3, 5, 5));

		centerPanel.add(lblPrecedingTasks);
		centerPanel.add(tasksScrollPane);
		centerPanel.add(lblDescription);
		centerPanel.add(descriptionScrollPane);
		centerPanel.setLayout(new GridLayout(2, 1));

		southPanel.add(btnCreate);
		southPanel.add(btnCancel);
		southPanel.setLayout(new FlowLayout());

		setLayout(new BorderLayout());
		add(BorderLayout.NORTH, northPanel);
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, southPanel);

		btnCreate.addActionListener
		(new ActionListener() 
		{
			public void actionPerformed(ActionEvent ae) 
			{
				if (tfTaskName.getText().isEmpty()) 
				{
					JOptionPane.showMessageDialog(null,
							"Please enter a name for your task.",
							"Missing field", JOptionPane.ERROR_MESSAGE);
					return;
				}

				//Extract StartDate -> String, then store as a Date variable.
				DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy"/*, Locale.ENGLISH*/);
				formatter.setLenient(false);
				Calendar cal = Calendar.getInstance();
				String startDateString = tfTaskStartDate.getText();
				String endDateString = tfTaskEndDate.getText();
								
				//If either tfTaskStartDate AND tfTaskEndDate are not empty, then ...
				if(!tfTaskStartDate.getText().isEmpty() && !tfTaskEndDate.getText().isEmpty())
				{
					try 
					{
					    cal.setTime(formatter.parse(startDateString));
					    taskStartDate = formatter.parse(startDateString);
					    taskEndDate = formatter.parse(endDateString);
				    
					    // > 0 means later, < 0 means earlier 
					    if (taskStartDate.compareTo(taskEndDate) > 0)
					    {
					    	JOptionPane.showMessageDialog(null,
								      					  "Start date must be set before end date.",
								      					  "Invalid date entry", JOptionPane.ERROR_MESSAGE);
					    	return;
					    }
					    else
					    {
					    	taskDuration = DateUtils.getDuration(taskStartDate, taskEndDate);
					    }
					}
					catch (Exception e) 
					{
						JOptionPane.showMessageDialog(null,
												      "Task dates must be entered in the following format: DD-MM-YYYY.",
												      "Incorrect date format", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null,
												  "Please enter both task start date and end date.",
												  "Missing Field", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (tfOptimistic.getText().isEmpty() || Integer.parseInt(tfOptimistic.getText()) >= taskDuration)
				{
					JOptionPane.showMessageDialog(null,
							"Please enter an optimistic value for your task. (Smaller than duration)",
							"Missing field", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (tfPessimistic.getText().isEmpty() || Integer.parseInt(tfPessimistic.getText()) <= taskDuration)
				{
					JOptionPane.showMessageDialog(null,
							"Please enter a pessimistic value for your task. (Greater than duration)",
							"Missing field", JOptionPane.ERROR_MESSAGE);
					return;
				}


				if (!Character.isLetter(tfTaskName.getText().charAt(0)))
				{
					JOptionPane.showMessageDialog(null,
							"Project name must begin with a letter.",
							"Incorrect naming", JOptionPane.ERROR_MESSAGE);
					return;
				}

				boolean isFirstTask = false;

				ArrayList<Integer> precedingTasks = new ArrayList<Integer>();

				if (cbFirstTask.isSelected()) 
				{
					isFirstTask = true;
				}
				else if (tblTasks.getSelectedRow() == -1)
				{
					JOptionPane
							.showMessageDialog(
									null,
									"Please select the preceding task(s) or check the box \"No preceding tasks\".",
									"Missing preceding task",
									JOptionPane.ERROR_MESSAGE);
					return;
				}
				else
				{
					isFirstTask = false;
					int[] selectedTasksIndices = tblTasks.getSelectedRows();

					for (int i = 0; i < tblTasks.getSelectedRowCount(); i++)
					{
						int j = selectedTasksIndices[i];
						ArrayList<Task> tasks = _manager.getProjectManager()
								.getSelectedProject().getTasks();

						precedingTasks.add((_manager.getProjectManager()
								.getSelectedProject().getTaskById(tasks.get(j)
								.getId())).getId());
					}
				}

				// precedingTasks is an ArrayList of integers
				// will be entered into the task_sequence table in sql
				// row preceding_task
				//
				// if no precedingTask, boolean isFirstTask is true and the
				// preceding_task in task_sequence will be itself
				// Cannot be assigned until after addTask()

				int dur = (int) taskDuration;
				int optimistic = Integer.parseInt(tfOptimistic.getText());
				int pessimistic = Integer.parseInt(tfPessimistic.getText());
				double e1 = optimistic + (4*dur) + pessimistic;
				double estimate = e1/6;
				double v1 = pessimistic - optimistic;
				double variance = v1/6*v1/6;
							
				_manager.getProjectManager()
				.getSelectedProject()
				.addTask(tfTaskName.getText(), taDescription.getText(),
						taskStartDate, taskEndDate, optimistic, pessimistic, estimate, variance);
							
				// Getting the id of the task that just got created
				// in order to setup the preceding tasks relation in the db

				ArrayList<Task> tList;
				int current_id;
				tList = _manager.getProjectManager().getSelectedProject()
						.getTasks();
				current_id = tList.get(tList.size() - 1).getId();

				if (isFirstTask == true) 
				{
					precedingTasks.clear();
					precedingTasks.add(current_id);
				}

				// insertion into the table
				for (int i = 0; i < precedingTasks.size(); i++) 
				{
					Statement stmt = null;
					try 
					{
						stmt = DB.getInstance().createStatement();
						String sql = "INSERT INTO task_sequence (task_id, preceding_task) VALUES ('"
								+ current_id
								+ "', '"
								+ precedingTasks.get(i)
								+ "');";
						stmt.executeUpdate(sql);

					} 
					catch (Exception e) 
					{
						System.err.println(e.getClass().getName() + ": "
								+ e.getMessage());
						System.exit(0);
					}
				}

				_manager.showAndResize(TaskScreen.IDENTIFIER, TaskScreen.WIDTH,
									   TaskScreen.HEIGHT);
			}
		});

		btnCancel.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent ae) 
			{
				_manager.showAndResize(TaskScreen.IDENTIFIER, TaskScreen.WIDTH,
									   TaskScreen.HEIGHT);
			}
		});
	}
	
	@Override
	public void Update() 
	{
		tfTaskName.setText("");
		taDescription.setText("");
		tfDuration.setText("");
		tfTaskStartDate.setText("");
		tfTaskEndDate.setText("");
		tfOptimistic.setText("");
		tfPessimistic.setText("");

		String[] columnNames = { "Task ID", "Task Name",
				"Progress", "Start Date", "End Date" , "Optimistic", "Pessimistic"};

		ArrayList<Task> tasks = _manager.getProjectManager().getSelectedProject().getTasks();
		Object[][] data = new Object[tasks.size()][];

		for (int i = 0; i < tasks.size(); i++) 
		{
			Task t = tasks.get(i);
			data[i] = new Object[] 
					{ 
						t.getId(), 
						t.getName(),
						Task.PROGRESS_STRINGS[t.getProgress().ordinal()],
						t.getStartDate(),
						t.getEndDate(),
						t.getOptimistic(),
						t.getPessimistic(),
					};
		}

		_model = new DefaultTableModel(data, columnNames);
		tblTasks.setModel(_model);
	}
}