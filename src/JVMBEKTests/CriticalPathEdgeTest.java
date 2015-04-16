package JVMBEKTests;

import static org.junit.Assert.*;



import org.junit.Test;

import JVMBEK.ProjectManager;
import JVMBEK.ScreenManager;
import JVMBEK.User;
import JVMBEK.UserRole;
import JVMBEK.CriticalPathEdge;
import JVMBEK.CriticalPathNode;
import JVMBEK.Task;
public class CriticalPathEdgeTest {

@Test
public void equalTest(){
	Task dummy = new Task();
	CriticalPathEdge test = new CriticalPathEdge(dummy);
	assertEquals(true,test.equals(test));
}

}
