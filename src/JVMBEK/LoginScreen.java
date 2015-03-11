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

		String strWelcome = "Hello and welcome! This is JVMBEK v1.0";
		String strInstructions = "Please enter your username and password.";
		/*
		 * Separate into 3 parts, north, south and center; North is used for
		 * welcome, instruction. South is for login button. Center is for
		 * username, password, fields,....
		 */
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();

		JLabel lblWelcome = new JLabel(strWelcome);
		JLabel lblInstructions = new JLabel(strInstructions);
		lblWelcome.setForeground(new Color(255, 0, 0));
		lblInstructions.setForeground(new Color(255, 0, 0));

		JLabel lblUserName = new JLabel("Username:");
		JLabel lblPassword = new JLabel("Password:");

		final JTextField tfUserName = new JTextField(10);
		final JPasswordField pfPassword = new JPasswordField(10);

		JButton btnLogin = new JButton("Log in");

		northPanel.add(lblWelcome);
		northPanel.add(lblInstructions);
		northPanel.setLayout(new GridLayout(2, 1, 1, 1));

		centerPanel.add(lblUserName);
		centerPanel.add(tfUserName);
		centerPanel.add(lblPassword);
		centerPanel.add(pfPassword);
		centerPanel.setLayout(new GridLayout(2, 2));

		southPanel.add(btnLogin);

		add(BorderLayout.NORTH, northPanel);
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, southPanel);

		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String uid = tfUserName.getText();
				String pw = new String(pfPassword.getPassword());

				if (uid.isEmpty())
					uid = "-1";

				User user = ProjectManager.login(uid, pw);
				if (user == null)
					return;
				else {
					_manager.setUser(user);
				}
				if (user.getRole() == UserRole.MANAGER) {
					_manager.show(ManagerMainScreen.IDENTIFIER);
				} else {
					_manager.show(MemberMainScreen.IDENTIFIER);
				}
			}
		});

	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub

	}

}
