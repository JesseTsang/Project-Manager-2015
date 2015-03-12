package JVMBEK;

import java.awt.event.*;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.table.*;

import org.jfree.ui.RefineryUtilities;

public class TaskScreen extends Screen {
	public static final String IDENTIFIER = "TASK";
	public static final int WIDTH = 450;
	public static final int HEIGHT = 300;

	private JLabel lblProjectHeader;
	private JTable tblTasks;
	private DefaultTableModel model;

	public TaskScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	public void SetupGUI() {
		setLayout(new BorderLayout());
		lblProjectHeader = new JLabel();

		JButton btnAdd = new JButton("Add Task");
		JButton btnDelete = new JButton("Delete Task");
		JButton btnGenerate = new JButton("Generate GANTT Chart");
		JButton btnBack = new JButton("Back");
		JButton btnShow = new JButton("Show Members");
		JButton btnShowPaths = new JButton("Get Paths");

		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel buttonPanel = new JPanel(new GridLayout(2, 2));

		tblTasks = new JTable();
		tblTasks.setPreferredScrollableViewportSize(new Dimension(350, 150));
		tblTasks.setFillsViewportHeight(true);

		JScrollPane scroll = new JScrollPane(tblTasks);
		scroll.setOpaque(true);
//		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		northPanel.add(lblProjectHeader);
		centerPanel.add(scroll);
		buttonPanel.add(btnAdd);
		buttonPanel.add(btnDelete);
		buttonPanel.add(btnGenerate);
		buttonPanel.add(btnBack);
		buttonPanel.add(btnShow);
		buttonPanel.add(btnShowPaths);

		add(BorderLayout.NORTH, northPanel);
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, buttonPanel);

		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				_manager.showAndResize(LoadScreen.IDENTIFIER, LoadScreen.WIDTH,
						LoadScreen.HEIGHT);
			}
		});

		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				_manager.showAndResize(CreateTaskScreen.IDENTIFIER, CreateTaskScreen.WIDTH,
						CreateTaskScreen.HEIGHT);
			}
		});

		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int row = tblTasks.getSelectedRow();
				boolean hasNoFollowingTask;

				// check for selected row first
				if (tblTasks.getSelectedRow() != -1) {

					int dialogButton = JOptionPane.YES_NO_OPTION;
					if (JOptionPane.showConfirmDialog(null,
							"Do you really want to delete this task?",
							"Warning", dialogButton) == JOptionPane.YES_OPTION) {
						// Delete the task from the database

						Integer t_id = (Integer) model.getValueAt(row, 0);
						
						hasNoFollowingTask = _manager.getProjectManager().getSelectedProject().getTaskById(t_id).updateTaskSequence();
						
						if (!hasNoFollowingTask){
							JOptionPane.showMessageDialog(null,
									"This task cannot be deleted because other tasks depend on it.",
									"Has Following Task", JOptionPane.ERROR_MESSAGE);
						} else{

							_manager.getProjectManager().getSelectedProject()
									.deleteTask(t_id);						
	
							// remove selected row from the model
							model.removeRow(tblTasks.getSelectedRow());
						}
					}
				} else {
					JOptionPane.showMessageDialog(null,
							"You must select a task to be deleted.",
							"No Task Selected", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

			btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				final GanttChart chart = new GanttChart(_manager.getProjectManager().getSelectedProject().getName() + "Gantt Chart", _manager);
				chart.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				chart.pack();
				RefineryUtilities.centerFrameOnScreen(chart);
				chart.setVisible(true);
			}
		});

		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				_manager.show(ShowMembersScreen.IDENTIFIER);
			}
		});
		
		
		btnShowPaths.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				int row = tblTasks.getSelectedRow();
				
				// check for selected row first
				if (tblTasks.getSelectedRow() != -1) {

						// Delete the task from the database

						Integer t_id = (Integer) model.getValueAt(row, 0);
						
						ArrayList<ArrayList<Integer>> pathList;
						
						pathList = _manager.getProjectManager().getSelectedProject().getTaskById(t_id).getPathsLeadingToTask();
						
						for (ArrayList<Integer> p : pathList) {
							System.out.println("\nnew: ");
							for (int id : p){
								System.out.print(id + ", ");
							}
						}
						}
					}
		});
	}

	@Override
	public void Update() {
		lblProjectHeader.setText(_manager.getProjectManager().getSelectedProject().getName().toUpperCase()
				+ " Tasks");

		String[] columnNames = { "Task ID", "Task Name", "Description",
				"Precedence", "Progress" };

		ArrayList<Task> tasks = _manager.getProjectManager()
				.getSelectedProject().getTasks();
		Object[][] data = new Object[tasks.size()][];

		for (int i = 0; i < tasks.size(); i++) {
			Task t = tasks.get(i);
			String strPrecedence = t.getPrecedingIdsAsString();
			if((Integer.toString(t.getId()) + " ").equals(strPrecedence)) {
				strPrecedence = "None";
			}
			data[i] = new Object[] { t.getId(), t.getName(),
					t.getDescription(), strPrecedence,
					Task.PROGRESS_STRINGS[t.getProgress().ordinal()] };
		}

		model = new DefaultTableModel(data, columnNames);
		tblTasks.setModel(model);
	}
}
