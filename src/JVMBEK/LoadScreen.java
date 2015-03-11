package JVMBEK;

import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;

public class LoadScreen extends Screen {
	public static final String IDENTIFIER = "LOAD";
	public static final int WIDTH = 300;
	public static final int HEIGHT = 180;

	private JComboBox cmbProjects;
	private JPanel comboPanel;
	private JLabel lblDescription;
	private JLabel lblStartDate;

	public LoadScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	public void SetupGUI() {
		comboPanel = new JPanel();

		JButton btnLoad = new JButton("Load");
		JButton btnModify = new JButton("Modify");
		JButton btnDelete = new JButton("Delete");
		JButton btnBack = new JButton("Back");
		JPanel columnPanel = new JPanel();
		columnPanel.add(btnLoad);
		columnPanel.add(btnModify);
		columnPanel.add(btnDelete);
		columnPanel.add(btnBack);
		setLayout(new GridLayout(0, 2));
		add(comboPanel);
		add(columnPanel);

		btnModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				_manager.showAndResize(ModifyScreen.IDENTIFIER, ModifyScreen.WIDTH,
						ModifyScreen.HEIGHT);
			}
		});
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int dialogButton = JOptionPane.YES_NO_OPTION;
				if (JOptionPane.showConfirmDialog(null,
						"Do you really want to delete this project?",
						"Warning", dialogButton) == JOptionPane.YES_OPTION) {
					_manager.getProjectManager().deleteSelectedProject();
					updateCombo();
					if (cmbProjects.getItemCount() <= 0)
						_manager.show(ManagerMainScreen.IDENTIFIER);
				}
			}
		});

		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				_manager.showAndResize(TaskScreen.IDENTIFIER, TaskScreen.WIDTH,
						TaskScreen.HEIGHT);
			}
		});

		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				_manager.show(ManagerMainScreen.IDENTIFIER);
			}
		});
	}

	private void updateCombo() {
		if (cmbProjects != null)
			comboPanel.remove(cmbProjects);	
		
		if (lblDescription != null)
			comboPanel.remove(lblDescription);
		
		if (lblStartDate != null)
			comboPanel.remove(lblStartDate);

		cmbProjects = new JComboBox();
		lblDescription = new JLabel();
		lblStartDate = new JLabel();

		cmbProjects.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Project proj = (Project) cmbProjects.getSelectedItem();				
				_manager.getProjectManager().setSelectedProject(proj.getId());
				
				String descriptionText = String.format(
						"<html><div WIDTH=%d>%s</div><html>", 80,
						proj.getDescription());
				lblDescription.setText(descriptionText);

				String startDateText = String.format(
						"<html><div WIDTH=%d>%s</div><html>", 80,
						"Start Date: "+proj.getStartDateString());
				lblStartDate.setText(startDateText);
				}
		});
		
		comboPanel.add(cmbProjects);
		comboPanel.add(lblDescription);
		comboPanel.add(lblStartDate);


		for (Project proj : _manager.getProjectManager().getProjects().values()) {
			cmbProjects.addItem(proj);

			if (!_manager.getProjectManager().hasSelected()) {
				_manager.getProjectManager().setSelectedProject(proj.getId());
			}
		}

	}

	@Override
	public void Update() {
		updateCombo();
	}
}
