package JVMBEK;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewScreen extends Screen {
	public static final String IDENTIFIER = "VIEW";
	public static final int WIDTH = 450;
	public static final int HEIGHT = 300;
	
	private JLabel _lblProjectHeader;
	private JTable _tblProjects;
	private DefaultTableModel _model;
	
	public ViewScreen(ScreenManager manager) {
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
		
		String[] columnNames = {"Task Name", "Project Name",
				"Start Date",
				"Due Date",
				"Description"};
		
		Object[][] data = new Object[][] { {"Sample Task", "Sample Project", "01/01/2015", "03/03/2015", "DefaultString"} };

/*		_model = new DefaultTableModel(data, columnNames);
		_tblTasks.setModel(_model);*/
		_tblProjects = new JTable(data, columnNames);
		_tblProjects.setPreferredScrollableViewportSize(new Dimension(350,150));
		JScrollPane scrollPane = new JScrollPane(_tblProjects);
		_tblProjects.setFillsViewportHeight(true);
		
		
		northPanel.add(lblAssignedTasks);
		centerPanel.add(scrollPane);
		southPanel.add(btnBack);
		
		add(BorderLayout.NORTH,northPanel);
		add(BorderLayout.CENTER,centerPanel);
		add(BorderLayout.SOUTH,southPanel);
	    
	    btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				_manager.showAndResize(MemberMainScreen.IDENTIFIER, MemberMainScreen.WIDTH, MemberMainScreen.HEIGHT);
			}
	    });
	}

	@Override
	public void Update() {
/*		_lblProjectHeader.setText("Test");
		
		String[] columnNames = {"Project Name",
				"Start Date",
				"Due Date",
				"Description"};
		
		Object[][] data = new Object[][] { {"Hello", "2", "dunno", "test"} };

		_model = new DefaultTableModel(data, columnNames);
		_tblTasks.setModel(_model);
		_tblProjects = new JTable(data, columnNames);
		_tblProjects.setPreferredScrollableViewportSize(new Dimension(350,150));
		_tblProjects.setFillsViewportHeight(true);*/
	}
}
