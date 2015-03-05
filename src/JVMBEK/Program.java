package JVMBEK;
/*=============================================================================
 |      Project:  COMP 354 Assignment, Iteration 1
 |   Developers:  Benjamin Slapcoff, Vinh Truong
 |  Documenters:  Jesse Tsang, Karim Atwa
 |      Testers:  Mark Le, Edward Chiang
 |    |
 |       Course:  COMP 354
 |   Instructor:  Gregory Butler
 |     Due Date:  Feb. 4th 2015
 |
 |  Description:  The first iteration of a Project Management software system. This program
 | 				  uses an SQLite Database (stored in a file called "jvmbek" in the source directory)
 |                to maintain and manage projects and users. Managers can log into the system and
 |                create, modify and delete projects. Tasks can be added to projects as well.
 *===========================================================================*/


import java.awt.*;
import java.awt.event.*;

import org.junit.*;

import javax.swing.*;
 
class Program
{
  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(new Runnable(){
      public void run(){
        ScreenManager screenManager = new ScreenManager();
        screenManager.startUp();
      }
    });
  }
}