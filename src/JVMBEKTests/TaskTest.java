package JVMBEKTests;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import JVMBEK.Task;
import JVMBEK.TaskProgress;


public class TaskTest {
	
	@Test
	public void getIdShouldReturnInt() {
		
		Task tester = new Task(1, "", "", TaskProgress.FINISHED, new Date(System.currentTimeMillis()), 1);
		assertEquals(1, tester.getId());
		
	}
	
	@Test
	public void getNameShouldReturnString() {
		
		Task tester = new Task(0, "test", "", TaskProgress.FINISHED, new Date(System.currentTimeMillis()), 1);
		assertEquals("test", tester.getName());
		
	}
	
	@Test
	public void getDescriptionShouldReturnString() {
		
		Task tester = new Task(0, "", "test", TaskProgress.FINISHED, new Date(System.currentTimeMillis()), 1);
		assertEquals("test", tester.getDescription());
		
	}

}
