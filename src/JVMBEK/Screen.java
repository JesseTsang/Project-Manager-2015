package JVMBEK;

import java.awt.CardLayout;
import javax.swing.JPanel;

//Base screen class that is managed by the ScreenManager.
//All screens should inherit from this class.
public abstract class Screen extends JPanel {
	protected ScreenManager _manager;

	public Screen(ScreenManager manager) {
		_manager = manager;

		SetupGUI();
	}

	// Any initial setup unrelated to project manager
	public abstract void SetupGUI();

	// Any setup related to the project manager model
	public abstract void Update();
}
