package JVMBEK;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MemberViewScreen extends Screen {
	public static final String IDENTIFIER = "VIEW";
	public static final int WIDTH = 600;
	public static final int HEIGHT = 300;

	private JLabel lblAssignedTasks;
	private JTable tblTasks;
	private DefaultTableModel model;

	public MemberViewScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	public void SetupGUI() {
		JButton btnView = new JButton("View Detailed Task Information");
		JButton btnBack = new JButton("Back");
		JLabel lblAssignedTasks = new JLabel("Assigned Tasks");
		setLayout(new BorderLayout());

		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();

		tblTasks = new JTable();
		tblTasks.setPreferredScrollableViewportSize(new Dimension(550, 150));
		tblTasks.setFillsViewportHeight(true);

		JScrollPane scroll = new JScrollPane(tblTasks);
		scroll.setOpaque(true);

		northPanel.add(lblAssignedTasks);
		centerPanel.add(scroll);
		southPanel.add(btnView);
		southPanel.add(btnBack);

		add(BorderLayout.NORTH, northPanel);
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, southPanel);
		
		btnView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (tblTasks.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null,
							"You must select a task to view information for.",
							"No Task Selected", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Integer t_id = (Integer) model.getValueAt(tblTasks.getSelectedRow(), 1);
				Project.selectedTaskId = t_id;
				
				_manager.showAndResize(MemberTaskInformationScreen.IDENTIFIER,
						MemberTaskInformationScreen.WIDTH, MemberTaskInformationScreen.HEIGHT);
			}
		});

		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				_manager.showAndResize(MemberMainScreen.IDENTIFIER,
						MemberMainScreen.WIDTH, MemberMainScreen.HEIGHT);
			}
		});
	}

	@Override
	public void Update() {
		String[] columnNames = { "Project Name", "Task ID", "Task Name",
				"Start Date", "Due Date", "Status"};

		ArrayList<Task> tasks = Project.getAssignedTasks(_manager.getUser()
				.getId());
		Object[][] data = new Object[tasks.size()][];
		
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		formatter.setLenient(false);
		Calendar cal = Calendar.getInstance();
		String formattedStart, formattedEnd;

		for (int i = 0; i < tasks.size(); i++) 
		{
			Task t = tasks.get(i);
			
			Date taskStartDate = t.getStartDate();
			Date easkEndDate = t.getEndDate();
			
			cal.setTime(t.getTaskStart());
			cal.add(Calendar.DATE, t.getDuration());
			
			formattedStart = formatter.format(cal.getTime());
			formattedEnd = formatter.format(cal.getTime());
			
			data[i] = new Object[] { 
								   	   t.getProject().getName(), 
								   	   t.getId(),
								   	   t.getName(), 
								   	   //formattedStart, 
								   	   //formattedEnd,
								   	   taskStartDate,
								   	   easkEndDate,
								   	   /*t.getDescription(),*/ 
								   	   Task.PROGRESS_STRINGS[t.getProgress().ordinal()] 
								   };
		}

		model = new DefaultTableModel(data, columnNames);
		tblTasks.setModel(model);
	}
}
