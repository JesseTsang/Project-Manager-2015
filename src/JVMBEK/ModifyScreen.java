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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ModifyScreen extends Screen {
	public final static String IDENTIFIER = "MODIFY";
	public static final int WIDTH = 350;
	public static final int HEIGHT = 400;
	
	private JTextField tfProjectName;
	private JTextArea taDescription;
	//private JTextField _durField;

	public ModifyScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	public void SetupGUI() {
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();
		
		JLabel lblProjectName = new JLabel("Project Name:");
		JLabel lblDescription = new JLabel("Project Description:");
		
		tfProjectName = new JTextField(10);
		taDescription = new JTextArea();
		JScrollPane scroll = new JScrollPane(taDescription);
		scroll.setOpaque(true);
//		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		JButton btnUpdate = new JButton("Update Project");
		JButton btnCancel = new JButton("Cancel");
		northPanel.add(lblProjectName);
		northPanel.add(tfProjectName);
		northPanel.setLayout(new GridLayout(2,2,5,5));
		
		centerPanel.add(lblDescription);
		centerPanel.add(scroll);
		centerPanel.setLayout(new GridLayout(2,1));
		
		southPanel.add(btnUpdate);
		southPanel.add(btnCancel);
		southPanel.setLayout(new FlowLayout());;
		
		setLayout(new BorderLayout());
		add(BorderLayout.NORTH,northPanel);
		add(BorderLayout.CENTER,centerPanel);
		add(BorderLayout.SOUTH,southPanel);
		
		 btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				if(tfProjectName.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null,
							  "Please enter a name for your project.",
							  "Missing field",
							  JOptionPane.ERROR_MESSAGE);
						return;
				}
				
				if(!Character.isLetter(tfProjectName.getText().charAt(0))) {
					JOptionPane.showMessageDialog(null,
							  "Project name must begin with a letter.",
							  "Incorrect naming",
							  JOptionPane.ERROR_MESSAGE);
						return;
				}
				_manager.getProjectManager().updateProjectName(tfProjectName.getText());
				_manager.getProjectManager().updateProjectDescription(taDescription.getText());
				_manager.showAndResize(LoadScreen.IDENTIFIER, 300, 180);
			}
		 });
		 
		 btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				_manager.showAndResize(LoadScreen.IDENTIFIER, 300, 180);
			}
		 });
	}

	
	@Override
	public void Update() {
		tfProjectName.setText(_manager.getProjectManager().getSelectedProject().getName());
		taDescription.setText(_manager.getProjectManager().getSelectedProject().getDescription());
	}

}
