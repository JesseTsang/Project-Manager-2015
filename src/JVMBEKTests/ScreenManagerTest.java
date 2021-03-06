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
		User dummy = new User(0, "", "", UserRole.MANAGER);
		tester.setUser(dummy);
		assertEquals(dummy, tester.getProjectManager()); //Error: Probably because dummy is a User object and tester.getProjectManager returns a ProjectManager object.
		
	}

	@Test
	public void hasProjectManagerShouldReturnBoolean() {
		
		ScreenManager tester = new ScreenManager();
		User dummy = new User(0, "", "", UserRole.MANAGER);
		tester.setUser(dummy);
		assertEquals(true, tester.hasProjectManager());
		
	}

}
