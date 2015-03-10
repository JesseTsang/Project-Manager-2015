package JVMBEK;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CreateScreen extends Screen {
	public final static String IDENTIFIER = "CREATE";
	public final static int WIDTH = 350;
	public final static int HEIGHT = 300;

	private JTextField textFieldProjectName;
	private JTextArea textAreaDescription;

	private JTextField textFieldProjectStartDate;	//in dd-MM-yyyy

	public CreateScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	public void SetupGUI() {
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();

		JLabel lblProjectName = new JLabel("Project Name:");
		JLabel lblDescription = new JLabel("Project Description:");
		JLabel lblProjectStartDate = new JLabel("Start Date (DD-MM-YYYYY): ");

		textFieldProjectName = new JTextField(10);
		textAreaDescription = new JTextArea();
		textFieldProjectStartDate = new JTextField();
		
		JScrollPane scroll = new JScrollPane(textAreaDescription);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JButton btnCreate = new JButton("Create Project");
		JButton btnCancel = new JButton("Cancel");
		northPanel.add(lblProjectName);
		northPanel.add(textFieldProjectName);		
		northPanel.add(lblProjectStartDate);
		northPanel.add(textFieldProjectStartDate);
		// northPanel.add(pjDur);
		// northPanel.add(_durField);
		northPanel.setLayout(new GridLayout(2, 2, 5, 5));

		centerPanel.add(lblDescription);
		centerPanel.add(scroll);
		centerPanel.setLayout(new GridLayout(2, 1));

		southPanel.add(btnCreate);
		southPanel.add(btnCancel);
		southPanel.setLayout(new FlowLayout());
		
		setLayout(new BorderLayout());
		add(BorderLayout.NORTH, northPanel);
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, southPanel);

		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (textFieldProjectName.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null,
							"Please enter a name for your project.",
							"Missing field", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!Character.isLetter(textFieldProjectName.getText().charAt(0))) {
					JOptionPane.showMessageDialog(null,
							"Project name must begin with a letter.",
							"Incorrect naming", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				DateFormat formater = new SimpleDateFormat("d-MM-yyyy"/*, Locale.ENGLISH*/);
				formater.setLenient(false);
				Calendar cal = Calendar.getInstance();
				String startDateString = textFieldProjectStartDate.getText();
				Date startDateEntry;
				
				if(!textFieldProjectStartDate.getText().isEmpty()){
					try {
					    cal.setTime(formater.parse(startDateString));
					    startDateEntry = formater.parse(startDateString);
					}
					catch (Exception e) {
						JOptionPane.showMessageDialog(null,
								"Project start date must be entered in the following format: DD-MM-YYYY.",
								"Incorrect date format", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}else{
					JOptionPane.showMessageDialog(null,
							"Please enter a project start date.",
							"Missing Field", JOptionPane.ERROR_MESSAGE);
					return;
				}
					
				_manager.getProjectManager().addNewProject(
						textFieldProjectName.getText(), textAreaDescription.getText(), startDateEntry);
				_manager.showAndResize(ManagerMainScreen.IDENTIFIER, 300, 180);
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				_manager.showAndResize(ManagerMainScreen.IDENTIFIER, 300, 180);
			}
		});
	}

	@Override
	public void Update() {
		textFieldProjectName.setText("");
		textAreaDescription.setText("");
		// _durField.setText("");
	}

}
