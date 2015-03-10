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

public class CreateTaskScreen extends Screen {
	public final static String IDENTIFIER = "CREATETASK";
	private JTextField tfTaskName;
	private JTextArea taDescription;
	private JTextField tfDuration;

	public CreateTaskScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	public void SetupGUI() {
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();

		JLabel lblTaskName = new JLabel("Task Name:");
		JLabel lblDescription = new JLabel("Task Description:");
		JLabel lblDuration = new JLabel("Task Duration:");

		tfTaskName = new JTextField(10);
		taDescription = new JTextArea();
		JScrollPane scroll = new JScrollPane(taDescription);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		tfDuration = new JTextField(10);

		JButton btnCreate = new JButton("Create Task");
		JButton btnCancel = new JButton("Cancel");
		northPanel.add(lblTaskName);
		northPanel.add(tfTaskName);
		northPanel.add(lblDuration);
		northPanel.add(tfDuration);
		northPanel.setLayout(new GridLayout(2, 2, 5, 5));

		centerPanel.add(lblDescription);
		centerPanel.add(scroll);
		centerPanel.setLayout(new GridLayout(2, 1));

		southPanel.add(btnCreate);
		southPanel.add(btnCancel);
		southPanel.setLayout(new FlowLayout());
		;

		setLayout(new BorderLayout());
		add(BorderLayout.NORTH, northPanel);
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, southPanel);

		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (tfTaskName.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null,
							"Please enter a name for your task.",
							"Missing field", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (tfDuration.getText().isEmpty()
						|| !tfDuration.getText().matches("-?\\d+(\\.\\d+)?")) {
					JOptionPane.showMessageDialog(null,
							"Please enter a valid duration (# of days).",
							"Incorrect duration", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!Character.isLetter(tfTaskName.getText().charAt(0))) {
					JOptionPane.showMessageDialog(null,
							"Project name must begin with a letter.",
							"Incorrect naming", JOptionPane.ERROR_MESSAGE);
					return;
				}

				int dur = Integer.parseInt(tfDuration.getText());

				_manager.getProjectManager()
						.getSelectedProject()
						.addTask(tfTaskName.getText(), taDescription.getText(),
								dur);

				_manager.showAndResize(TaskScreen.IDENTIFIER, TaskScreen.WIDTH,
						TaskScreen.HEIGHT);
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				_manager.showAndResize(TaskScreen.IDENTIFIER, TaskScreen.WIDTH,
						TaskScreen.HEIGHT);
			}
		});
	}

	@Override
	public void Update() {
		tfTaskName.setText("");
		taDescription.setText("");
		tfDuration.setText("");
	}
}