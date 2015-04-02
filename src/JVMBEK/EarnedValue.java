package JVMBEK;

import java.util.ArrayList;

public class EarnedValue 
{
	private ArrayList<Task> tasks;
	private ArrayList<User> assignedMemberList; //Store the members that assigned to this task, in an ArrayList.
	private int taskDuration; //Store the duration of the task, in # of days.
	private int estimateAtCompletion;
	public static final int COST_PER_DAY = 200;
	private int budgetAtCompletion; //BAC: Budget at Completion (BAC - Sum of PV) value.
	private int plannedValue; //Store PV

	
	public EarnedValue(ArrayList<Task> tasks) 
	{
		this.tasks = tasks;
	}
	
	//Formula: # of ppl * COST_PER_DAY * taskDuration
	private int calculateEAC()
	{
		//Currently we are using fixed cost for all members. 
		//In the future, we can add a cost colume for each member and apply that value to find the cost of a task.
		return estimateAtCompletion = assignedMemberList.size() * COST_PER_DAY * taskDuration;	
	}
	
	//Formula: Sum of all PV
	private int calculateBAC()
	{
		budgetAtCompletion = 0;
		
//		for(Task task : tasksList)
//		{
//			EarnedValue earnValueObj = new EarnedValue(task);
//			budgetAtCompletion = budgetAtCompletion + earnValueObj.calculateTaskCost();
//		}
		
		return budgetAtCompletion;
	}
	
	
	//Formula: % completed (according to plan) * budgetAtCompletion
	private int calculatePlannedValue()
	{
		
		return 0;
	}
	
	
	public int getBAC()
	{
		return budgetAtCompletion;
	}

}
