package JVMBEK;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MemberViewScreen extends Screen {
	public static final String IDENTIFIER = "VIEW";
	public static final int WIDTH = 450;
	public static final int HEIGHT = 300;

	private JLabel lblAssignedTasks;
	private JTable tblTasks;
	private DefaultTableModel model;

	public MemberViewScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	public void SetupGUI() {
		JButton btnBack = new JButton("Back");
		JLabel lblAssignedTasks = new JLabel("Assigned Tasks");
		setLayout(new BorderLayout());

		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();

		tblTasks = new JTable();
		tblTasks.setPreferredScrollableViewportSize(new Dimension(350, 150));
		tblTasks.setFillsViewportHeight(true);

		JScrollPane scroll = new JScrollPane(tblTasks);
		scroll.setOpaque(true);

		northPanel.add(lblAssignedTasks);
		centerPanel.add(scroll);
		southPanel.add(btnBack);

		add(BorderLayout.NORTH, northPanel);
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, southPanel);

		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				_manager.showAndResize(MemberMainScreen.IDENTIFIER,
						MemberMainScreen.WIDTH, MemberMainScreen.HEIGHT);
			}
		});
	}

	@Override
	public void Update() {
		// lblAssignedTasks.setText("Test");

		String[] columnNames = { "Project Name", "Task ID", "Task Name",
				"Start Date", "Due Date", "Description" };

		ArrayList<Task> tasks = Project.getAssignedTasks(_manager.getUser()
				.getId());
		Object[][] data = new Object[tasks.size()][];

		for (int i = 0; i < tasks.size(); i++) {
			Task t = tasks.get(i);
			data[i] = new Object[] { t.getProject().getName(), t.getId(),
					t.getName(), "StartDate", "EndDate", t.getDescription() };
		}

		model = new DefaultTableModel(data, columnNames);
		tblTasks.setModel(model);
	}
}
