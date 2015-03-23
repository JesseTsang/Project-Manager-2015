package JVMBEK;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;

import org.jfree.util.ObjectUtilities;
import org.jfree.util.PublicCloneable;

/**
 * Represents an edge in the dependency graph
 * Holds a specific task
 * 
 * @author Chris Katz (ckatz2009@gmail.com)
 *
 */
public class CriticalPathEdge implements Cloneable, PublicCloneable, Serializable   {
	
	/** the node that this edge begins at */
	private CriticalPathNode previous;
	
	/** the node that this edge ends at */
	private CriticalPathNode next;
	
	/** that Task object that this edge represents */
	private Task task;
	
	/**
	 * Creates a new CriticalPathEdge with the parameter Task
	 * 
	 * @param task the Task object to be held by this edge
	 */
	public CriticalPathEdge(Task task)
	{
		previous = null;
		next = null;
		this.task = task;
	}
	
	/**
	 * sets this edge's beginning node to the parameter
	 * 
	 * @param node this edge's beginning node
	 */
	public void setPrevious(CriticalPathNode node)
	{
		previous = node;
	}
	
	/**
	 * sets this edge's ending node to the parameter value
	 * 
	 * @param node this edge's ending node
	 */
	public void setNext(CriticalPathNode node)
	{
		next = node;
	}
	
	/**
	 * returns the Task's description
	 * 
	 * @return a String
	 */
	public String getDescription()
	{
		return task.getDescription();
	}
	
	/**
	 * returns this edge's beginning node
	 * 
	 * @return a CriticalPathNode
	 */
	public CriticalPathNode getPrevious()
	{
		return previous;
	}
	
	/**
	 * returns this edge's ending node
	 * 
	 * @return a CriticalPathNode
	 */
	public CriticalPathNode getNext()
	{
		return next;
	}
	
	/**
	 * returns this Task's duration
	 * 
	 * @return a long
	 */
	public long getDuration()
	{
		// turn the Task's duration into a Date object
		Date endDate = task.getDuration().getEnd();
		Date startDate = task.getDuration().getStart();
		
		// turn the Date objects into Calendar objects
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);  
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		
		// Count from the start date to the end date to calculate duration
		Calendar date = (Calendar) startCal.clone();  
		long daysBetween = 0;  
		while (date.before(endCal)) {  
			date.add(Calendar.DAY_OF_MONTH, 1);  
			daysBetween++;  
		}  
		
		return daysBetween; 
	}
	
	/**
	 * sets the edge's Task object to the parameter value
	 * 
	 * @param task The Task object for this edge
	 */
	public void setTask(Task task)
	{
		this.task = task;
	}
	
	/**
	 * returns the edge's Task object
	 * 
	 * @return a Task object
	 */
	public Task getTask()
	{
		return task;
	}
	
	/**
     * Tests this object for equality with an arbitrary object.
     *
     * @param object  the other object (<code>null</code> permitted).
     *
     * @return A boolean.
     */
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof CriticalPathEdge)) {
            return false;
        }
        
        CriticalPathEdge that = (CriticalPathEdge)object;
        if (!ObjectUtilities.equal(this.getNext(), that.getNext())) {
            return false;
        }
        if (!ObjectUtilities.equal(this.getPrevious(), that.getPrevious())) {
            return false;
        }
        if (!ObjectUtilities.equal(this.getDescription(), that.getDescription())) {
            return false;
        }
        if (!ObjectUtilities.equal(this.getDuration(), that.getDuration())) {
            return false;
        }
        if (!ObjectUtilities.equal(this.getTask(), that.getTask())) {
            return false;
        }
        return true;
    }

    /**
     * Returns a clone of the edge.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException  never thrown by this class, but
     *         subclasses may not support cloning.
     */
    public Object clone() throws CloneNotSupportedException {
        CriticalPathNode clone = (CriticalPathNode) super.clone();
        return clone;
    }
}
