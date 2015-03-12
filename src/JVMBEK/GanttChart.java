package JVMBEK;

/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * ---------------
 * GanttDemo1.java
 * ---------------
 * (C) Copyright 2002-2004, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: GanttDemo1.java,v 1.12 2004/04/26 19:11:54 taqua Exp $
 *
 * Changes
 * -------
 * 06-Jun-2002 : Version 1 (DG);
 * 10-Oct-2002 : Modified to use DemoDatasetFactory (DG);
 * 10-Jan-2003 : Renamed GanttDemo --> GanttDemo1 (DG);
 * 16-Oct-2003 : Shifted dataset from DemoDatasetFactory to this class (DG);
 *
 */



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;

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

// Slightly modified version of David Gilbert's JFreeChart
public class GanttChart extends JFrame {
	
	private ScreenManager screenManager;
	private Project loadedProject = null;

    /**
     * Creates a new chart.
     *
     * @param title  the frame title.
     */
    public GanttChart(final String title, ScreenManager manager) {
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
    
    /**
     * Creates a sample dataset for a Gantt chart.
     *
     * @return The dataset.
     */
    public static IntervalCategoryDataset createDataset(ArrayList<JVMBEK.Task> tasks) {
    	
    	final TaskSeries s1 = new TaskSeries("Scheduled");
    	for(JVMBEK.Task t : tasks) {
    		// "getDate" methods don't actually exist yet
/*    		s1.add(new Task(t.getName(),
    	               new SimpleTimePeriod(t.getStartDate().getTime(), 
    	            		   t.getEndDate().getTime())));*/
    	}
    	
/*    	final TaskSeries s2 = new TaskSeries("Actual");
    	for(JVMBEK.Task t : tasks) {
    		// "getDate" methods don't actually exist yet
    		s1.add(new Task(t.getName(),
    	               new SimpleTimePeriod(t.getStartDate(), 
    	            		   t.getEndDate())));
    	}*/
    	
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
    private static Date date(final int day, final int month, final int year) {

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
    private JFreeChart createChart(final IntervalCategoryDataset dataset, String title) {
        final JFreeChart chart = ChartFactory.createGanttChart(
            title,  // chart title
            "Tasks",              // domain axis label
            "Dates",              // range axis label
            dataset,             // data
            true,                // include legend
            true,                // tooltips
            false                // urls
        );    
//        chart.getCategoryPlot().getDomainAxis().setMaxCategoryLabelWidthRatio(10.0f);
        return chart;    
    }
}