package utkrishtdhankar.projectneptune;

import android.provider.ContactsContract;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
     * Returns the whereclause bit for the database query
     * @return
     */
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

        if (taskStatus != null && !taskStatus.isEmpty()) {
            // If the string builder already has something in it before this particular clause
            // Then we should add an "&&".
            if (clauseStringBuilder.length() > 0) {
                clauseStringBuilder.append(" && ");
            }
            clauseStringBuilder.append(DatabaseHelper.TASKS_KEY_STATUS);
            clauseStringBuilder.append("=?");
            whereClause.clauseArgs.add(taskStatus);
        }

        whereClause.clause = clauseStringBuilder.toString();
        return whereClause;
    }

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
        return taskPattern != null || taskStatus != null || contextName != null;
    }
}
