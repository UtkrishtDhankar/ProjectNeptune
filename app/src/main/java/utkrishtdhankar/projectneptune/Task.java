package utkrishtdhankar.projectneptune;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by utkrishtdhankar on 21/10/16.
 */
public class Task {
    // The name of this task, e.g "Do the laundry"
    private String name;

    // The tags list for this task
    private ArrayList<String> tags;

    // What is the status of the task, e.g. Next, Waiting etc.
    private TaskStatus status;

    /**
     * Constructs a new task with the given parameters
     * Sets the tag list to empty and the status to inbox
     * @param taskName Name for the task
     */
    Task(String taskName) {
        name = taskName;
        tags = new ArrayList<String>();
        status = TaskStatus.Inbox;
    }

    /**
     * Returns the status of the task, e.g. Inbox, Next etc.
     * @return The status
     */
    TaskStatus getStatus() {
        return status;
    }

    /**
     * Changes the status of the task
     * @param newStatus the new value of the status of this task
     */
    void changeStatus(TaskStatus newStatus) {
        status = newStatus;
    }
    /**
     * Adds a new tag to the task
     * @param newTag The tag to be added
     */
    public void addTag(String newTag) {
        if (!newTag.isEmpty() && !tags.contains(newTag)) {
            tags.add(newTag);
        }
    }

    /**
     * Removes a tag from the task.
     * If the given tag exists, it will be removed.
     * @param tagToBeRemoved The tag that will be searched for and removed
     */
    public void removeTag(String tagToBeRemoved) {
        // remove all tags which are the same as tagToBeRemoved
        tags.removeAll(Collections.singleton(tagToBeRemoved));
    }

    /**
     * Returns all the tags accociated with this task
     * @return an array of all the tags
     */
    public ArrayList<String> getAllTags() {
        return tags;
    }

    /**
     * Sets the name of the Task to newName
     * @param newName The new name of the task
     */
    public void modifyName(String newName) {
        name = newName;
    }

    /**
     * Getter for name
     * @return The name of the task
     */
    public String getName() {return name;}
}
