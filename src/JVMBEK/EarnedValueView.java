package JVMBEK;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class EarnedValueView extends Screen 
{
	public final static String IDENTIFIER = "EarnedValueView";
	public static final int WIDTH = 650;
	public static final int HEIGHT = 300;

	private JLabel lblProjectHeader;
	private JTable tblPert;
	private DefaultTableModel model;

	public EarnedValueView(ScreenManager manager) 
	{
		super(manager);
	}

	@Override
	public void SetupGUI() 
	{
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();

		lblProjectHeader = new JLabel();
		
		JButton btnBack = new JButton("Back");

		northPanel.add(lblProjectHeader);

		tblPert = new JTable();
		tblPert.setPreferredScrollableViewportSize(new Dimension(600, 150));
		tblPert.setFillsViewportHeight(true);
		tblPert.setEnabled(false);

		// Members are shown here
		JScrollPane scroll = new JScrollPane(tblPert);
		scroll.setOpaque(true);
		centerPanel.add(scroll);

		southPanel.add(btnBack);

		setLayout(new BorderLayout());
		add(BorderLayout.NORTH, northPanel);
		add(BorderLayout.CENTER, centerPanel);
		add(BorderLayout.SOUTH, southPanel);

		btnBack.addActionListener
		(new ActionListener() 
		{
			public void actionPerformed(ActionEvent ae) 
			{
				_manager.show(TaskScreen.IDENTIFIER);
			}
		});
	}

	@Override
	public void Update() 
	{
		String title = _manager.getProjectManager().getSelectedProject().getName().toUpperCase() + " Earned Value Analysis";
		lblProjectHeader.setText(title);

		String[] columnNames = { " Task Name ", " Planned Value - PV (BCWS) ", " Earned Value - EV (BCWP) ", " SV ", " CV ", " EAC ", " BAC ", " BAC " };

		ArrayList<Task> tasks = _manager.getProjectManager()
										.getSelectedProject().getTasks();
		
		Object[][] data = new Object[tasks.size()][];	

		for (int i = 0; i < tasks.size(); i++) 
		{
			Task t = tasks.get(i);
			
			data[i] = new Object[] { t.getName(), t.getDuration(), t.getOptimistic(), t.getPessimistic(), t.getEstimate(), t.getVariance() };
		}

		model = new DefaultTableModel(data, columnNames);
		tblPert.setModel(model);
	}
}