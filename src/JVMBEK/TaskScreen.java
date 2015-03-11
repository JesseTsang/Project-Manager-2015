package JVMBEK;

import java.awt.event.*;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.table.*;

public class TaskScreen extends Screen {
	public static final String IDENTIFIER = "TASK";
	public static final int WIDTH = 450;
	public static final int HEIGHT = 300;

	private JLabel _lblProjectHeader;
	private JTable tblTasks;
	private DefaultTableModel _model;

	public TaskScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	public void SetupGUI() {
		setLayout(new BorderLayout());
		_lblProjectHeader = new JLabel();

		JButton btnAdd = new JButton("Add Task");
		JButton btnDelete = new JButton("Delete Task");
		JButton btnGenerate = new JButton("Generate GANTT Chart");
		JButton btnBack = new JButton("Back");
		JButton btnShow = new JButton("Show Members");

		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel buttonPanel = new JPanel(new GridLayout(2, 2));

		tblTasks = new JTable();
		tblTasks.setPreferredScrollableViewportSize(new Dimension(350, 150));
		tblTasks.setFillsViewportHeight(true);

		JScrollPane tasksScrollPane = new JScrollPane(tblTasks);
		tasksScrollPane.setOpaque(true);
		
		northPanel.add(_lblProjectHeader);
		centerPanel.add(tasksScrollPane);
		buttonPanel.add(btnAdd);
		buttonPanel.add(btnDelete);
		buttonPanel.add(btnGenerate);
		buttonPanel.add(btnBack);
		buttonPanel.add(btnShow);

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

				// check for selected row first
				if (tblTasks.getSelectedRow() != -1) {

					int dialogButton = JOptionPane.YES_NO_OPTION;
					if (JOptionPane.showConfirmDialog(null,
							"Do you really want to delete this task?",
							"Warning", dialogButton) == JOptionPane.YES_OPTION) {
						// Delete the task from the database
						Integer t_id = (Integer) _model.getValueAt(row, 0);

						_manager.getProjectManager().getSelectedProject()
								.deleteTask(t_id);

						// remove selected row from the model
						_model.removeRow(tblTasks.getSelectedRow());
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

				// check for selected row first
				if (tblTasks.getSelectedRow() != -1) {

				} else {
					JOptionPane
							.showMessageDialog(
									null,
									"You must select a task for which to generate a GANTT chart.",
									"No Task Selected",
									JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				_manager.show(ShowMembersScreen.IDENTIFIER);
			}
		});
	}

	@Override
	public void Update() {
		_lblProjectHeader.setText("\""
				+ _manager.getProjectManager().getSelectedProject().getName()
				+ "\" Tasks");

		String[] columnNames = { "Task ID", "Task Name", "Description",
				"Precedence", "Progress" };

		ArrayList<Task> tasks = _manager.getProjectManager()
				.getSelectedProject().getTasks();
		Object[][] data = new Object[tasks.size()][];

		for (int i = 0; i < tasks.size(); i++) {
			Task t = tasks.get(i);
			data[i] = new Object[] { t.getId(), t.getName(),
					t.getDescription(), t.getPrecedingIdsAsString(),
					Task.PROGRESS_STRINGS[t.getProgress().ordinal()] };
		}

		_model = new DefaultTableModel(data, columnNames);
		tblTasks.setModel(_model);
	}
}
