package JVMBEK;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ShowMembersScreen extends Screen {
	public final static String IDENTIFIER = "SHOWMEMBERS";

	private JLabel lblProjectHeader;
	private JTable tblMembers;
	private DefaultTableModel model;

	private JTextField tfMemberName;
	private JTextField tfActivityId;

	public ShowMembersScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	public void SetupGUI() {
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();

		lblProjectHeader = new JLabel();
		JLabel lblMemberName = new JLabel("Member Name:");
		JLabel lblActivityId = new JLabel("Activity ID:");

		tfMemberName = new JTextField(15);
		tfActivityId = new JTextField(15);

		JButton btnAdd = new JButton("Assign Members");
		JButton btnCancel = new JButton("Cancel");

		northPanel.add(lblProjectHeader);

		tblMembers = new JTable();
		tblMembers.setPreferredScrollableViewportSize(new Dimension(350, 150));
		tblMembers.setFillsViewportHeight(true);
		tblMembers.setEnabled(false);

		// Members are shown here
		JScrollPane scroll = new JScrollPane(tblMembers);
		scroll.setOpaque(true);
//		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		centerPanel.add(scroll);

		southPanel.add(btnAdd);
		southPanel.add(btnCancel);

		setLayout(new BorderLayout());
		add(BorderLayout.NORTH, northPanel);
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, southPanel);

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				_manager.show(TaskScreen.IDENTIFIER);
			}
		});

		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				_manager.show(AddMemberScreen.IDENTIFIER);
			}
		});

	}

	@Override
	public void Update() {

		lblProjectHeader.setText(_manager.getProjectManager()
				.getSelectedProject().getName()
				+ " Members");

		String[] columnNames = { "Member ID", "Member Name", "Task ID" };

		ArrayList<User> members = new ArrayList();
		ArrayList<Task> tasks = new ArrayList();

		for (int i = 0; i < _manager.getProjectManager().getSelectedProject()
				.getTasks().size(); i++) {
			for (User u : _manager.getProjectManager().getSelectedProject()
					.getTasks().get(i).getAssignedMembers()) {
				tasks.add(_manager.getProjectManager().getSelectedProject()
						.getTasks().get(i));
				members.add(u);
			}
		}
		/*
		 * String[] columnNames = { "Member ID", "Member Name", // "Tasks",
		 * "Role"};
		 * 
		 * ArrayList<User> members = null;
		 * 
		 * for (int i=0;
		 * i<_manager.getProjectManager().getSelectedProject().getTasks
		 * ().size(); i++){
		 * members.addAll(_manager.getProjectManager().getSelectedProject
		 * ().getTasks().get(i).getMembers()); }
		 */
		Object[][] data = new Object[members.size()][];

		for (int i = 0; i < members.size(); i++) {
			User u = members.get(i);

			data[i] = new Object[] { u.getId(), u.getUserName(), u.getRole() };
			Task t = tasks.get(i);
			data[i] = new Object[] { u.getId(), u.getUserName(), t.getId() };
		}

		model = new DefaultTableModel(data, columnNames);
		tblMembers.setModel(model);
	}

}
