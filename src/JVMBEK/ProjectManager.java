package JVMBEK;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

//Class in charge of handling user connections and loading of projects
//from a database. The ProjectManager class manages projects only for 
//the user currently logged in.
public class ProjectManager {
	private User _owner;
	Map<Integer, Project> _projects;
	int _selected_project;

	public ProjectManager(User owner) {
		_owner = owner;
		_selected_project = -1;
		_projects = new HashMap<Integer, Project>();
		populateProjects();
	}

	public User getOwner() {
		return _owner;
	}

	public Map<Integer, Project> getProjects() {
		return _projects;
	}

	public Project getSelectedProject() {
		if (!hasSelected())
			return null;

		return _projects.get(_selected_project);
	}

	public void setSelectedProject(int id) {
		if (!_projects.containsKey(id)) {
			System.err.println("Error: Trying to select a project with ID "
					+ id + " -- not found!");
			return;
		}
		_selected_project = id;
	}

	public boolean hasSelected() {
		return _selected_project > 0;
	}

	// **********************************************************************
	// GENERAL DATABASE FUNCTIONS
	// **********************************************************************
	// To create a ProjectManager, you must login with valid credentials
	// Upon successful login, this will return a new ProjectManager.
	// Upon failed login, this will return null.
	public static User login(String uid, String password) {
		Statement stmt = null;
		try {
			stmt = DB.getInstance().createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT * FROM users WHERE id == '" + uid
							+ "' AND password == '" + password + "';");

			if (!rs.next()) {
				// No user row was found with given credentials
				JOptionPane
						.showMessageDialog(
								null,
								"The username or password you entered was invalid. Please try again.",
								"Invalid user credentials",
								JOptionPane.ERROR_MESSAGE);
				return null;
			}

			String role = rs.getString("role");
			User user;
			if (!role.equals("manager")) {
				/*
				 * //Logged in user is not a manager
				 * JOptionPane.showMessageDialog(null,
				 * "You do not have the permission to log onto this system. Please"
				 * + " contact your project manager for further assistance.",
				 * "Invalid permissions", JOptionPane.ERROR_MESSAGE);
				 */
				user = new User(rs.getInt("id"), rs.getString("fname"),
						rs.getString("lname"), UserRole.MEMBER);
			} else {
				user = new User(rs.getInt("id"), rs.getString("fname"),
						rs.getString("lname"), UserRole.MANAGER);
			}
			return user;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		return null;
	}

	// Loads all projects created by logged in user
	private void populateProjects() {
		try {
			Statement stmt = DB.getInstance().createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT * FROM projects, project_managers "
							+ "WHERE project_managers.manager_id == "
							+ _owner.getId()
							+ " AND projects.id == project_managers.project_id;");

			while (rs.next()) {
				int id = rs.getInt("id");
				Project p = new Project(id, rs.getString("name"),
						rs.getString("description"), rs.getDate("date_created"), rs.getDate("start_date"));
				_projects.put(id, p);
			}

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	// **********************************************************************
	// PROJECT DATABASE FUNCTIONS
	// **********************************************************************

	public void updateProjectName(String new_name) {
		Statement stmt = null;
		try {
			stmt = DB.getInstance().createStatement();
			String sql = "UPDATE projects SET name = '" + new_name
					+ "' WHERE id = " + _selected_project + ";";
			stmt.executeUpdate(sql);

			getSelectedProject().setName(new_name);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public void updateProjectDescription(String new_descr) {
		Statement stmt = null;
		try {
			stmt = DB.getInstance().createStatement();
			String sql = "UPDATE projects SET description = '" + new_descr
					+ "' WHERE id = " + _selected_project + ";";
			stmt.executeUpdate(sql);

			getSelectedProject().setDescription(new_descr);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public void deleteSelectedProject() {
		Statement stmt = null;
		try {
			stmt = DB.getInstance().createStatement();
			String sql = "DELETE FROM projects WHERE id = " + _selected_project
					+ ";";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM project_managers WHERE manager_id = "
					+ _owner.getId() + " AND project_id = " + _selected_project
					+ ";";
			stmt.executeUpdate(sql);

			_projects.remove(_selected_project);
			_selected_project = -1;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public void addNewProject(String name, String description, Date startDate) {
		Date dateCreated = new Date();
		Statement stmt = null;
		try {
			stmt = DB.getInstance().createStatement();
			String sql = "INSERT INTO projects (name, description, date_created, start_date) VALUES ('"
					+ name
					+ "', '"
					+ description
					+ "', '"
					+ dateCreated.getTime()
					+ "', '"
					+ startDate.getTime()
					+ "');";
			stmt.executeUpdate(sql);

			// Grab latest autoincrement id
			sql = "SELECT last_insert_rowid() as id";
			ResultSet rs = stmt.executeQuery(sql);

			if (!rs.next()) {
				System.err.println("Error while creating table '" + name + "'");
				return;
			}

			int proj_id = rs.getInt("id");

			sql = "INSERT INTO project_managers VALUES (" + proj_id + ", "
					+ _owner.getId() + ")";
			stmt.executeUpdate(sql);

			_selected_project = proj_id;
			_projects.put(proj_id,
					new Project(proj_id, name, description, dateCreated, startDate));

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
}
