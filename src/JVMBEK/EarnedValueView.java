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
	public static final int WIDTH = 1463;//650
	public static final int HEIGHT = 400;

	private JLabel lblProjectHeader;
	private JTable tblEVA;
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

		tblEVA = new JTable();
		tblEVA.setPreferredScrollableViewportSize(new Dimension(1330, 250));
		tblEVA.setFillsViewportHeight(true);
		tblEVA.setEnabled(false);

		// Members are shown here
		JScrollPane scroll = new JScrollPane(tblEVA);
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

		String[] columnNames = { " Task Name ", " PV ", " EV ", " AC ", " BAC ", " SV ", " CV ", " CPI ", " SPI ", " ETC ", " VAC " };

		ArrayList<Task> tasksList = _manager.getProjectManager().getSelectedProject().getTasks();

		_manager.getProjectManager().getSelectedProject().earnedValueAnalysis();
		
		int pv;
		int ev;
		int ac;
		int bac = _manager.getProjectManager().getSelectedProject().getBudgetAtCompletion();	
		int sv;
		int cv;
		int cpi;
		int spi;
		int eac;
		int etc;
		int vac;
		
			
		Object[][] data = new Object[tasksList.size()][];	

		for (int i = 0; i < tasksList.size(); i++) 
		{
			Task t = tasksList.get(i);
			
			pv = t.getPlannedValue();
			ev = t.getEarnedValue();
			ac = t.getActualCost();
			sv = t.getScheduleVariance();
			cv = t.getCostVariance();
			cpi = t.getCostPerformanceIndex();
			spi = t.getSchedulePerformanceIndex();
			eac = t.getEstimateAtCompletion();
			etc = t.getEstimateToComplete();
			vac = t.getVarianceAtCompletion();
			
			data[i] = new Object[] { t.getName(), pv, ev, ac, bac, sv, cv, cpi, spi, eac, etc, vac};
		}

		model = new DefaultTableModel(data, columnNames);
		tblEVA.setModel(model);
	}

}