package JVMBEK;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class MemberTaskInformationScreen extends Screen {
	public final static String IDENTIFIER = "TASKINFO";
	public static final int WIDTH = 300;
	public static final int HEIGHT = 400;

	/*
	 * private JLabel lblProjectNameVal; private JLabel lblTaskIDVal; private
	 * JLabel lblTaskNameVal; private JLabel lblStartDateVal; private JLabel
	 * lblDueDateVal; private JLabel lblDurationVal; private JTextArea
	 * taDescription; private JLabel lblProgressVal;
	 */

	private JPanel northPanel, centerPanel;

	public MemberTaskInformationScreen(ScreenManager manager) {
		super(manager);
	}

	@Override
	public void SetupGUI() {
		northPanel = new JPanel();
		centerPanel = new JPanel();
		JPanel southPanel = new JPanel();

		JButton btnBack = new JButton("Back");

		southPanel.add(btnBack);
		southPanel.setLayout(new FlowLayout());

		setLayout(new BorderLayout());
		add(BorderLayout.NORTH, northPanel);
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, southPanel);

		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				_manager.showAndResize(MemberViewScreen.IDENTIFIER,
						MemberViewScreen.WIDTH, MemberViewScreen.HEIGHT);
			}
		});
	}

	@Override
	public void Update() {

		JLabel lblProjectName = new JLabel("Project Name:");
		JLabel lblProjectNameVal = new JLabel();
		JLabel lblTaskID = new JLabel("Task ID:");
		JLabel lblTaskIDVal = new JLabel();
		JLabel lblTaskName = new JLabel("Task Name:");
		JLabel lblTaskNameVal = new JLabel();
		JLabel lblStartDate = new JLabel("Start Date:");
		JLabel lblStartDateVal = new JLabel();
		JLabel lblDueDate = new JLabel("Due Date:");
		JLabel lblDueDateVal = new JLabel();
		JLabel lblDuration = new JLabel("Duration:");
		JLabel lblDurationVal = new JLabel();
		JLabel lblDescription = new JLabel("Description:");
		JTextArea taDescription = new JTextArea();
		JLabel lblProgress = new JLabel("Status:");
		final JComboBox cmbProgressVal = new JComboBox();
		cmbProgressVal.addItem("In Queue");
		cmbProgressVal.addItem("In Progress");
		cmbProgressVal.addItem("Finished");

		ArrayList<Task> tasks = Project.getAssignedTasks(_manager.getUser()
				.getId());

		DateFormat formatter = new SimpleDateFormat("d-MM-yyyy");
		formatter.setLenient(false);
		Calendar cal = Calendar.getInstance();
		String formattedStart, formattedEnd;
		Task temp = null;

		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getId() == Project.selectedTaskId) {
				temp = tasks.get(i);
				Task t = tasks.get(i);
				cal.setTime(t.getTaskStart());
				formattedStart = formatter.format(cal.getTime());
				cal.add(Calendar.DATE, t.getDuration());
				formattedEnd = formatter.format(cal.getTime());
				lblProjectNameVal = new JLabel(t.getProject().getName());
				lblTaskIDVal = new JLabel(Integer.toString(t.getId()));
				lblTaskNameVal = new JLabel(t.getName());
				lblStartDateVal = new JLabel(formattedStart);
				lblDueDateVal = new JLabel(formattedEnd);
				lblDurationVal = new JLabel(Integer.toString(t.getDuration()));
				taDescription = new JTextArea(t.getDescription());
				taDescription.setEditable(false);
				cmbProgressVal.setSelectedItem(Task.PROGRESS_STRINGS[t
						.getProgress().ordinal()]);
				break;
			}
		}

		final Task assignedTask = temp;

		cmbProgressVal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String strProgress = (String) cmbProgressVal.getSelectedItem();

				// Checking if the selection for the progress is the same as the
				// task's current progress status
				// Only do something if it is different
				if (!Task.PROGRESS_STRINGS[assignedTask.getProgress().ordinal()]
						.equals(strProgress)) {

					// Making sure that the previous tasks are done if starting
					// or finishing this task
					if (strProgress.equals("In Progress")
							|| strProgress.equals("Finished")) {
						ArrayList<Integer> precedingIds = assignedTask
								.getPrecedingIds();
						for (int i : precedingIds) {
							TaskProgress test = assignedTask.getProject()
									.getTaskById(i).getProgress();
							if (i != assignedTask.getId()
									&& test != TaskProgress.FINISHED) {
								// Can't start or finish a task if previous ones
								// are still in queue
								JOptionPane
										.showMessageDialog(
												null,
												"Cannot mark this task as \"In Progress\" or \"Finished\" because there "
														+ "is a preceding task that has not been completed.",
												"Invalid Selection",
												JOptionPane.ERROR_MESSAGE);
								return;
							}
						}
					}
					// Making sure that there are no following tasks that are in
					// progress or finished if setting the task back to
					// incomplete
					if (strProgress.equals("In Queue")
							|| strProgress.equals("In Progress")) {
						ArrayList<Integer> followingTasksIds = new ArrayList<Integer>();
						Statement stmt = null;
						try {
							stmt = DB.getInstance().createStatement();
							ResultSet id_set = stmt
									.executeQuery("SELECT task_id FROM task_sequence "
											+ "WHERE preceding_task =="
											+ Project.selectedTaskId);

							while (id_set.next()) {
								followingTasksIds.add(id_set.getInt("task_id"));
							}
						} catch (Exception e) {
							System.err.println(e.getClass().getName() + ": "
									+ e.getMessage());
							System.exit(0);
						}

						for (int i : followingTasksIds) {
							TaskProgress test = assignedTask.getProject()
									.getTaskById(i).getProgress();
							if (i != assignedTask.getId()
									&& test == TaskProgress.FINISHED) {
								// Can't set this task as "In Queue" if there
								// are following tasks
								// that are in progress or are finished
								JOptionPane
										.showMessageDialog(
												null,
												"Cannot mark this task as \"In Queue\" or \"In Progress\" because there "
														+ "is a following task that has already been started.",
												"Invalid Selection",
												JOptionPane.ERROR_MESSAGE);
								return;
							}
						}
					}

					assignedTask.setTaskProgress(strProgress);
					
					//TEST
					//System.out.println(assignedTask.getTotalPrecedingTasks());

					_manager.showAndResize(MemberViewScreen.IDENTIFIER,
							MemberViewScreen.WIDTH, MemberViewScreen.HEIGHT);
				}
			}
		});

		northPanel.removeAll();
		northPanel.add(lblProjectName);
		northPanel.add(lblProjectNameVal);
		northPanel.add(lblTaskID);
		northPanel.add(lblTaskIDVal);
		northPanel.add(lblTaskName);
		northPanel.add(lblTaskNameVal);
		northPanel.add(lblStartDate);
		northPanel.add(lblStartDateVal);
		northPanel.add(lblDueDate);
		northPanel.add(lblDueDateVal);
		northPanel.add(lblDuration);
		northPanel.add(lblDurationVal);
		northPanel.add(lblProgress);
		northPanel.add(cmbProgressVal);
		northPanel.setLayout(new GridLayout(7, 2, 0, 0));

		JScrollPane scroll = new JScrollPane(taDescription);
		scroll.setOpaque(true);
		// scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		// scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		centerPanel.removeAll();
		centerPanel.add(lblDescription);
		centerPanel.add(scroll);
		centerPanel.setLayout(new GridLayout(1, 1));
	}
}