package JVMBEKTests;

import static org.junit.Assert.*;

import org.junit.Test;

import JVMBEK.Project;
import JVMBEK.ProjectManager;
import JVMBEK.Task;
import JVMBEK.User;
import JVMBEK.UserRole;

import java.util.ArrayList;
import java.util.Date;

//Project ID's: 11, 12, 13, 15

public class ProjectTest {

	@Test
	public void getIdShouldReturnInt() {
		
		User dummy = new User(1000, "", "", UserRole.MANAGER);
		User tester = ProjectManager.login("1000", "password");
		ProjectManager tester2 = new ProjectManager(tester);
		tester2.setSelectedProject(7); //Error: Project 10 belongs to 1002. So we can't create dummy project, the must be pre-existed.
		
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
	
	/*Iteration 2 Tests*/
	
	//US3 Test Idea: If we assign person1 to task1, then task1 should have person1.
	@Test
	public void assignResourseTest()
	{	
		User testerManager = ProjectManager.login("1002", "password");		
		ProjectManager testerProgram = new ProjectManager(testerManager);
		testerProgram.setSelectedProject(10); //selectProjectByID
		
		User testerMember = new User(1013, "", "", UserRole.MEMBER);
		testerProgram.getSelectedProject().assignMember(testerMember, 45); //Assign testerMember to task ID 45
		
		for (int i = 0; i < testerProgram.getSelectedProject().getTasks().size(); i++)
		{
			for(User u : testerProgram.getSelectedProject().getTasks().get(i).getAssignedMembers())
			{
				assertEquals(testerMember.getId(), u.getId());
			}
		}	
	}
	
	//US 4 Test Idea: If we assign person1 to task1, then person1 should see task1 as assignment.
	@Test
	public void checkAssignedTasksTest()
	{	
		User testerManager = ProjectManager.login("1002", "password");
		ProjectManager testerProgram = new ProjectManager(testerManager);
		testerProgram.setSelectedProject(10); //selectProjectByID
		
		User testerMember = new User(1013, "", "", UserRole.MEMBER);
		testerProgram.getSelectedProject().assignMember(testerMember, 45); //Assign testerMember to task ID 45
		
		User testerMemberLogin = ProjectManager.login("1013", "password");
		ArrayList<Task> testerMemberList = Project.getAssignedTasks(testerMemberLogin.getId());
		
		for(int i = 0; i < testerMemberList.size(); i++)
		{
			for (Task task : testerMemberList)
			{
				assertEquals(45, task.getId());
			}		
		}	
	}
	
	/*Iteration 3: Tests*/
	@Test
	public void CriticalPathTest() 
	{	
		User testUserLogin = ProjectManager.login("1000", "password");
		
		ProjectManager testUserProject = new ProjectManager(testUserLogin);
		testUserProject.setSelectedProject(10);
		
		
	}

}
