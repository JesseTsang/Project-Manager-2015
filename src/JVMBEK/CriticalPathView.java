package JVMBEK;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;

public class CriticalPathView extends JFrame 
{
	private ScreenManager screenManager;
	private Project loadedProject = null;
	private static CriticalPath criticalPath;

    /**
     * Creates a new chart.
     *
     * @param title  the frame title.
     */
    public CriticalPathView(final String title, ScreenManager manager) 
    {
        super(title);
        
        screenManager = manager;
        loadedProject = screenManager.getProjectManager().getSelectedProject();
        ArrayList<JVMBEK.Task> tasks = loadedProject.getTasks();

        final IntervalCategoryDataset dataset = createDataset(tasks);
        final JFreeChart chart = createChart(dataset, screenManager.getProjectManager().getSelectedProject().getName());

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }
    
    public static TaskSeries getTaskSeries(ArrayList<JVMBEK.Task> tasks)
    {
    	final TaskSeries s1 = new TaskSeries("Scheduled");
	 	
    	for(JVMBEK.Task t : tasks) 
    	{
    		String taskName    = t.getName();
    		
    		Long startDateTime = t.getStartDate().getTime();
    		Long endDateTime   = t.getEndDate().getTime();
    		SimpleTimePeriod duration = new SimpleTimePeriod(startDateTime, endDateTime);
    		 		
    		System.out.println("Getting Predecessor List for : " + taskName);
    		ArrayList<JVMBEK.Task> testList = t.getPredecessors();
    		
    		if (testList.size() != 0)
    		{
    			int k = 0;
    			for(JVMBEK.Task predecessorTask : testList)
        		{			
        			String predecessorTaskName = predecessorTask.getName();
        			
        			System.out.println("Predecessor " + k + " : " + predecessorTaskName);
        			k++;        			
        		}			
    		}
    			
    		s1.add(new Task(taskName, duration));
    	}
    	
    	return s1; 	
    }
    
    /**
     * Creates a sample dataset for a Gantt chart.
     *
     * @return The dataset.
     */
    public static IntervalCategoryDataset createDataset(ArrayList<JVMBEK.Task> tasks) {
    	
    	final TaskSeries s1 = getTaskSeries(tasks);
    	
    	criticalPath = new CriticalPath(s1);
    	criticalPath.createPath();
    	List testList = criticalPath.getCriticalPath();
    	
    	// iterate via "for loop"
    	System.out.println("List size: " + testList.size());
    	for (int i = 0; i < testList.size(); i++) 
    	{
    		System.out.println(i);
    		System.out.println(testList.get(i));
    	}
    	System.out.println("End loop");
    	
    	 	
        final TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(s1);
    	
        return collection;
    }

    /**
     * Utility method for creating <code>Date</code> objects.
     *
     * @param day  the date.
     * @param month  the month.
     * @param year  the year.
     *
     * @return a date.
     */
    private static Date date(final int day, final int month, final int year) 
    {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        final Date result = calendar.getTime();
        
        return result;
    }
        
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private JFreeChart createChart(final IntervalCategoryDataset dataset, String title) 
    {
        final JFreeChart chart = ChartFactory.createGanttChart
        (
            title + " ",  // chart title
            "Tasks",      // domain axis label
            "Dates",      // range axis label
            dataset,      // data
            true,         // include legend
            true,         // tooltips
            false         // urls
        );
        
        return chart;    
    }
}