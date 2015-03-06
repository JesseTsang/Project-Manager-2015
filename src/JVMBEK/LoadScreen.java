package JVMBEK;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;

public class LoadScreen extends Screen {
	public static final String IDENTIFIER = "LOAD";
	public static final int WIDTH = 300;
	public static final int HEIGHT = 180;
	
	private JComboBox _combo;
	private JPanel _combo_panel;
	private JLabel _descr_label;
	
	public LoadScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	public void SetupGUI() {
		_combo_panel = new JPanel();

	    JButton btnReturn = new JButton("Cancel");
		JButton nameModify = new JButton("Modify ");
		JButton btnDelete = new JButton("Delete ");
	    JButton btnLoad = new JButton("Load");
	    JPanel col = new JPanel();
	    col.add(btnLoad);
	    col.add(btnReturn);
	    col.add(nameModify);
	    col.add(btnDelete);
	    setLayout(new GridLayout(0,2));
	    add(_combo_panel);
	    add(col);
	    
	    nameModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				_manager.show(ModifyScreen.IDENTIFIER);
			}
	    }); 
	    btnDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae)
				{
					 int dialogButton = JOptionPane.YES_NO_OPTION;
		             if ( JOptionPane.showConfirmDialog (null, "Do you really want to delete?","Warning",dialogButton) ==
		            		 JOptionPane.YES_OPTION) {
		            	 _manager.getProjectManager().deleteSelectedProject();
		            	 updateCombo();
		            	 if(_combo.getItemCount() <= 0)
		            		 _manager.show(ManagerMainScreen.IDENTIFIER);
		             }
	            }
		    }); 
	    
	    btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				_manager.showAndResize(TaskScreen.IDENTIFIER, TaskScreen.WIDTH, TaskScreen.HEIGHT);
			}
	    });
	    
	    btnReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				_manager.show(ManagerMainScreen.IDENTIFIER);
			}
	    });
	}

	private void updateCombo() {
		if(_combo != null) {
			_combo_panel.remove(_combo);
		}
		if(_descr_label != null)
			_combo_panel.remove(_descr_label);
		
		_combo = new JComboBox();
		_descr_label = new JLabel();
		
		_combo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				Project proj = (Project)_combo.getSelectedItem();
				_manager.getProjectManager().setSelectedProject(proj.getId());
				String text = String.format("<html><div WIDTH=%d>%s</div><html>", 80, proj.getDescription());
				_descr_label.setText(text);
			}
	    }); 
		_combo_panel.add(_combo);
		_combo_panel.add(_descr_label);
		
		for(Project proj : _manager.getProjectManager().getProjects().values()) {
			_combo.addItem(proj);
			
			if(!_manager.getProjectManager().hasSelected()) {
				_manager.getProjectManager().setSelectedProject(proj.getId());
			}
		}
		
		
		
	}

	@Override
	public void Update() {
		updateCombo();
	}
}
