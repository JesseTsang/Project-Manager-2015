package JVMBEKTests;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import JVMBEK.User;
import JVMBEK.UserRole;


public class UserTest {

	@Test
	public void getIdShouldReturnInt() {
		
		User tester = new User(1, "", "", UserRole.MANAGER);
		assertEquals(1, tester.getId());
		
	}
	
	@Test
	public void getUserNameShouldReturnFirstAndLastNames() {
		
		User tester = new User(0, "test", "test", UserRole.MANAGER);
		assertEquals("test test", tester.getUserName());
		
	}

}
