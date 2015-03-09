package JVMBEK;
import java.awt.CardLayout;
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
	Map <String, Screen> _screens;
	JFrame _frame;
	CardLayout _layout;
	JPanel _parent;
	
	
	public ScreenManager()
	{
		_layout = new CardLayout();
		_parent = new JPanel(_layout);	
		_screens = new HashMap<String, Screen>();
	}
	
	public void startUp() 
	{
		// Create JFrame	  
	    _frame = new JFrame("JVMBEK inc");
	    _frame.setSize(300, 180);
	    _frame.getContentPane().add(_parent);
	    _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    _frame.setLocationRelativeTo(null);
	    _frame.setVisible(true);
	    
	    //Start with login screen, once we login we can populate the others
	    _parent.add(LoginScreen.IDENTIFIER, new LoginScreen(this));
	}
	
	//Register all screens within this function
	public void setUser(User temp) {
		user = temp;
		if(user.getRole() == UserRole.MANAGER) {
			_pm = new ProjectManager(user);
			System.out.println("yay");
			//Once we have a project manager, we can create all other screens (since they rely on the PM)
			_screens.put(ManagerMainScreen.IDENTIFIER, new ManagerMainScreen(this));
			_screens.put(LoadScreen.IDENTIFIER, new LoadScreen(this));
			_screens.put(CreateScreen.IDENTIFIER, new CreateScreen(this));
			_screens.put(ModifyScreen.IDENTIFIER, new ModifyScreen(this));
			_screens.put(TaskScreen.IDENTIFIER, new TaskScreen(this));
			_screens.put(CreateTaskScreen.IDENTIFIER, new CreateTaskScreen(this));
		}
		else {
			System.out.println("boo");
			_screens.put(MemberMainScreen.IDENTIFIER, new MemberMainScreen(this));
			_screens.put(ViewScreen.IDENTIFIER, new ViewScreen(this));
		}
		
		for(Entry<String, Screen> entry : _screens.entrySet()) {
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
	
	//Show a screen using a given ID with which it was registered
	public void show(String id) {
		_screens.get(id).Update();
		_layout.show(_parent, id);
	}
	
	//Show a screen with a given ID and resize the window at the same time
	public void showAndResize(String id, int width, int height) {
		_frame.setSize(width, height);
		show(id);
	}
}