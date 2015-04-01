package JVMBEKTests;

import static org.junit.Assert.*;

import org.junit.Test;

import JVMBEK.DB;
import JVMBEK.Project;
import JVMBEK.ProjectManager;
import JVMBEK.Task;
import JVMBEK.User;
import JVMBEK.UserRole;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;


public class DatabaseTest {

    //Ensure all interactions with out project manager actually modify our database correctly.
    @Test
    public void createAndDeleteProjectDBTest()
    {
        User user = ProjectManager.login("1002", "password");
        ProjectManager pm = new ProjectManager(user);
        
        Date d = new Date();
        pm.addNewProject("Test_Project_1", "description", d);
        
        Statement stmt = null;
        try {
            stmt = DB.getInstance().createStatement();

            ResultSet rs = stmt.executeQuery("SELECT id, count(*) FROM projects WHERE name == 'Test_Project_1'");

            //********PROJECT CREATION TEST************//
            //Ensure a created project is actually added to the database
            assert(rs.next());
            assertEquals(rs.getInt("count(*)"), 1);
            //********************************//
            
            int id = rs.getInt("id");
            pm.setSelectedProject(id);
            Project proj = pm.getSelectedProject();
            
            proj.addTask("Test_Task_1", "description", d, d, 0, 0, 0, 0);
            rs = stmt.executeQuery("SELECT id, count(*) FROM tasks WHERE name == 'Test_Task_1'");
            
            //********TASK CREATION TEST************//
            //Ensure a created task is actually added to the database
            assert(rs.next());
            assertEquals(rs.getInt("count(*)"), 1);
            //********************************//
            
            int t_id = rs.getInt("id");
            proj.deleteTask(t_id);
            rs = stmt.executeQuery("SELECT count(*) FROM tasks WHERE name == 'Test_Task_1'");
            
            //********TASK DELETION TEST************//
            //Ensure a deleted task is actually deleted from our database
            assert(rs.next());
            assertEquals(rs.getInt("count(*)"), 0);
            //********************************//
            
            proj.addTask("Test_Task_2", "description", d, d, 0, 0, 0, 0);
            
            pm.deleteSelectedProject();
            rs = stmt.executeQuery("SELECT count(*) FROM projects WHERE name == 'Test_Project_1'");
            //********PROJECT DELETION TEST************//
            //Ensure a deleted project is actually deleted from our database
            assert(rs.next());
            assertEquals(rs.getInt("count(*)"), 0);
            //********************************//
            
            rs = stmt.executeQuery("SELECT count(*) FROM project_tasks WHERE project_id ==" + id);
            
            //********TASK DELETED WHEN PROJECT DELETED TEST************//
            //Ensure a deleted project appropriately removes its associated tasks from the project_tasks table
            assert(rs.next());
            assertEquals(rs.getInt("count(*)"), 0);
            //********************************//
            
            
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
