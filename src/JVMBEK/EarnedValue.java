package JVMBEK;

import java.util.ArrayList;

public class EarnedValue 
{
	private ArrayList<Task> tasks;
	private ArrayList<User> assignedMemberList; //Store the members that assigned to this task, in an ArrayList.

	private int plannedValue; //Store PV, in terms of duration (in days) of a given task
	private int earnedValue;
	private int actualCost;
	private int budgetAtCompletion; //BAC: Budget at Completion (Sum of PV) value.
	
	private int scheduleVariance;
	private int costVariance;
	private int costPerformanceIndex;
	private int schedulePerformanceIndex;
	private int estimateAtCompletion;
	private int estimateToComplete;
	private int varianceAtCompletion;
	
	public EarnedValue(ArrayList<Task> tasks) 
	{
		this.tasks = tasks;
	}
	
	//Formula: duration of the task (in days)
	private int calculatePlannedValue()
	{	
		return 0;
	}
	
	//Formula: duration of the task (in days)
	private int calculateEarnedValue()
	{	
		return 0;
	}

	//Formula: duration of the task (in days)
	private int calculateActualCost()
	{	
		return 0;
	}

	//Formula: Sum of all PV
	private int calculateBAC()
	{
		budgetAtCompletion = 0;
		
		return budgetAtCompletion;
	}
	
	

	
	
	public int getBAC()
	{
		return budgetAtCompletion;
	}

}
