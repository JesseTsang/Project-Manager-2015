package JVMBEK;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class CreateTaskScreen extends Screen {
	public final static String IDENTIFIER = "CREATETASK";
	private JTextField _nameTField;
	private JTextField _descTField;
	private JTextField _durTField;

	public CreateTaskScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	public void SetupGUI() {
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();
		
		JLabel pjName = new JLabel("Task Name: ");
		JLabel pjDis  = new JLabel("Task Description: ");
		JLabel pjDur = new JLabel("Task Duration: ");
		
		_nameTField = new JTextField(10);
		_descTField = new JTextField(30);
		_durTField = new JTextField(10);
		
		JButton createBtt = new JButton("Create Task");
		JButton cancelBtt = new JButton("Cancel ");
		northPanel.add(pjName);
		northPanel.add(_nameTField);
		northPanel.add(pjDur);
		northPanel.add(_durTField);
		northPanel.setLayout(new GridLayout(2,2,5,5));
		
		centerPanel.add(pjDis);
		centerPanel.add(_descTField);
		centerPanel.setLayout(new GridLayout(2,1));
		
		southPanel.add(createBtt);
		southPanel.add(cancelBtt);
		southPanel.setLayout(new FlowLayout());;
		
		setLayout(new BorderLayout());
		add(BorderLayout.NORTH,northPanel);
		add(BorderLayout.CENTER,centerPanel);
		add(BorderLayout.SOUTH,southPanel);
		

		createBtt.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae){
					if(_nameTField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null,
								  "Please enter a name for your project.",
								  "Missing field",
								  JOptionPane.ERROR_MESSAGE);
							return;
					}
					
					if(_durTField.getText().isEmpty() || !_durTField.getText().matches("-?\\d+(\\.\\d+)?")) {
						JOptionPane.showMessageDialog(null,
								  "Please enter a valid duration (# of days).",
								  "Incorrect duration",
								  JOptionPane.ERROR_MESSAGE);
							return;
					}
					
					if(!Character.isLetter(_nameTField.getText().charAt(0))) {
						JOptionPane.showMessageDialog(null,
								  "Project name must begin with a letter.",
								  "Incorrect naming",
								  JOptionPane.ERROR_MESSAGE);
							return;
					}
					
					int dur = Integer.parseInt(_durTField.getText());
					
					_manager.getProjectManager()
						.getSelectedProject()
						.addTask(_nameTField.getText(), _descTField.getText(), dur);
					
					_manager.showAndResize(TaskScreen.IDENTIFIER,  TaskScreen.WIDTH, TaskScreen.HEIGHT);
				}
			});
		 
		 cancelBtt.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae){
					_manager.showAndResize(TaskScreen.IDENTIFIER, TaskScreen.WIDTH, TaskScreen.HEIGHT);
				}
			});
		}

		@Override
		public void Update() {	
			_nameTField.setText("");
			_descTField.setText("");
			_durTField.setText("");
		}
	}