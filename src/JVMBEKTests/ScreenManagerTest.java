package JVMBEKTests;

import static org.junit.Assert.*;

import org.junit.Test;

import JVMBEK.ProjectManager;
import JVMBEK.ScreenManager;
import JVMBEK.User;
import JVMBEK.UserRole;


public class ScreenManagerTest {
	
	@Test
	public void getProjectManagerShouldReturnProjectManager() {
		
		ScreenManager tester = new ScreenManager();
		ProjectManager dummy = new ProjectManager(new User(0, "", "", UserRole.MANAGER));
		tester.setProjectManager(dummy);
		assertEquals(dummy, tester.getProjectManager());
		
	}

	@Test
	public void hasProjectManagerShouldReturnBoolean() {
		
		ScreenManager tester = new ScreenManager();
		ProjectManager dummy = new ProjectManager(new User(0, "", "", UserRole.MANAGER));
		tester.setProjectManager(dummy);
		assertEquals(true, tester.hasProjectManager());
		
	}

}
