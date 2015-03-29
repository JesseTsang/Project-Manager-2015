package JVMBEKTests;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import JVMBEK.Task;
import JVMBEK.TaskProgress;


public class TaskTest {
	
	@Test
	public void getIdShouldReturnInt() {
		
		Task tester = new Task(1, "", "", TaskProgress.FINISHED, 1);
		assertEquals(1, tester.getId());
		
	}
	
	@Test
	public void getNameShouldReturnString() {
		
		Task tester = new Task(0, "test", "", TaskProgress.FINISHED, 1);
		assertEquals("test", tester.getName());
		
	}
	
	@Test
	public void getDescriptionShouldReturnString() {
		
		Task tester = new Task(0, "", "test", TaskProgress.FINISHED, 1);
		assertEquals("test", tester.getDescription());
		
	}
	
	@Test
	public void getDurationTest() throws ParseException 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
		String dateInString1 = "28/03/2015";
		String dateInString2 = "30/03/2015";
		Date date1 = sdf.parse(dateInString1);
		Date date2 = sdf.parse(dateInString2);
		
		//Task(int ID, String name, String description, TaskProgress progress, Date startDate, Date endDate)
		Task tester = new Task(0, "", "test", TaskProgress.IN_PROGRESS, date1, date2);
		assertEquals(2, tester.getDuration());	
	}

}
