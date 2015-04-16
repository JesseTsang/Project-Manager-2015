package JVMBEKTests;


import static org.junit.Assert.*;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import org.junit.Test;
import JVMBEK.ProjectManager;
import JVMBEK.ScreenManager;
import JVMBEK.User;
import JVMBEK.GanttChart;
public class GanttTest {
	@Test
	public void GanttChartShouldNotBeNull() 
	{	ScreenManager manager = new ScreenManager();
		GanttChart a = new GanttChart();
		assertNotNull(a);
	}
	@Test
	//test if createDataSet create a TaskSeriesCollection Object
	//failed because they do not point to the same Object.
	public void TestcreateDataSet(){
		  TaskSeriesCollection dummy = new TaskSeriesCollection();
		  ArrayList<JVMBEK.Task> tasks = new ArrayList<JVMBEK.Task>();
		  GanttChart test = new GanttChart();
		  IntervalCategoryDataset dummy_dataset = test.createDataset(tasks);
		  TaskSeriesCollection collection = new TaskSeriesCollection();
		  assertEquals(dummy_dataset,collection);
	}	

	}
