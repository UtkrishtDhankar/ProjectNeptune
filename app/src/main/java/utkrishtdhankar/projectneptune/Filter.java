package utkrishtdhankar.projectneptune;

import utkrishtdhankar.projectneptune.TaskStatusPackage.TaskStatus;

/**
 * Created by utkrishtdhankar on 15/11/16.
 */

/**
 * This class is used to filter the results of a DatabaseHelper query request. Compose a filter
 * out of the given functions and pass it to DatabaseHelper.getTasksByFilter.
 */
public class Filter {
    // This stores what name the task will be like, in the SQL format,
    // e.g. "_Hello"
    private String taskPattern;

    // What contexts to filter for
    private TaskContext context;

    // What status the task can have
    private String taskStatusName;

    /**
     * Initializes an empty filter
     * If passed to a function to get things from the database,
     * This new filter will return everything unless you set parameters
     */
    Filter() {
        taskPattern = null;
        context = null;
        taskStatusName = null;
    }

    /**
     * @param pattern the pattern to look for
     */
    public void setTaskPattern(String pattern) {
        taskPattern = pattern;
    }

    /**
     * @param context The context to look for. _Must_ be Identifiable, ie, must have a valid id.
     */
    public void setContext(TaskContext context) {
        this.context = context;
    }

    /**
     * @param status The status to look for
     */
    public void setTaskStatusName(String status) {
        taskStatusName = status;
    }

    /**
     * This gives a string for the SELECT * FROM * WHERE (* = *)* statements in the database
     * Automatically uses the join if needed
     * @return
     */
    public String getSelectAndWhereStatements() {
        StringBuilder query = new StringBuilder();

        if (context != null) {
            query.append(String.format(
                    "SELECT * " +
                            "FROM %s JOIN %s " +
                            "ON %s.%s = " +
                            "%s.%s ",
                    DatabaseHelper.TASKS_TABLE_NAME, DatabaseHelper.TASKS_CONTEXTS_JUNCTION_TABLE_NAME,
                    DatabaseHelper.TASKS_TABLE_NAME, DatabaseHelper.TASKS_KEY_ID,
                    DatabaseHelper.TASKS_CONTEXTS_JUNCTION_TABLE_NAME, DatabaseHelper.TASKS_CONTEXTS_JUNCTION_KEY_TASK_ID
                    ));
        } else {
            query.append("SELECT * FROM " + DatabaseHelper.TASKS_TABLE_NAME);
        }

        if (isAnythingSet()) {
            query.append(" WHERE ");
        } else {
            return query.toString();
        }

        // Tracks whether or not I've added any clauses yet.
        // Is set to true whenever the first clause has been added
        boolean addedAnyClausesYet = false;

        if (taskPattern != null && !taskPattern.isEmpty()) {
            query.append(DatabaseHelper.TASKS_TABLE_NAME);
            query.append(".");
            query.append(DatabaseHelper.TASKS_KEY_NAME);
            query.append(" LIKE ");
            query.append("\"" + taskPattern + "\"");

            addedAnyClausesYet = true;
        }

        if (taskStatusName != null) {
            // If the string builder already has something in it before this particular clause
            // Then we should add an "&&".
            if (addedAnyClausesYet) {
                query.append(" AND ");
            }
            query.append(DatabaseHelper.TASKS_TABLE_NAME);
            query.append(".");
            query.append(DatabaseHelper.TASKS_KEY_STATUS);
            query.append(" LIKE ");
            query.append("\"" + taskStatusName + "%\"");

            addedAnyClausesYet = true;
        }

        if (context != null) {
            if (addedAnyClausesYet) {
                query.append(" AND ");
            }

            query.append(String.format("%s.%s = %s", DatabaseHelper.TASKS_CONTEXTS_JUNCTION_TABLE_NAME, DatabaseHelper.TASKS_CONTEXTS_JUNCTION_KEY_CONTEXT_ID, Long.toString(context.getId())));

            addedAnyClausesYet = true;
        }

        return query.toString();
    }

    /**
     * @return If someone has set any of the parameters, returns true. Otherwise returns false.
     */
    private boolean isAnythingSet() {
        return taskPattern != null || taskStatusName != null || context != null;
    }
}
