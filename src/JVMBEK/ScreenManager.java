package JVMBEK;

import java.awt.CardLayout;
import java.awt.List;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;

//Manager class in charge of handling all interactions with
//interface "screens" (i.e. different states the interface can be in)
public class ScreenManager {
	User user;
	ProjectManager _pm;
	Boolean projectManager;
	Map<String, Screen> _screens;
	ArrayList<User> _users;
	JFrame _frame;
	CardLayout _layout;
	JPanel _parent;

	public ScreenManager() {
		_layout = new CardLayout();
		_parent = new JPanel(_layout);
		_screens = new HashMap<String, Screen>();
		_users = new ArrayList<User>();
		loadUsers();
	}

	public void startUp() {
		// Create JFrame
		_frame = new JFrame("JVMBEK inc");
		_frame.setSize(300, 180);
		_frame.getContentPane().add(_parent);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.setLocationRelativeTo(null);
		_frame.setVisible(true);

		// Start with login screen, once we login we can populate the others
		_parent.add(LoginScreen.IDENTIFIER, new LoginScreen(this));
	}

	// Register all screens within this function
	public void setUser(User temp) {
		user = temp;
		if (user.getRole() == UserRole.MANAGER) {
			_pm = new ProjectManager(user);
			System.out.println("Logged in as manager.");
			// Once we have a project manager, we can create all other screens
			// (since they rely on the PM)
			_screens.put(ManagerMainScreen.IDENTIFIER, new ManagerMainScreen(
					this));
			_screens.put(LoadScreen.IDENTIFIER, new LoadScreen(this));
			_screens.put(CreateScreen.IDENTIFIER, new CreateScreen(this));
			_screens.put(ModifyScreen.IDENTIFIER, new ModifyScreen(this));
			_screens.put(Pert.IDENTIFIER, new Pert(this));
			_screens.put(TaskScreen.IDENTIFIER, new TaskScreen(this));
			_screens.put(CreateTaskScreen.IDENTIFIER,
					new CreateTaskScreen(this));
			_screens.put(ShowMembersScreen.IDENTIFIER, new ShowMembersScreen(
					this));
			_screens.put(AddMemberScreen.IDENTIFIER, new AddMemberScreen(this));
		} else {
			System.out.println("Logged in member.");
			_screens.put(MemberMainScreen.IDENTIFIER,
					new MemberMainScreen(this));
			_screens.put(MemberViewScreen.IDENTIFIER,
					new MemberViewScreen(this));
			_screens.put(MemberTaskInformationScreen.IDENTIFIER,
					new MemberTaskInformationScreen(this));
		}

		for (Entry<String, Screen> entry : _screens.entrySet()) {
			_parent.add(entry.getKey(), entry.getValue());
		}
	}

	public User getUser() {
		return user;
	}

	public ProjectManager getProjectManager() {
		return _pm;
	}

	public boolean hasProjectManager() {
		return _pm != null;
	}

	// Show a screen using a given ID with which it was registered
	public void show(String id) {
		_screens.get(id).Update();
		_layout.show(_parent, id);
	}

	// Show a screen with a given ID and resize the window at the same time
	public void showAndResize(String id, int width, int height) {
		_frame.setSize(width, height);
		show(id);
	}

	// Loads all users from the database
	private void loadUsers() {
		try {
			Statement stmt = DB.getInstance().createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT id, fname, lname, role FROM users");

			while (rs.next()) {
				int id = rs.getInt("id");
				UserRole role = null;
				if (rs.getString("role").equals("manager")) {
					role = UserRole.MANAGER;
				} else if (rs.getString("role").equals("user")) {
					role = UserRole.MEMBER;
				}
				_users.add(new User(id, rs.getString("fname"), rs
						.getString("lname"), role));
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public ArrayList<User> getUsers() {
		return _users;
	}

}
