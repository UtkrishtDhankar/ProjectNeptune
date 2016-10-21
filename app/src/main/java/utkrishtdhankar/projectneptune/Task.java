package utkrishtdhankar.projectneptune;

import android.content.Intent;

/**
 * Created by utkrishtdhankar on 21/10/16.
 */
public class Task {
    // The name of this task, e.g "Do the laundry"
    private String name;

    /**
     * Constructs a new task with the given parameters
     * @param taskName Name for the task
     */
    Task(String taskName) {
        name = taskName;
    }

    /**
     * Sets the name of the Task to newName
     * @param newName The new name of the task
     */
    void modifyName(String newName) {
        name = newName;
    }

    /**
     * Getter for name
     * @return The name of the task
     */
    public String getName() {return name;}
}
