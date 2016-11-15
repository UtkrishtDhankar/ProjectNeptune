package utkrishtdhankar.projectneptune;

/**
 * Created by utkrishtdhankar on 07/11/16.
 */

// All statuses extend this
public interface TaskStatus {
    // Encodes the task status for the database
    String toString();

    // Gets the name of the status, e.g Inbox, Done, Scheduled, etc.
    // Use for UI.
    String getName();

    // This gets the thing that should be appended to the status in the UI.
    // E.g, this will return the date a task is Scheduled, or the person we are waiting for.
    String getSpecial();
}