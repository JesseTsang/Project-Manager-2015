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
	public final static String IDENTIFIER = "MAIN";
	private JButton _btnLoad;
	
	public ManagerMainScreen(ScreenManager manager) {
		super(manager);
	}

//	@Override
	public void SetupGUI() {
		setLayout(new BorderLayout());
		
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();
		
		//This should maybe be in "UPDATE" instead
		String message = new String("Welcome " +
		_manager.getProjectManager().getOwner().getUserName() + "!");
		 JLabel welcomeP = new JLabel(message);
		 centerPanel.add(welcomeP);
		 
		 
		 JButton createBtt = new JButton("Create");
		 _btnLoad = new JButton("Load");
		 add(createBtt);
		 add(_btnLoad);
		 
		 southPanel.add(createBtt);
		 southPanel.add(_btnLoad);
		 southPanel.setLayout(new FlowLayout());
		 
		 add(BorderLayout.CENTER,centerPanel);
		 add(BorderLayout.SOUTH,southPanel);
	 
		 _btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				_manager.show(LoadScreen.IDENTIFIER);
			}
	    });
	    
	    createBtt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				_manager.showAndResize(CreateScreen.IDENTIFIER, CreateScreen.WIDTH, CreateScreen.HEIGHT);
			}
	    });
	}

	@Override
	public void Update() {
		_btnLoad.setEnabled(_manager.getProjectManager().getProjects().size() > 0);
	}

}
