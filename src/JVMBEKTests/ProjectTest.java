package JVMBEKTests;

import static org.junit.Assert.*;

import org.junit.Test;

import JVMBEK.Project;
import JVMBEK.ProjectManager;
import JVMBEK.User;
import JVMBEK.UserRole;

import java.util.Date;

//Project ID's: 11, 12, 13, 15

public class ProjectTest {

	@Test
	public void getIdShouldReturnInt() {
		
		User dummy = new User(1000, "", "", UserRole.MANAGER);
		User tester = ProjectManager.login("1000", "password");
		ProjectManager tester2 = new ProjectManager(tester);
		tester2.setSelectedProject(7);
		
		assertEquals(7, tester2.getSelectedProject().getId());
		
	}
	
	@Test
	public void getNameShouldReturnString() {
		
		Project tester = new Project(0, "test", "", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
		assertEquals("test", tester.getName());
		
	}
	
	@Test
	public void getDescriptionShouldReturnString() {
		
		Project tester = new Project(0, "", "test", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
		assertEquals("test", tester.getDescription());
		
	}
	
	@Test
	public void toStringShouldReturnString() {
		
		Project tester = new Project(0, "test", "", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
		assertEquals("test", tester.toString());
		
	}

}
