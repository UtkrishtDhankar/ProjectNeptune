package utkrishtdhankar.projectneptune;

import java.util.ArrayList;

import utkrishtdhankar.projectneptune.TaskStatusPackage.TaskStatus;

/**
 * Created by utkrishtdhankar on 15/11/16.
 */

public class Filter {
    // This stores what name the task will be like, in the SQL format,
    // e.g. "_Hello"
    private String taskPattern;

    // What contexts to filter for
    private TaskContext context;

    // What status the task can have
    private TaskStatus taskStatus;

    /**
     * Initializes an empty filter
     * If passed to a function to get things from the database,
     * This new filter will return everything unless you set parameters
     */
    Filter() {
        taskPattern = null;
        context = null;
        taskStatus = null;
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
    public void setTaskStatus(TaskStatus status) {
        taskStatus = status;
    }

    /**
     * Returns the whereclause bit for the database query
     * @return
     */
    @Deprecated
    public WhereClause getQueryWhereClause() {
        WhereClause whereClause = new WhereClause();

        if (!isAnythingSet()) {
            return whereClause;
        }

        StringBuilder clauseStringBuilder = new StringBuilder();

        if (taskPattern != null && !taskPattern.isEmpty()) {
            clauseStringBuilder.append(DatabaseHelper.TASKS_KEY_NAME);
            clauseStringBuilder.append(" LIKE ");
            whereClause.clauseArgs.add(taskPattern);
        }

        if (taskStatus != null) {
            // If the string builder already has something in it before this particular clause
            // Then we should add an "&&".
            if (clauseStringBuilder.length() > 0) {
                clauseStringBuilder.append(" && ");
            }
            clauseStringBuilder.append(DatabaseHelper.TASKS_KEY_STATUS);
            clauseStringBuilder.append("=?");
            whereClause.clauseArgs.add(taskStatus.encode());
        }

        whereClause.clause = clauseStringBuilder.toString();
        return whereClause;
    }

    /**
     * This gives a string for the SELECT * FROM * WHERE (* = *)* statements in the database
     * Automatically uses the join if needed
     * @return
     */
    public String getSelectAndWhereStatements() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM " + DatabaseHelper.TASKS_TABLE_NAME);

        if (isAnythingSet()) {
            query.append(" WHERE ");
        } else {
            // Tracks whether or not I've added any clauses yet.
            // Is set to true whenever the first clause has been added
            boolean addedAnyClausesYet = false;

            if (taskPattern != null && !taskPattern.isEmpty()) {
                query.append(DatabaseHelper.TASKS_KEY_NAME);
                query.append(" LIKE ");
                query.append(taskPattern);

                addedAnyClausesYet = true;
            }

            if (taskStatus != null) {
                // If the string builder already has something in it before this particular clause
                // Then we should add an "&&".
                if (addedAnyClausesYet) {
                    query.append(" && ");
                }
                query.append(DatabaseHelper.TASKS_KEY_STATUS);
                query.append(" = ");
                query.append(taskStatus.encode());

                addedAnyClausesYet = true;
            }
        }

        return query.toString();
    }

    @Deprecated
    public class WhereClause {
        public String clause;
        public ArrayList<String> clauseArgs;

        WhereClause() {
            clause = new String();
            clauseArgs = new ArrayList<>();
        }
    }

    /**
     * @return If someone has set any of the parameters, returns true. Otherwise returns false.
     */
    private boolean isAnythingSet() {
        return taskPattern != null || taskStatus != null || context != null;
    }
}
