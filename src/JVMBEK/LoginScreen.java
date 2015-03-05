package JVMBEK;


import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class LoginScreen extends Screen {
	public final static String IDENTIFIER = "LOGIN";
	
	public LoginScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	public void SetupGUI() {
		
		String welcome = "Hello and Welcome! This is JVMBEK ver 1.0";
		String instruct = "Please enter your username and password";
/*separate into 3 parts, north, south and center; North is used for welcome, instruction.
  South is for login button.
  Center is for username, password, fields,....	
*/		
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();

		JLabel welcomeUser = new JLabel(welcome);
		JLabel instructUser = new JLabel(instruct);
		welcomeUser.setForeground(new Color(255,0,0));
		instructUser.setForeground(new Color(255,0,0));

		JLabel userName = new JLabel("Username");
		JLabel passWord = new JLabel("Password");
		
		final JTextField userField = new JTextField(10);
		final JPasswordField passField = new JPasswordField(10);
		
		JButton loginBt = new JButton("Log in");
		
		northPanel.add(welcomeUser);
		northPanel.add(instructUser);
		northPanel.setLayout(new GridLayout (2,1,1,1));
		
		
		centerPanel.add(userName);
		centerPanel.add(userField);
		centerPanel.add(passWord);
		centerPanel.add(passField);
		centerPanel.setLayout(new GridLayout(2,2));
		
		southPanel.add(loginBt);
		
		add(BorderLayout.NORTH,northPanel);
		add(BorderLayout.CENTER,centerPanel);
		add(BorderLayout.SOUTH,southPanel);
		
		loginBt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				String uid = userField.getText();
				String pw = new String(passField.getPassword());
				
				if(uid.isEmpty()) uid = "-1";
				
				
				User user = ProjectManager.login(uid, pw);
				if(user == null)
					return;
				else if(user.getRole() == UserRole.MANAGER) {
					ProjectManager pm = new ProjectManager(user);
					_manager.setProjectManager(pm);
				}
				else {
					
				}
				_manager.show(ManagerMainScreen.IDENTIFIER);
			}
		});    
	 
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub
		
	}

}
