
/**
 * 
 * @author Cooper 
 * @version 1
 */

import java.*;

public class QueueBlock implements Queue 
{
    
    private Object[] items; // an array of queue items

    private int first;  // index for the first item
    
    private int last;   // index for the last item

    /**
    * initialise a new queue
    * @param size the size of the queue 
    */
    public QueueBlock (int size) 
    { 
        items = new Object[size]; first = 0;
        last = -1;
    }
    
    
    /**
    * add a new item to the queue
    * @param a the item to add
    * @exception Overflow if queue is full */
    public void enqueue (Object a) throws Overflow 
    { 
        if (!isFull()) 
        {
            last++;
            items[last] = a;
        }
        else throw new Overflow("enqueuing to full queue");
    }
    
    /**
    * examine the first item in the queue
    * @return the first item
    * @exception Underflow if the queue is empty 
    */
    public Object examine () throws Underflow 
    {
       if (!isEmpty()) return items[first];
       else throw new Underflow("examining empty queue");
    }
    
    /**
    * remove the first item in the queue
    * @return the first item
    * @exception Underflow if the queue is empty 
    */
    public Object dequeue() throws Underflow 
    { 
       if (!isEmpty()) 
       {
           Object a = items[first];
           first++;
           return a;
       }
       else throw new Underflow("dequeuing from empty queue");
    }
    
    /**
    * test whether the queue is empty
    * @return true if the queue is empty, false otherwise 
    */
    public boolean isEmpty () 
    {
        return first == last + 1;
    }

    /**
    * test whether the queue is full
    * @return true if the queue is full, false otherwise 
    */
    public boolean isFull () 
    {
        return last == items.length - 1;
    }
    
}