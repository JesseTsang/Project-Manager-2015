package JVMBEK;

import java.util.ArrayList;
import java.util.Date;

public class EarnedValue 
{
	private ArrayList<Task> tasks;
	private ArrayList<User> assignedMemberList; //Store the members that assigned to this task, in an ArrayList.
	
	private TaskProgress taskProgress;

	//Stats for a task
	private int plannedValue; //PV: in terms of duration (in days) of a given task
	private int earnedValue; //EV
	private int actualCost; //AC
	
	//Stats for a task
	private int scheduleVariance; //SV
	private int costVariance; //CV
	private int costPerformanceIndex; //CPI
	private int schedulePerformanceIndex; //SPI
	
	//Stats for a project
	private int budgetAtCompletion; //BAC: Budget at Completion (Sum of all PV) value.
	private int estimateAtCompletion; //EAC
	private int estimateToComplete; //ETC
	private int varianceAtCompletion; //VAC
	
	public EarnedValue(ArrayList<Task> tasks) 
	{
		this.tasks = tasks;
		calculatePlannedValue();
		calculateBAC();
		calculateEarnedValue();
		calculateActualCost();
		calculateScheduleVariance();
		calculateCostVariance();
		calculateCostPerformanceIndex();
		calculateSchedulePerformanceIndex();
		calculateEstimateAtCompletion();
		calculateEstimateToComplete();
		calculateVarianceAtCompletion();
	}
	
	//Formula: duration of the task (in days)
	private void calculatePlannedValue()
	{	
		for (Task task : tasks)
		{
			int taskDuration = task.getDuration();
			task.setPlannedValue(taskDuration);
			
			System.out.println("EarnedValue.java - calculatePlannedValue() - plannedValue is : " + task.getPlannedValue());
		}
	}
	
	//Formula: Sum of all PV
	private int calculateBAC()
	{
		budgetAtCompletion = 0;
		
		for (Task task : tasks)
		{
			int pv = task.getPlannedValue();
			
			System.out.println("EarnedValue.java - calculateBAC() - plannedValue is : " + pv);
			
			budgetAtCompletion = budgetAtCompletion + pv;
		}
		
		System.out.println("EarnedValue.java - calculateBAC() - budgetAtCompletion is : " + budgetAtCompletion);
		
		return budgetAtCompletion;
	}
	
	private void calculateEarnedValue()
	{
		int percentComplete = 0;
		
		for (Task task : tasks)
		{
			if((task.get_progress() == TaskProgress.IN_QUEUE) || (task.get_progress() == TaskProgress.IN_PROGRESS))
			{
				percentComplete = 0;
				
				System.out.println("EarnedValue.java - calculateEarnedValue() - percentComplete set to : " + percentComplete);
			}
			else if ((task.get_progress() == TaskProgress.FINISHED))
			{
				percentComplete = 100;
				
				System.out.println("EarnedValue.java - calculateEarnedValue() - percentComplete set to : " + percentComplete);
			}
			
			earnedValue = percentComplete * budgetAtCompletion;
			
			System.out.println("EarnedValue.java - calculateEarnedValue() - earned Value for task : " + task.getName() + " is " + earnedValue + ".");
			
			task.setEarnedValue(earnedValue);
		} 
	}

	//Formula: duration of the task (in days)
	private void calculateActualCost()
	{
		actualCost = 0;
		
		for (Task task : tasks)
		{
			if((task.get_progress() == TaskProgress.FINISHED))
			{
				System.out.println("EarnedValue.java - calculateActualCost(): get_finished_date - Name: " + task.getName() + " Date : " + task.get_finished_date());
				
				actualCost = (int) DateUtils.getDuration(task.getStartDate(), task.get_finished_date());
					
				System.out.println("EarnedValue.java - calculateActualCost(): Name - " + task.getName() + " : Actual cost : "+  actualCost);
			}
			else
			{
				Date todayDate = new Date();
				
				actualCost = (int) DateUtils.getDuration(task.getStartDate(), todayDate);
				
				System.out.println("EarnedValue.java - calculateActualCost(): Name - " + task.getName() + " : Actual cost : "+  actualCost);
			}
			
			task.setActualCost(actualCost);
		} 
	}
	
	private void calculateScheduleVariance()
	{	
		scheduleVariance = 0;
		
		for (Task task : tasks)
		{
			earnedValue = task.getEarnedValue();
			plannedValue = task.getPlannedValue();
			
			scheduleVariance = earnedValue - plannedValue;
			
			task.setScheduleVariance(scheduleVariance);
		}
	}
	
	private void calculateCostVariance()
	{	
		costVariance = 0;
		
		for (Task task : tasks)
		{
			earnedValue = task.getEarnedValue();
			actualCost = task.getActualCost();
			
			costVariance = earnedValue - actualCost;
			
			task.setCostVariance(costVariance);
		}
	}
	
	//Possible logical error!
	//If AC is 0, does CPI become 0 or CPI doesn't exist? 
	private void calculateCostPerformanceIndex()
	{	
		costPerformanceIndex = 0;
		
		for (Task task : tasks)
		{
			earnedValue = task.getEarnedValue();
			actualCost = task.getActualCost();
			
			//Could be logical error here.
			if(actualCost == 0)
			{
				costPerformanceIndex = 0;	
			}
			else
			{
				costPerformanceIndex = earnedValue / actualCost;				
			}
			
			System.out.println("EarnedValue.java - calculateCostPerformanceIndex() - Task name: " + task.getName() +", CPI: " + costPerformanceIndex);
		
			task.setCostPerformanceIndex(costPerformanceIndex);
		}
	}
	
	private void calculateSchedulePerformanceIndex()
	{	
		schedulePerformanceIndex = 0;
		
		for (Task task : tasks)
		{
			earnedValue = task.getEarnedValue();
			plannedValue = task.getPlannedValue();
			
			costPerformanceIndex = earnedValue / plannedValue;
			
			task.setSchedulePerformanceIndex(schedulePerformanceIndex);
		}
	}
	
	//Possible logical error!
	//If costPerformanceIndex is 0, does estimateAtCompletion become 0 or doesn't exist?
	private void calculateEstimateAtCompletion()
	{	
		estimateAtCompletion = 0;
	
		for (Task task : tasks)
		{
			costPerformanceIndex = task.getCostPerformanceIndex();
			
			//Could be logical error here.
			if(costPerformanceIndex == 0)
			{
				estimateAtCompletion = 0;				
			}
			else
			{
				estimateAtCompletion = budgetAtCompletion / costPerformanceIndex;				
			}
					
			task.setCostPerformanceIndex(costPerformanceIndex);
		}
	}
	
	private void calculateEstimateToComplete()
	{	
		estimateToComplete = 0;
		
		for (Task task : tasks)
		{
			estimateAtCompletion = task.getEstimateAtCompletion();
			actualCost = task.getActualCost();
			
			estimateToComplete = estimateAtCompletion - actualCost;
			
			task.setEstimateToComplete(estimateToComplete);
		}
	}
	
	private void calculateVarianceAtCompletion()
	{	
		varianceAtCompletion = 0;
		
		for (Task task : tasks)
		{
			estimateAtCompletion = task.getEstimateAtCompletion();
			
			varianceAtCompletion = budgetAtCompletion - actualCost;
			
			task.setVarianceAtCompletion(varianceAtCompletion);
		}
	}
	
	public int getBAC()	{	
		return budgetAtCompletion;
	}
}
