package JVMBEK;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.util.ArrayList;


public class AddMemberScreen extends Screen{
	public final static String IDENTIFIER = "ADDMEMBERS";
	
	private JComboBox cmbMembers;
	private JTable _tblTasks;
	private DefaultTableModel _model;
	
	private JTextField _memberNameTField;
	private JTextField _taskIdTField;
	
	private JPanel centerPanel;
	
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
		
		//_memberNameTField = new JTextField(15);
		_taskIdTField = new JTextField(15);
		
		JButton btnAdd = new JButton("Assign");
		JButton btnCancel = new JButton("Cancel");
		
		_tblTasks = new JTable();
		_tblTasks.setPreferredScrollableViewportSize(new Dimension(350,150));
		_tblTasks.setFillsViewportHeight(true);

		JScrollPane scrollPane = new JScrollPane(_tblTasks);
		scrollPane.setOpaque(true);
		northPanel.add(scrollPane);
		
		cmbMembers = new JComboBox();

		centerPanel.add(taskId);
		centerPanel.add(_taskIdTField);
		centerPanel.add(memberName);
		//centerPanel.add(_memberNameTField);
		centerPanel.add(cmbMembers);
		centerPanel.setLayout(new GridLayout(2,2,5,5));		
		
		southPanel.add(btnAdd);
		southPanel.add(btnCancel);
		
		setLayout(new BorderLayout());
		add(BorderLayout.NORTH,northPanel);
		add(BorderLayout.CENTER,centerPanel);
		add(BorderLayout.SOUTH,southPanel);
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				_manager.show(ShowMembersScreen.IDENTIFIER);
			}
	    });
		
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
			
/*				if(_memberNameTField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null,
							  "Please enter the member's name.",
							  "Missing field",
							  JOptionPane.ERROR_MESSAGE);
						return;
				}*/
				
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
	
	private void updateCombo() {		
/*		if (cmbMembers != null) {
			_combo_panel.remove(cmbMembers);
		}
		if (_descr_label != null)
			_combo_panel.remove(_descr_label);*/
		
/*		_descr_label = new JLabel();*/

/*		cmbMembers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Project proj = (Project) cmbMembers.getSelectedItem();
				_manager.getProjectManager().setSelectedProject(proj.getId());
				String text = String.format(
						"<html><div WIDTH=%d>%s</div><html>", 80,
						proj.getDescription());
				_descr_label.setText(text);
			}
		});*/
/*		_combo_panel.add(_combo);
		_combo_panel.add(_descr_label);*/
		
		cmbMembers.removeAllItems();

		for(User u : _manager.getUsers()) {
			
			// Allow only members to get assigned?
			if(u.getRole() == UserRole.MEMBER) {
				cmbMembers.addItem(u.getUserName());
			}
		}
	}
	
	@Override
	public void Update() {
		updateCombo();
	
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
		
		//_memberNameTField.setText("");
		_taskIdTField.setText("");
		
	}
}
