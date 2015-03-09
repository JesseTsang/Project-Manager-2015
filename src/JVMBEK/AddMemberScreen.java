package JVMBEK;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.util.ArrayList;


public class AddMemberScreen extends Screen{
	public final static String IDENTIFIER = "ADDMEMBERS";
	
	private JTable _tblTasks;
	private DefaultTableModel _model;
	
	private JTextField _memberNameTField;
	private JTextField _taskIdTField;
	
	public AddMemberScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	public void SetupGUI() {
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();
			
		JLabel memberName = new JLabel("Member Name: ");
		JLabel taskId = new JLabel("Task ID: ");
		
		_memberNameTField = new JTextField(15);
		_taskIdTField = new JTextField(15);
		
		JButton addMemberButt = new JButton("Add");
		JButton backBtt = new JButton("Cancel");
		
		_tblTasks = new JTable();
		_tblTasks.setPreferredScrollableViewportSize(new Dimension(350,150));
		_tblTasks.setFillsViewportHeight(true);

		JScrollPane scrollPane = new JScrollPane(_tblTasks);
		scrollPane.setOpaque(true);
		northPanel.add(scrollPane);

		centerPanel.add(memberName);
		centerPanel.add(_memberNameTField);
		centerPanel.add(taskId);
		centerPanel.add(_taskIdTField);
		centerPanel.setLayout(new GridLayout(2,2,5,5));		
		
		southPanel.add(backBtt);
		southPanel.add(addMemberButt);
		
		setLayout(new BorderLayout());
		add(BorderLayout.NORTH,northPanel);
		add(BorderLayout.CENTER,centerPanel);
		add(BorderLayout.SOUTH,southPanel);

			
		backBtt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				_manager.show(ShowMembersScreen.IDENTIFIER);
			}
	    });
		
		
		addMemberButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
			
				if(_memberNameTField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null,
							  "Please enter the member's name.",
							  "Missing field",
							  JOptionPane.ERROR_MESSAGE);
						return;
				}
				
				if(_taskIdTField.getText().isEmpty() || !_taskIdTField.getText().matches("-?\\d+")) {
					JOptionPane.showMessageDialog(null,
							  "Please enter a valid ID number.",
							  "Incorrect ID",
							  JOptionPane.ERROR_MESSAGE);
						return;
				}
				
				
				
				ArrayList<Task> tasks =  _manager.getProjectManager().getSelectedProject().getTasks();
				int[] taskIds = new int[tasks.size()];
				
				int taskId = -1;
				
				for(int i = 0; i < tasks.size(); i++ ) {
					Task t = tasks.get(i);
					if(Integer.parseInt(_taskIdTField.getText()) == t.getId()){
						taskId = Integer.parseInt(_taskIdTField.getText());
					}
				}
				
				if (taskId == -1){
					JOptionPane.showMessageDialog(null,
							  "Please enter a valid ID number.",
							  "Incorrect ID",
							  JOptionPane.ERROR_MESSAGE);
						return;
				}
				
				
//				_manager.getProjectManager()
//					.getSelectedProject()
//					.addMember(_memberNameTField.getText(), taskId);
				
				_manager.show(ShowMembersScreen.IDENTIFIER);
			}
		});



	}
	
	@Override
	public void Update() {	
//		_nameTField.setText("");
//		_descTField.setText("");
//		_durTField.setText("");
		
		String[] columnNames = {"Task ID",
				"Task Name",
				"Description",
				"Progress"};
		
		ArrayList<Task> tasks =  _manager.getProjectManager().getSelectedProject().getTasks();
		Object[][] data = new Object[tasks.size()][];
		
		for(int i = 0; i < tasks.size(); i++ ) {
			Task t = tasks.get(i);
			data[i] = new Object[] { t.getId(), t.getName(), t.getDescription(), Task.PROGRESS_STRINGS[t.getProgress().ordinal()] };
		}

		_model = new DefaultTableModel(data, columnNames);
		_tblTasks.setModel(_model);
		
	}

}
