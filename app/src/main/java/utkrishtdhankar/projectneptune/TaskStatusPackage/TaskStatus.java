package utkrishtdhankar.projectneptune.TaskStatusPackage;

/**
 * Created by utkrishtdhankar on 07/11/16.
 */

// All statuses extend this
public interface TaskStatus {
    // Encodes the task status for the database
    public String encode();

    // Gets the name of the status, e.g Inbox, Done, Scheduled, etc.
    // Use for UI.
    public String getName();

    // This gets the thing that should be appended to the status in the UI.
    // E.g, this will return the date a task is Scheduled, or the person we are waiting for.
    public String getSpecial();
}