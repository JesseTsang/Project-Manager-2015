package JVMBEK;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MemberMainScreen extends Screen {
	public final static String IDENTIFIER = "MEMBER_MAIN";
	public static final int WIDTH = 300;
	public static final int HEIGHT = 180;

	public MemberMainScreen(ScreenManager manager) {
		super(manager);
	}

	// @Override
	public void SetupGUI() {
		setLayout(new BorderLayout());

		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();

		// This should perhaps be in "UPDATE" instead
		String strWelcome = new String("Welcome, member "
				+ _manager.getUser().getUserName() + "!");
		JLabel lblWelcome = new JLabel(strWelcome);
		centerPanel.add(lblWelcome);

		JButton btnView = new JButton("View Assigned Tasks");
		add(btnView);

		southPanel.add(btnView);
		southPanel.setLayout(new FlowLayout());

		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, southPanel);

		btnView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				_manager.showAndResize(MemberViewScreen.IDENTIFIER,
						MemberViewScreen.WIDTH, MemberViewScreen.HEIGHT);
			}
		});
	}

	@Override
	public void Update() {
	}

}
