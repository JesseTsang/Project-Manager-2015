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
    
    //Extract our task list and generalize it for JFreeChart TaskList (TaskSeries).
    public static TaskSeries getTaskSeries(ArrayList<JVMBEK.Task> tasks)
    {
    	final TaskSeries taskSeries = new TaskSeries("Scheduled");
	 	
    	for(JVMBEK.Task task : tasks) 
    	{
    		String taskName    = task.getName();
    		
    		Long startDateTime = task.getStartDate().getTime();
    		Long endDateTime   = task.getEndDate().getTime();
    		SimpleTimePeriod duration = new SimpleTimePeriod(startDateTime, endDateTime);
    		 		 			
    		taskSeries.add(new Task(taskName, duration));
    	}
    	
    	return taskSeries; 	
    }
    
    /**
     * Creates a sample dataset for a Gantt chart.
     *
     * @return The dataset.
     */
    public static IntervalCategoryDataset createDataset(ArrayList<JVMBEK.Task> tasks) {
    	
    	//final TaskSeries s1 = getTaskSeries(tasks);
    	//criticalPath = new CriticalPath(s1);
    	
    	criticalPath = new CriticalPath(tasks);
    	criticalPath.createPath();
    	ArrayList<JVMBEK.Task> criticalPathList = criticalPath.getCriticalPath();
    	
    	System.out.println("CriticalPathView.java - criticalPathList.size() : " + criticalPathList.size());
    	
    	final TaskSeries s2 = getTaskSeries(criticalPathList);
    	
    	 	  	 	
        final TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(s2);
    	
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