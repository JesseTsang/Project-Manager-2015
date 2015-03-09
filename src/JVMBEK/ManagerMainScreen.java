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

public class ManagerMainScreen extends Screen {
	public final static String IDENTIFIER = "MANAGER_MAIN";
	private JButton btnLoad;
	
	public ManagerMainScreen(ScreenManager manager) {
		super(manager);
	}

//	@Override
	public void SetupGUI() {
		setLayout(new BorderLayout());
		
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();
		
		//This should maybe be in "UPDATE" instead
		String message = new String("Welcome, manager " +
		_manager.getUser().getUserName() + "!");
		 JLabel welcomeP = new JLabel(message);
		 centerPanel.add(welcomeP);
		 
		 JButton btnCreate = new JButton("Create Project");
		 btnLoad = new JButton("Load Project");
		 add(btnCreate);
		 add(btnLoad);
		 
		 southPanel.add(btnCreate);
		 southPanel.add(btnLoad);
		 southPanel.setLayout(new FlowLayout());
		 
		 add(BorderLayout.CENTER,centerPanel);
		 add(BorderLayout.SOUTH,southPanel);
	 
		 btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				_manager.show(LoadScreen.IDENTIFIER);
			}
	    });
	    
	    btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				_manager.showAndResize(CreateScreen.IDENTIFIER, CreateScreen.WIDTH, CreateScreen.HEIGHT);
			}
	    });
	}

	@Override
	public void Update() {
		btnLoad.setEnabled(_manager.getProjectManager().getProjects().size() > 0);
	}

}
