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
	public static final int WIDTH = 350;
	public static final int HEIGHT = 400;

	private JTextField tfTaskName;
	private JTextArea taDescription;
	private JTextField tfDuration;
	private JCheckBox cbFirstTask; 
	private JTable tblTasks;
	private DefaultTableModel _model;

	

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
		JLabel lblFirstTaskCheckBox = new JLabel(/*"Check if no preceding taks:"*/);
		JLabel lblPrecedingTasks = new JLabel("Select Preceding Tasks:");
		JLabel lblDuration = new JLabel("Task Duration:");
		
		tblTasks = new JTable();
		tblTasks.setPreferredScrollableViewportSize(new Dimension(350, 150));
		tblTasks.setFillsViewportHeight(true);

		JScrollPane tasksScrollPane = new JScrollPane(tblTasks);
		tasksScrollPane.setOpaque(true);


		tfTaskName = new JTextField(10);
		taDescription = new JTextArea();
		JScrollPane descriptionScrollPane = new JScrollPane(taDescription);
//		descriptionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//		descriptionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		tfDuration = new JTextField(10);

		JButton btnCreate = new JButton("Create Task");
		JButton btnCancel = new JButton("Cancel");
		northPanel.add(lblTaskName);
		northPanel.add(tfTaskName);
		northPanel.add(lblDuration);
		northPanel.add(tfDuration);
		northPanel.add(lblFirstTaskCheckBox);
		cbFirstTask = new JCheckBox("No Preceding Task"); 
		northPanel.add(cbFirstTask);
		northPanel.setLayout(new GridLayout(3, 2, 5, 5));



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

		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (tfTaskName.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null,
							"Please enter a name for your task.",
							"Missing field", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (tfDuration.getText().isEmpty()
						|| !tfDuration.getText().matches("-?\\d+(\\.\\d+)?")) {
					JOptionPane.showMessageDialog(null,
							"Please enter a valid duration (# of days).",
							"Incorrect duration", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!Character.isLetter(tfTaskName.getText().charAt(0))) {
					JOptionPane.showMessageDialog(null,
							"Project name must begin with a letter.",
							"Incorrect naming", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				boolean isFirstTask = false;

				ArrayList<Integer> precedingTasks = new ArrayList<Integer>();
				
				if (cbFirstTask.isSelected()){
//					precedingTasks.add(-1);
					isFirstTask = true;
					
				}else if (tblTasks.getSelectedRow() == -1){
					JOptionPane.showMessageDialog(null,
							"Please set the preceding task(s) or select no preceding task.",
							"Missing preceding task", JOptionPane.ERROR_MESSAGE);
					return;
				}else{
					isFirstTask = false;
					int[] selectedTasksIndices = tblTasks.getSelectedRows();
					
					for (int i =0; i<tblTasks.getSelectedRowCount(); i++){
						int j = selectedTasksIndices[i];
						ArrayList<Task> tasks = _manager.getProjectManager()
								.getSelectedProject().getTasks();

						precedingTasks.add((_manager.getProjectManager().getSelectedProject()
								.getTaskById( tasks.get(j).getId() ))
								.getId());
					}
				}
				
//				
//
//precedingTasks is an ArrayList of integers
//will be entered into the task_sequence table in sql
//row preceding_task
//
//if no precedingTask, boolean isFirstTask is true and the preceding_task in task_sequence will be itself
//Cannot be assigned until after addTask()
//
//
//			

				int dur = Integer.parseInt(tfDuration.getText());

				_manager.getProjectManager()
						.getSelectedProject()
						.addTask(tfTaskName.getText(), taDescription.getText(),
								dur);
				
//				
//				
//Getting the id of the task that just got created
//in order to setup the preceding tasks relation in the db
//				
//				
							
				ArrayList<Task> tList;
				int current_id;	
				tList = _manager.getProjectManager().getSelectedProject().getTasks();
				current_id = tList.get(tList.size()-1).getId();
				
				if (isFirstTask == true){
					precedingTasks.clear();
					precedingTasks.add(current_id);
				}
				
//insertion into the table
				
				for(int i=0; i<precedingTasks.size();i++){
					Statement stmt = null;
					try {
						stmt = DB.getInstance().createStatement();
						String sql = "INSERT INTO task_sequence (task_id, preceding_task) VALUES ('"
								+ current_id
								+ "', '"
								+ precedingTasks.get(i)
								+ "');";
						stmt.executeUpdate(sql);
	
					} catch (Exception e) {
						System.err.println(e.getClass().getName() + ": " + e.getMessage());
						System.exit(0);
					}
				}
			

				
				
				

				_manager.showAndResize(TaskScreen.IDENTIFIER, TaskScreen.WIDTH,
						TaskScreen.HEIGHT);
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				_manager.showAndResize(TaskScreen.IDENTIFIER, TaskScreen.WIDTH,
						TaskScreen.HEIGHT);
			}
		});
	}

	@Override
	public void Update() {
		tfTaskName.setText("");
		taDescription.setText("");
		tfDuration.setText("");
		
	
		String[] columnNames = { "Task ID", "Task Name", "Description",
				"Progress" };

		ArrayList<Task> tasks = _manager.getProjectManager()
				.getSelectedProject().getTasks();
		Object[][] data = new Object[tasks.size()][];

		for (int i = 0; i < tasks.size(); i++) {
			Task t = tasks.get(i);
			data[i] = new Object[] { t.getId(), t.getName(),
					t.getDescription(),
					Task.PROGRESS_STRINGS[t.getProgress().ordinal()] };
		}

		_model = new DefaultTableModel(data, columnNames);
		tblTasks.setModel(_model);

	}
}