package JVMBEK;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils 
{

	public DateUtils() 
	{

	}
	
	public static String getDateString(Date date)
	{
		String dateString = new SimpleDateFormat("dd-MM-yyyy").format(date);
		
		return dateString;
	}
	
	public static long getDuration(Date startDate, Date endDate)
	{	
		// turn the Date objects into Calendar objects
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);  
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		
		// Count from the start date to the end date to calculate duration
		Calendar date = (Calendar) startCal.clone();  
		
		long daysBetween = 0;  
		
		while (date.before(endCal)) 
		{  
			date.add(Calendar.DAY_OF_MONTH, 1);  
			daysBetween++;  
		}  
	
		return daysBetween; 
	}

}
