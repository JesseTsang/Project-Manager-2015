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

public class ShowMembersScreen extends Screen{
	public final static String IDENTIFIER = "SHOWMEMBERS";
	
	private JLabel _lblProjectHeader;
	private JTable _tblMembers;
	private DefaultTableModel _model;
	
	private JTextField _memberNameTField;
	private JTextField _activityIdTField;
	
	public ShowMembersScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	public void SetupGUI() {
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();
		
		_lblProjectHeader = new JLabel();
		JLabel memberName = new JLabel("Member Name:");
		JLabel activityId = new JLabel("Activity ID:");
		
		_memberNameTField = new JTextField(15);
		_activityIdTField = new JTextField(15);
		
		JButton btnAdd = new JButton("Assign Members");
		JButton btnCancel = new JButton("Cancel");
		
		northPanel.add(_lblProjectHeader);
		
		_tblMembers = new JTable();
		_tblMembers.setPreferredScrollableViewportSize(new Dimension(350,150));
		_tblMembers.setFillsViewportHeight(true);
		
		//Members are shown here
		JScrollPane scrollPane = new JScrollPane(_tblMembers);
		scrollPane.setOpaque(true);
		centerPanel.add(scrollPane);
		
		southPanel.add(btnAdd);
		southPanel.add(btnCancel);
		
		setLayout(new BorderLayout());
		add(BorderLayout.NORTH,northPanel);
		add(BorderLayout.CENTER,centerPanel);
		add(BorderLayout.SOUTH,southPanel);
		
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				_manager.show(TaskScreen.IDENTIFIER);
			}
	    });
		
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				_manager.show(AddMemberScreen.IDENTIFIER);
			}
	    });

	}
	
	@Override
	public void Update() {	
		
		_lblProjectHeader.setText(_manager.getProjectManager().getSelectedProject().getName().toUpperCase() + " Members");
			
		String[] columnNames = {
				"Member ID",
				"Member Name",
				"Tasks"};
		
		ArrayList<User> members = new ArrayList();
		ArrayList<Task> tasks = new ArrayList();
		
		for (int i = 0 ; i<_manager.getProjectManager().getSelectedProject().getTasks().size(); i++){
			for(User u : _manager.getProjectManager().getSelectedProject().getTasks().get(i).getAssignedMembers()) {
				tasks.add(_manager.getProjectManager().getSelectedProject().getTasks().get(i));
				members.add(u);
			}
		}
		
		Object[][] data = new Object[members.size()][];
		
		
		for(int i = 0; i < members.size(); i++ ) {
			User u = members.get(i);
			Task t = tasks.get(i);
			data[i] = new Object[] { u.getId(), u.getUserName(), t.getId() };
		}

		_model = new DefaultTableModel(data, columnNames);
		_tblMembers.setModel(_model);
	}

}
