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
	private JTable _tblTasks;
	private DefaultTableModel _model;
	
	public TaskScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	public void SetupGUI() {
		setLayout ( new BorderLayout());
		_lblProjectHeader = new JLabel();
		
		JButton backBtt = new JButton("Back");
		JButton addBtt = new JButton("Add");
		JButton delBtt = new JButton("Delete");

		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();
		
		_tblTasks = new JTable();
		_tblTasks.setPreferredScrollableViewportSize(new Dimension(350,150));
		_tblTasks.setFillsViewportHeight(true);	 
		
		
		JScrollPane scrollPane = new JScrollPane(_tblTasks);
		scrollPane.setOpaque(true);
		northPanel.add(_lblProjectHeader);
		centerPanel.add(scrollPane);
		southPanel.add(backBtt);
		southPanel.add(delBtt);
		southPanel.add(addBtt);
		
		
		add(BorderLayout.NORTH,northPanel);
		add(BorderLayout.CENTER,centerPanel);
		add(BorderLayout.SOUTH,southPanel);
		
		backBtt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				_manager.showAndResize(LoadScreen.IDENTIFIER, LoadScreen.WIDTH, LoadScreen.HEIGHT);
			}
	    });
		
		addBtt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				_manager.show(CreateTaskScreen.IDENTIFIER);
			}
	    });
		
		delBtt.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {
		    	int row = _tblTasks.getSelectedRow();
		    	
		        // check for selected row first
		        if (_tblTasks.getSelectedRow() != -1) {
		           //Delete the task from the database
		           Integer t_id = (Integer)_model.getValueAt(row, 0);
		           
		           _manager.getProjectManager().getSelectedProject().deleteTask(t_id);
		           
		            // remove selected row from the model
		           _model.removeRow(_tblTasks.getSelectedRow());
		        }
		    }
		});
	}

	@Override
	public void Update() {	
		_lblProjectHeader.setText("\"" + _manager.getProjectManager().getSelectedProject().getName() + "\" Tasks");
		
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
