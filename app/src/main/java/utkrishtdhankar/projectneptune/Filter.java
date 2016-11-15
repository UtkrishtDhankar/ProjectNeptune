package utkrishtdhankar.projectneptune;

/**
 * Created by utkrishtdhankar on 15/11/16.
 */

public class Filter {
    // This stores what name the task will be like, in the SQL format,
    // e.g. "_Hello"
    private String taskPattern;

    // What contexts to filter for
    private String contextName;

    // What status the task can have
    private String taskStatus;

    /**
     * Initializes an empty filter
     * If passed to a function to get things from the database,
     * This new filter will return everything unless you set parameters
     */
    Filter() {
        taskPattern = null;
        contextName = null;
        taskStatus = null;
    }

    /**
     * @param pattern the pattern to look for
     */
    public void setTaskPattern(String pattern) {
        taskPattern = pattern;
    }

    /**
     * @param name The name to look for
     */
    public void setContextName(String name) {
        contextName = name;
    }

    /**
     * @param status The status to look for
     */
    public void setTaskStatus(String status) {
        taskStatus = status;
    }

    /**
     * @return If someone has set any of the parameters, returns true. Otherwise returns false.
     */
    private boolean isAnythingSet() {
        return taskPattern != null || taskStatus != null || contextName != null;
    }
}
