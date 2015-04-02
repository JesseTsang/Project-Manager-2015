package JVMBEK;

/*=============================================================================
|	Project:		COMP 354 Assignment, Iteration 3
|	Developers:		Jesse Tsang, Karim Atwa
|	Documenters:	Mark Le, Edward Chiang 
|	Testers:		Benjamin Slapcoff, Vinh Truong
|	Course:			COMP 354
|	Instructor:		Gregory Butler
|	Due Date:		April 01 2015
|
|	Description:	The third and lase iteration of a Project Management software system. This program
|					uses an SQLite Database (stored in a file called "jvmbek" in the source directory)
|					to maintain and manage projects and users. Managers can log into the system and
|					create, modify and delete projects. Tasks can be added to projects as well, and assigned
|					to members, who can view those assigned to them. Also, project managers can easily
|					generate GANTT charts. 
*===========================================================================*/

import java.awt.*;
import java.awt.event.*;

import org.junit.*;

import javax.swing.*;

class Program {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ScreenManager screenManager = new ScreenManager();
				screenManager.startUp();
			}
		});
	}
}