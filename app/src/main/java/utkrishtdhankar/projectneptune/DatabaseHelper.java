package utkrishtdhankar.projectneptune;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by utkrishtdhankar on 21/10/16.
 *
 * This contains all the database related code. This can read, write, update etc. all values from
 * the database, and is to be used for all such operations.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    // The version of the database that ships with this version of the app
    // Update this if you make changes to the database so that it autoupdates the app
    private static final int DATABASE_VERSION = 1;

    // The name of the database
    private static final String DATABASE_NAME = "projectNeptune";

    // Parameters related to the tasks table. The name and all of it's column names go here.
    private static final String TASKS_TABLE_NAME = "tasks";
    private static final String TASKS_KEY_ID = "id";
    private static final String TASKS_KEY_NAME = "name";
    private static final String TASKS_KEY_STATUS = "status";

    // Parameters related to the contexts table. The name and all of it's column names go here.
    private static final String CONTEXTS_TABLE_NAME = "contexts";
    private static final String CONTEXTS_KEY_ID = "id";
    private static final String CONTEXTS_KEY_NAME = "name";

    private static final String TASKS_CONTEXTS_JUNCTION_TABLE_NAME = "tasksContextsJunction";
    private static final String TASKS_CONTEXTS_JUNCTION_KEY_TASK_ID = "taskId";
    private static final String TASKS_CONTEXTS_JUNCTION_KEY_CONTEXT_ID = "contextId";

    /**
     * Creates a new DatabaseHelper
     * @param context the context of the activity
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database needs to create all the tables.
     * Will automatically be called when the database version is updated,
     * or when no database exists.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createInboxQuery = String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT, " +
                        "%s TEXT);",
                TASKS_TABLE_NAME, TASKS_KEY_ID, TASKS_KEY_NAME, TASKS_KEY_STATUS);
        db.execSQL(createInboxQuery);

        final String createContextsQuery = String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT UNIQUE);",
                CONTEXTS_TABLE_NAME, CONTEXTS_KEY_ID, CONTEXTS_KEY_NAME);
        db.execSQL(createContextsQuery);

        final String createJunctionQuery = String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER NOT NULL, " +
                        "%s INTEGER NOT NULL, " +
                        "PRIMARY KEY(%s, %s));",
                TASKS_CONTEXTS_JUNCTION_TABLE_NAME, TASKS_CONTEXTS_JUNCTION_KEY_TASK_ID,
                TASKS_CONTEXTS_JUNCTION_KEY_CONTEXT_ID, TASKS_CONTEXTS_JUNCTION_KEY_TASK_ID,
                TASKS_CONTEXTS_JUNCTION_KEY_CONTEXT_ID);
        db.execSQL(createJunctionQuery);
    }

    /**
     * Called when database version changes.
     * Calls onCreate.
     * @param db the database we're working with
     * @param oldVersion the old version of the database
     * @param newVersion the new version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CONTEXTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + TASKS_CONTEXTS_JUNCTION_TABLE_NAME);
        onCreate(db);
    }

    /**
     * Adds the task given to the database
     * Does _not_ check if such a task already exists, so this might add duplicates
     * @param task the task to add
     */
    void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Put in the values for this task into a contentvalues
        ContentValues inboxValues = new ContentValues();
        inboxValues.put(TASKS_KEY_NAME, task.getName());
        inboxValues.put(TASKS_KEY_STATUS, task.getStatus().name());

        // Insert the task into the database and get it's id
        long newTaskId = db.insert(TASKS_TABLE_NAME, null, inboxValues);

        // Add all of the task's contexts into the database as well
        ArrayList<TaskContext> contexts = task.getAllContexts();
        for (TaskContext context : contexts) {
            // The following two paragraphs of code sees if the context we are on now
            // exists in the database. If it does not exist, then it adds it to the database
            Cursor contextsCursor = db.query(
                    CONTEXTS_TABLE_NAME,
                    new String[] {CONTEXTS_KEY_NAME, CONTEXTS_KEY_ID},
                    CONTEXTS_KEY_NAME + "=?",
                    new String[]{context.getName()},
                    null, null, null, null);

            long newContextId;
            if (contextsCursor != null && contextsCursor.getCount() > 0) {
                // If we have this cursor, we should just add an entry to the junction db
                contextsCursor.moveToFirst();
                newContextId = contextsCursor.getLong(contextsCursor.getColumnIndex(CONTEXTS_KEY_ID));
            } else {
                // If we don't have this cursor, we should add it to the database
                ContentValues contextValues = new ContentValues();
                contextValues.put(CONTEXTS_KEY_NAME, context.getName());

                newContextId = db.insert(CONTEXTS_TABLE_NAME, null, contextValues);
            }
            contextsCursor.close();

            // Add the relation to the junction table
            ContentValues junctionValues = new ContentValues();
            junctionValues.put(TASKS_CONTEXTS_JUNCTION_KEY_TASK_ID, newTaskId);
            junctionValues.put(TASKS_CONTEXTS_JUNCTION_KEY_CONTEXT_ID, newContextId);
            db.insert(TASKS_CONTEXTS_JUNCTION_TABLE_NAME, null, junctionValues);
        }

        db.close();
    }

    /**
     * @return all the tasks in the database
     */
    ArrayList<Task> getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor inboxCursor = db.query(
                TASKS_TABLE_NAME,
                new String[] {TASKS_KEY_ID, TASKS_KEY_NAME, TASKS_KEY_STATUS},
                null, null, null, null, null, null);

        ArrayList<Task> tasks = new ArrayList<Task> ();

        // TODO replace these return nulls with exceptions
        if (inboxCursor != null && inboxCursor.getCount() > 0)
            inboxCursor.moveToFirst();
        else
            return null;

        do {
            long taskId = inboxCursor.getLong(inboxCursor.getColumnIndex(TASKS_KEY_ID));

            Task task = new Task(inboxCursor.getString(inboxCursor.getColumnIndex(TASKS_KEY_NAME)));
            task.changeStatus(
                    TaskStatus.valueOf(inboxCursor.getString(inboxCursor.getColumnIndex(TASKS_KEY_STATUS))));

            String contextQuery = String.format(
                    "SELECT %s FROM %s LEFT JOIN %s " +
                            "ON %s.%s = %s.%s " +
                            "WHERE %s.%s = %s",
                    CONTEXTS_KEY_NAME, CONTEXTS_TABLE_NAME, TASKS_CONTEXTS_JUNCTION_TABLE_NAME,
                    TASKS_CONTEXTS_JUNCTION_TABLE_NAME, TASKS_CONTEXTS_JUNCTION_KEY_CONTEXT_ID, CONTEXTS_TABLE_NAME, CONTEXTS_KEY_ID,
                    TASKS_CONTEXTS_JUNCTION_TABLE_NAME, TASKS_CONTEXTS_JUNCTION_KEY_TASK_ID, Long.toString(taskId));

            Cursor contextsCursor = db.rawQuery(
                    contextQuery,
                    null);

            if (contextsCursor != null && contextsCursor.getCount() > 0) {
                contextsCursor.moveToFirst();
            } else {
                // If we've not got any tags, then this is an untagged task
                // We should move on
                contextsCursor.close();
                tasks.add(task);
                continue;
            }

            do {
                TaskContext newContext = new TaskContext(contextsCursor.getString(contextsCursor.getColumnIndex(CONTEXTS_KEY_NAME)));
                task.addContext(newContext);
            } while (contextsCursor.moveToNext());

            contextsCursor.close();
            tasks.add(task);
        } while (inboxCursor.moveToNext());

        inboxCursor.close();
        return tasks;
    }
}
