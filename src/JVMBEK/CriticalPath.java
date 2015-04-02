package JVMBEK;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.jfree.data.gantt.TaskSeries;
//import org.jfree.data.gantt.Task;

/**
 * This is a modification of the critical path algorithm originally by Chris Katz.
 * @author Chris Katz and Jesse Tsang
 */
public class CriticalPath 
{	
	/** The root node of the critical path */
	private CriticalPathNode root;
	
	/** The TaskSeries object that this critical path refers to */
	//private TaskSeries taskseries;
	private ArrayList<Task> tasksList;
	
	/** The List of nodes in the dependency graph */
	private List<CriticalPathNode> nodes;
	
	/** The List of edges in the dependency graph */
	private List<CriticalPathEdge> edges;
	
	/** The List of edges that make up the critical path of the TaskSeries */
	private List<CriticalPathEdge> criticalpath;
	
	/**
	 * Creates a new CriticalPath
	 * 
	 * @param series The TaskSeries object whose critical path needs to be calculated
	 */
	//public CriticalPath(TaskSeries series)
	public CriticalPath(ArrayList<Task> tasksListFromProject)
	{
		root = new CriticalPathNode(0);
		//taskseries = series;
		tasksList = tasksListFromProject;
		
		nodes = new ArrayList<CriticalPathNode>();
		nodes.add(root);
		edges = new ArrayList<CriticalPathEdge>();
		criticalpath = new ArrayList<CriticalPathEdge>();
	}
	
	/**
	 * External method to start the critical path calculation
	 */
	public void createPath()
	{
		CreatePath();
	}
	
	/** 
	 * Internal method to begin critical path calculation 
	 */
	private void CreatePath()
	{
		CreateEdges();//So all the tasks will be first cast to edge objects, then add to "edges" list.
		CreateRoot();
		CreateRestOfPath();
		CalculateEarliestStartDate();
		CalculateLatestStartDate();
	}
	
	/**
	 * Populates the dependency graph, putting a Task onto each edge in the graph
	 */
	private void CreateEdges()
	{
		for(Task t: tasksList)
		{
			CriticalPathEdge edge =  new CriticalPathEdge(t);
			edges.add(edge);
		}	
	}
	
	/** 
	 * Determines which Task is the root
	 */
	private void CreateRoot()
	{
		for(CriticalPathEdge edge: edges)
		{		
			edge.getTask().getPredecessors();
				
			if(edge.getTask().getPredecessorCount() == 0)
			{
				System.out.println("CriticalPath.java - CreateRoot()- edge.getTask().getName() - root edge: " + edge.getTask().getName());
				edge.setPrevious(root);
				root.addNext(edge);
			}
		}
		
		System.out.println("CriticalPath.java - CreateRoot()- Done");
	}
	
	/**
	 * Cycles through the rest of the edges in the graph and connects all dependent
	 * edges by either placing a new node in between them or connecting them via an existing node
	 */
	private void CreateRestOfPath()
	{
		System.out.println("CriticalPath.java - CreateRestOfPath() - Starting: ");
	
		int nodecounter = 1;
		
		for(CriticalPathEdge edge: edges)//First, extract all the tasks in this project, called it taskFromList.
		{
			Task task = edge.getTask();
			
			System.out.println("CriticalPath.java - CreateRestOfPath() - Edge: " + task.getName());
			
			if(task.getPredecessorCount() > 0) //If a task (taskFromList) has 1 or more predecessor
			{
				List predecessors = task.getPredecessors(); //Then, get the list of the predecessors, call it predecessorsList
				
				System.out.println("CriticalPath.java - CreateRestOfPath() - PredecessorCount > 0: Yes");
				
				for(Object next: predecessors)//For each object in this predecessorsList 
				{
					Task predecessor = (Task)next;//We cast the object as Task. call it predecessorTask
					
					System.out.println("CriticalPath.java - CreateRestOfPath() - Predecessor name: " + predecessor.getName());
					
					for(CriticalPathEdge predecessoredge: edges) //looping all the tasks from this project again, call it predecessoredge
					{
						if(predecessoredge.getTask().getId() == predecessor.getId()) //if the task (predecessoredge) is equal to predecessorTask (from predecessorsList)
						{
							System.out.println("CriticalPath.java - CreateRestOfPath() - Predecessor found.");
							
							// only create a new node if neither has a connection
							if(predecessoredge.getNext() == null && edge.getPrevious() == null)
							{
								System.out.println("CriticalPath.java - CreateRestOfPath() - null && null");
								
								CriticalPathNode nodebetween = new CriticalPathNode(nodecounter);
								nodebetween.addNext(edge);
								nodebetween.addPrevious(predecessoredge);
								edge.setPrevious(nodebetween);
								predecessoredge.setNext(nodebetween);
								nodes.add(nodebetween);
								nodecounter++;
							}
							/*
							 *  if the second edge already has a beginning node, connect the first edge
								to the second edge via the existing node
							 */
							else if(predecessoredge.getNext() == null && edge.getPrevious() != null)
							{
								System.out.println("CriticalPath.java - CreateRestOfPath() - null && !null");
								
								CriticalPathNode nodebetween = edge.getPrevious();
								nodebetween.addPrevious(predecessoredge);
								predecessoredge.setNext(nodebetween);
							}
							
							/*
							 * if the first edge already has an ending node, connect the first edge to the second
							 * edge via the existing node
							 */
							else if(predecessoredge.getNext() != null && edge.getPrevious() == null)
							{
								System.out.println("CriticalPath.java - CreateRestOfPath() - !null && null");
								
								CriticalPathNode nodebetween = predecessoredge.getNext();
								nodebetween.addNext(edge);
								edge.setPrevious(nodebetween);
							}
						}
						else
						{
							System.out.println("False! predecessoredge.getTask().getName() != predecessor.getName()");
							System.out.println("predecessoredge.getTask().getName() : " + predecessoredge.getTask().getName());
							System.out.println("predecessor.getName() : " + predecessor.getName());
						}
					}
					
				}
			}
		}
		
		/*
		 * Now we have to deal with all of the edges that end on the final node
		 * by creating the end node and setting all the correct connections
		 */
		CriticalPathNode end = new CriticalPathNode(nodecounter);
		nodes.add(end);
		
		for(CriticalPathEdge edge: edges)
		{
			if(edge.getNext() == null)
			{
				edge.setNext(end);
				end.addPrevious(edge);
			}
		}
	}
	
	/**
	 * Calculates the earliest starting date of each node
	 * 
	 * Earliest start date is determined by adding the earliest start date
	 * of any preceeding tasks to the duration of the current task
	 */
	private void CalculateEarliestStartDate()
	{
		//root.setEarliestStartDate(0);
		
		CriticalPathEdge firstTask = (CriticalPathEdge) root.getNext().get(0);
		String firstTaskStartDateString = DateUtils.getDateString(firstTask.getTask().getStartDate());
		
		System.out.println("CriticalPath.java - CalculateEarliestStartDate() - rootEarliestStartDate " + firstTaskStartDateString);
		
		root.setEarliestStartDate(firstTask.getTask().getStartDate().getTime());
		
		long time = 0;
		
		for(int i = 1; i < nodes.size(); i++)
		{
			List prevs = nodes.get(i).getPrevious();
			time = 0;
			
			for(int j = 0; j < prevs.size(); j++)
			{
				CriticalPathEdge edge = (CriticalPathEdge)prevs.get(j);
				if(edge != null)
				{
					long edgeDuration = DateUtils.getDuration(edge.getTask().getStartDate(), edge.getTask().getEndDate());
					
					if(edge.getPrevious() == null)
					{
						//time = edge.getDuration();
						time = edgeDuration;
					}
					else
					{
						time = edgeDuration + edge.getPrevious().getEarliestStartDate();
					}
					
					if(time >= nodes.get(i).getEarliestStartDate())
					{
						nodes.get(i).setEarliestStartDate(time);
					}
				}
			}
		}
	}
	
	/**
	 * Calculates the latest possible start date for each node
	 * 
	 * Latest possible start date is calculated by taking any dependent node's latest start date
	 * and subtracting the current task's duration
	 */
	private void CalculateLatestStartDate()
	{
		long largestTime = nodes.get(nodes.size()-1).getEarliestStartDate();
		long time = 0;
		
		for(int i = nodes.size()-1; i >= 0; i--)
		{
			CriticalPathNode currentNode = nodes.get(i);
			currentNode.setLatestStartDate(largestTime);
			
			List nextEdges = currentNode.getNext();
			
			for(int j = 0; j < nextEdges.size(); j++)
			{
				CriticalPathEdge edge = (CriticalPathEdge)nextEdges.get(j);
				time = edge.getNext().getLatestStartDate() - edge.getDuration();
				if(time <= currentNode.getLatestStartDate())
				{
					currentNode.setLatestStartDate(time);
				}
			}
		}
		
		// the first node's latest start date is the beginning of the project
		root.setLatestStartDate(0);
	}
	
	/**
	 * returns the critical path Task list
	 * 
	 * A task is on the critical path if it has no float time
	 * Float is defined as the latest possible start date of the following node minus the
	 * earliest possible start date of the preceeding node minus the duration of the task
	 * 
	 * @return the list of Task objects that make up the critical path 
	 */
	public ArrayList<JVMBEK.Task> getCriticalPath()
	{
		ArrayList<JVMBEK.Task> criticalPath = new ArrayList();
		
		for(CriticalPathEdge edge: edges)
		{
			System.out.println("CriticalPath.java - getCriticalPath() - Task name: " + edge.getTask().getName());
			
			long latestStartDate = edge.getNext().getLatestStartDate();
			
//			Date testDate = new Date(latestStartDate);
//			String testDateString = DateUtils.getDateString(testDate);
//			System.out.println("CriticalPath.java - getCriticalPath() - latestStartDate: " + testDateString); //Okay

			long earliestStartDate = 0;
			
//			if(edge.getPrevious() == null)
//			{
//				System.out.println("CriticalPath.java - getCriticalPath() - earliestStartDate: " + null + " -- setting 0.");
//				earliestStartDate = 0;			
//			}
//			else
//			{
//				earliestStartDate = edge.getPrevious().getEarliestStartDate();			
//			}
			
			earliestStartDate = edge.getPrevious().getEarliestStartDate();
			
//			testDate = new Date(latestStartDate);
//			testDateString = DateUtils.getDateString(testDate);	
//			System.out.println("CriticalPath.java - getCriticalPath() - earliestStartDate: " + testDateString);
			
			long duration = edge.getDuration();
			
			long taskfloat =  latestStartDate - earliestStartDate - duration;
			
			if(taskfloat == 0)
			{
				//criticalPath.add(edge);
				criticalPath.add(edge.getTask());
			}
		}
		
		return criticalPath;
	}
}
