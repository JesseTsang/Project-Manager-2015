package JVMBEK;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriod;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PublicCloneable;

/**
 * A representation of a Node in the Critical Path Analysis
 * 
 * @author Chris Katz (ckatz2009@gmail.com)
 *
 */
public class CriticalPathNode implements Cloneable, PublicCloneable, Serializable  {

	/** A List of the edges that lead into this node */
	private List previous;
	
	/** A List of the edges that lead out of this node */
	private List next;
	
	/** The earliest possible start date of this node */
	private long earliestStartDate;
	
	/** The latest possible start date of this node */
	private long latestStartDate;
	
	/** An id for each node, so we can identify them uniquely */
	private int id;
	
	/**
	 * Creates a new critical path node with the given id
	 * 
	 * @param id The unique id of the node
	 */
	public CriticalPathNode(int id)
	{
		previous = new ArrayList();
		next = new ArrayList();
		earliestStartDate = 0;
		latestStartDate = 0;
		this.id = id;
	}
	
	/**
	 * adds an edge to the list of edges that lead into this node
	 * 
	 * @param prev The CriticalPathEdge object to be added to the previous list
	 */
	public void addPrevious(Object prev)
	{
		previous.add(prev);
	}
	
	/**
	 * adds an edge to the list of edges that lead out of this node
	 * 
	 * @param next The CriticalPathEdge object to be added to the next list
	 */
	public void addNext(Object next)
	{
		this.next.add(next);
	}
	
	/**
	 * Sets the earliest possible start date to the parameter value
	 * 
	 * @param date The earliest possible time to start the current task 
	 */
	public void setEarliestStartDate(long date)
	{
		earliestStartDate = date;
	}
	
	/**
	 * Sets the latest possible start date to the parameter value
	 * 
	 * @param date The latest possible time to start the current task
	 */
	public void setLatestStartDate(long date)
	{
		latestStartDate = date;
	}
	
	/**
	 * returns the earliest possible start date
	 * 
	 * @return the earliest possible start date
	 */
	public long getEarliestStartDate()
	{
		return earliestStartDate;
	}
	
	/**
	 * returns the latest possible start date
	 * 
	 * @return the latest possible start date
	 */
	public long getLatestStartDate()
	{
		return latestStartDate;
	}
	
	/**
	 * returns all of the edges that go into this node
	 * 
	 * @return the list of edges that go into this node
	 */
	public List getPrevious()
	{
		return previous;
	}
	
	/**
	 * returns all of the edges that come out of this node
	 * 
	 * @return the list of edges that come out of this node
	 */
	public List getNext()
	{
		return next;
	}

	/**
	 * returns the unique id of this node
	 * 
	 * @return the id of this node
	 */
	public int getId()
	{
		return id;
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
        if (!(object instanceof CriticalPathNode)) {
            return false;
        }
        
        CriticalPathNode that = (CriticalPathNode)object;
        if (!ObjectUtilities.equal(this.getNext(), that.getNext())) {
            return false;
        }
        if (!ObjectUtilities.equal(this.getPrevious(), that.getPrevious())) {
            return false;
        }
        if (!ObjectUtilities.equal(this.getEarliestStartDate(), that.getEarliestStartDate())) {
            return false;
        }
        if (!ObjectUtilities.equal(this.getLatestStartDate(), that.getLatestStartDate())) {
            return false;
        }
        if (!ObjectUtilities.equal(this.getId(), that.getId())) {
            return false;
        }
        return true;
    }

    /**
     * Returns a clone of the node.
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
