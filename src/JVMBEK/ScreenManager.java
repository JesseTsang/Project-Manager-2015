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
	ProjectManager _pm;
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
	public void setProjectManager(ProjectManager pm) {
		_pm = pm;
		
		//Once we have a project manager, we can create all other screens (since they rely on the PM)
		_screens.put(ManagerMainScreen.IDENTIFIER, new ManagerMainScreen(this));
		_screens.put(LoadScreen.IDENTIFIER, new LoadScreen(this));
		_screens.put(CreateScreen.IDENTIFIER, new CreateScreen(this));
		_screens.put(ModifyScreen.IDENTIFIER, new ModifyScreen(this));
		_screens.put(TaskScreen.IDENTIFIER, new TaskScreen(this));
		_screens.put(CreateTaskScreen.IDENTIFIER, new CreateTaskScreen(this));
		
		for(Entry<String, Screen> entry : _screens.entrySet()) {
			_parent.add(entry.getKey(), entry.getValue());
		}
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
