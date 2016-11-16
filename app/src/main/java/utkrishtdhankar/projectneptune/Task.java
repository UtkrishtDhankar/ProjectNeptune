package utkrishtdhankar.projectneptune;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import utkrishtdhankar.projectneptune.TaskStatusPackage.Inbox;
import utkrishtdhankar.projectneptune.TaskStatusPackage.TaskStatus;

/**
 * Created by utkrishtdhankar on 21/10/16.
 */
public class Task extends Identifiable {
    // The name of this oldtask, e.g "Do the laundry"
    private String name;

    // The contexts list for this oldtask
    private ArrayList<TaskContext> contexts;

    // What is the status of the oldtask, e.g. Next, Waiting etc.
    private TaskStatus status;

    // When is this task due to be completed?
    private Calendar dueDate;

    /**
     * Constructs a new oldtask with the given parameters
     * Sets the tag list to empty and the status to inbox
     * @param taskName Name for the oldtask
     */
    Task(String taskName) {
        name = taskName;
        contexts = new ArrayList<TaskContext>();
        status = new Inbox();

        // This task does not have a due date now
        dueDate = null;
    }

    /**
     * Returns the status of the oldtask, e.g. Inbox, Next etc.
     * @return The status
     */
    TaskStatus getStatus() {
        return status;
    }

    /**
     * Changes the status of the oldtask
     * @param newStatus the new value of the status of this oldtask
     */
    void changeStatus(TaskStatus newStatus) {
        status = newStatus;
    }

    /**
     * Adds a new context to the oldtask
     * @param newContext The context to be added
     */
    public void addContext(TaskContext newContext) {
        if (newContext != null && !contexts.contains(newContext)) {
            contexts.add(newContext);
        }
    }

    /**
     * Removes a tag from the oldtask.
     * If the given tag exists, it will be removed.
     * @param contextToBeRemoved The tag that will be searched for and removed
     */
    public void removeTag(TaskContext contextToBeRemoved) {
        // remove all contexts which are the same as tagToBeRemoved
        contexts.removeAll(Collections.singleton(contextToBeRemoved));
    }

    /**
     * Returns all the contexts associated with this oldtask
     * @return an array of all the contexts
     */
    public ArrayList<TaskContext> getAllContexts() {
        return contexts;
    }

    /**
     * Sets the name of the Task to newName
     * @param newName The new name of the oldtask
     */
    public void modifyName(String newName) {
        name = newName;
    }

    /**
     * Getter for name
     * @return The name of the oldtask
     */
    public String getName() {return name;}

    /**
     * @return the due date for this task
     */
    public Calendar getDueDate() {
        return dueDate;
    }

    /**
     * Sets the due date of this task to whatever is the parameter dueDate.
     * @param dueDate
     */
    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Unsets the due date. This task effectively does not have a due date now.
     */
    public void unsetDueDate() {
        this.dueDate = null;
    }
}
