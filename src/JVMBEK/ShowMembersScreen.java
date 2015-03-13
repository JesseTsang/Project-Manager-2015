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
	public static final int WIDTH = 400;
	public static final int HEIGHT = 300;

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

		JButton btnAdd = new JButton("Assign Member");
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

		//We first get the list of tasks from the project
		for (int i = 0; i < _manager.getProjectManager().getSelectedProject().getTasks().size(); i++)
		{
			//For each task, we have a list of assigned members
			for (User u : _manager.getProjectManager().getSelectedProject().getTasks().get(i).getAssignedMembers())
			{
				//tasks (ArrayList) will end up getting all the tasks
				//members (ArrayList) will end up getting all the members with assignment (ID + Names)
				tasks.add(_manager.getProjectManager().getSelectedProject().getTasks().get(i));
				members.add(u);
			}
		}
		
		Object[][] data = new Object[members.size()][]; //Why is multidimensional array needed?

		for (int i = 0; i < members.size(); i++) {
			User u = members.get(i);

			data[i] = new Object[] { u.getId(), u.getUserName(), u.getRole() }; //What's the point of this line?
			Task t = tasks.get(i);
			data[i] = new Object[] { u.getId(), u.getUserName(), t.getId() };
		}

		model = new DefaultTableModel(data, columnNames);
		tblMembers.setModel(model);
	}

}
