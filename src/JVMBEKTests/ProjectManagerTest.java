package JVMBEKTests;

import JVMBEK.*;
import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

// Project ID's: 11, 12, 13, 15

public class ProjectManagerTest {
	
	@Test
	public void testLogin() {
		
		User dummy = new User(1000, "", "", UserRole.MANAGER);
		User tester = ProjectManager.login("1000", "password");
		
		assertEquals("Benjamin Slapcoff", tester.getUserName());

	}
	
	@Test
	public void getProjectsShouldReturnMaps() {
		
		ProjectManager tester = new ProjectManager(new User(0, "", "", UserRole.MANAGER));
		System.out.println(tester.getProjects().get(1));
		
		assertEquals(new HashMap<Integer, Project>(), tester.getProjects());
	}
	
	@Test
	public void hasSelectedShouldReturnTrue() {
		
		ProjectManager tester = new ProjectManager(new User(10, "", "", UserRole.MANAGER));
		assertEquals(false, tester.hasSelected());
		
	}

}
