package JVMBEK;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.util.ArrayList;

public class AddMemberScreen extends Screen {
	public final static String IDENTIFIER = "ADDMEMBERS";

	private JComboBox cmbMembers;
	private JTable _tblTasks;
	private DefaultTableModel _model;

	private JTextField _memberNameTField;
	private JTextField _taskIdTField;

	private JPanel centerPanel;

	private ArrayList<User> members = new ArrayList<User>();

	public AddMemberScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	public void SetupGUI() {
		JPanel northPanel = new JPanel();
		centerPanel = new JPanel();
		JPanel southPanel = new JPanel();

		JLabel memberName = new JLabel("Member Name:");
		JLabel taskId = new JLabel("Task ID:");
		
		_taskIdTField = new JTextField(15);

		JButton btnAdd = new JButton("Assign");
		JButton btnCancel = new JButton("Cancel");

		_tblTasks = new JTable();
		_tblTasks.setPreferredScrollableViewportSize(new Dimension(350, 150));
		//_tblTasks.setPreferredScrollableViewportSize(_tblTasks.getPreferredSize());
		_tblTasks.setFillsViewportHeight(true);

		JScrollPane scroll = new JScrollPane(_tblTasks);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		northPanel.add(scroll);

		cmbMembers = new JComboBox();

		centerPanel.add(taskId);
		centerPanel.add(_taskIdTField);
		centerPanel.add(memberName);
		centerPanel.add(cmbMembers);
		centerPanel.setLayout(new GridLayout(2, 2, 5, 5));

		southPanel.add(btnAdd);
		southPanel.add(btnCancel);

		setLayout(new BorderLayout());
		add(BorderLayout.NORTH, northPanel);
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, southPanel);

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				_manager.show(ShowMembersScreen.IDENTIFIER);
			}
		});

		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				if (_taskIdTField.getText().isEmpty()
						|| !_taskIdTField.getText().matches("-?\\d+")) {
					JOptionPane.showMessageDialog(null,
							"Please enter a valid ID number.", "Incorrect ID",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				ArrayList<Task> tasks = _manager.getProjectManager()
						.getSelectedProject().getTasks();
				int[] taskIds = new int[tasks.size()];

				int taskId = -1;

				for (int i = 0; i < tasks.size(); i++) {
					Task t = tasks.get(i);
					if (Integer.parseInt(_taskIdTField.getText()) == t.getId()) {
						taskId = Integer.parseInt(_taskIdTField.getText());
					}
				}

				if (taskId == -1) {
					JOptionPane.showMessageDialog(null,
							"Please enter a valid ID number.", "Incorrect ID",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				boolean validAssignment = _manager.getProjectManager().getSelectedProject()
						.assignMember(members.get(cmbMembers.getSelectedIndex()), taskId);
				
				if(validAssignment) {
					_manager.show(ShowMembersScreen.IDENTIFIER);
				}
			}
		});
	}

	private void updateCombo() {

		if(members.isEmpty() == false) {
			members.clear();
		}
		cmbMembers.removeAllItems();

		for (User u : _manager.getUsers()) {

			// Only allow members to get assigned
			if (u.getRole() == UserRole.MEMBER) {
				members.add(u);
				cmbMembers.addItem(u.getUserName());
			}
		}
	}

	@Override
	public void Update() {
		updateCombo();

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
		_tblTasks.setModel(_model);
		
		_taskIdTField.setText("");

	}
}
