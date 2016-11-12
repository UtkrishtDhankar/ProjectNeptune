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

    // Parameters related to the inbox table. The name and all of it's column names go here.
    private static final String INBOX_TABLE_NAME = "inbox";
    private static final String INBOX_KEY_ID = "id";
    private static final String INBOX_KEY_NAME = "name";
    private static final String INBOX_KEY_STATUS = "status";

    // Parameters related to the contexts table. The name and all of it's column names go here.
    private static final String CONTEXTS_TABLE_NAME = "contexts";
    private static final String CONTEXTS_KEY_ID = "id";
    private static final String CONTEXTS_KEY_NAME = "name";
    private static final String CONTEXTS_KEY_TASK_ID = "taskId";

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
                INBOX_TABLE_NAME, INBOX_KEY_ID, INBOX_KEY_NAME, INBOX_KEY_STATUS);
        db.execSQL(createInboxQuery);

        final String createContextsQuery = String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT, " +
                        "%s INTEGER);",
                CONTEXTS_TABLE_NAME, CONTEXTS_KEY_ID, CONTEXTS_KEY_NAME,
                CONTEXTS_KEY_TASK_ID);
        db.execSQL(createContextsQuery);
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
        db.execSQL("DROP TABLE IF EXISTS " + INBOX_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CONTEXTS_TABLE_NAME);
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
        inboxValues.put(INBOX_KEY_NAME, task.getName());
        inboxValues.put(INBOX_KEY_STATUS, task.getStatus().name());

        // Insert the task into the database and get it's id
        long newTaskId = db.insert(INBOX_TABLE_NAME, null, inboxValues);

        // Add all of the task's contexts into the database as well
        ArrayList<TaskContext> contexts = task.getAllContexts();
        for (TaskContext context : contexts) {
            ContentValues contextsValues = new ContentValues();

            contextsValues.put(CONTEXTS_KEY_NAME, context.getName());
            contextsValues.put(CONTEXTS_KEY_TASK_ID, newTaskId);
            db.insert(CONTEXTS_TABLE_NAME, null, contextsValues);
        }

        db.close();
    }

    /**
     * @return all the tasks in the database
     */
    ArrayList<Task> getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor inboxCursor = db.query(
                INBOX_TABLE_NAME,
                new String[] {INBOX_KEY_ID, INBOX_KEY_NAME, INBOX_KEY_STATUS},
                null, null, null, null, null, null);

        ArrayList<Task> tasks = new ArrayList<Task> ();

        // TODO replace these return nulls with exceptions
        if (inboxCursor != null && inboxCursor.getCount() > 0)
            inboxCursor.moveToFirst();
        else
            return null;

        do {
            long taskId = inboxCursor.getLong(inboxCursor.getColumnIndex(INBOX_KEY_ID));

            Task task = new Task(inboxCursor.getString(inboxCursor.getColumnIndex(INBOX_KEY_NAME)));
            task.changeStatus(
                    TaskStatus.valueOf(inboxCursor.getString(inboxCursor.getColumnIndex(INBOX_KEY_STATUS))));

            Cursor contextsCursor = db.query(
                    CONTEXTS_TABLE_NAME,
                    new String[] {CONTEXTS_KEY_NAME},
                    CONTEXTS_KEY_TASK_ID + "=?",
                    new String[] {Long.toString(taskId)},
                    null, null, null, null);

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

    /**
     * Finds a task by it's id and returns it
     * @param id the thing to search for
     * @return the task
     */
    Task getTaskByID(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor inboxCursor = db.query(
                INBOX_TABLE_NAME,
                new String[] {INBOX_KEY_NAME},
                INBOX_KEY_ID + "=?",
                new String[] {Long.toString(id)},
                null, null, null, null);

        // TODO replace these return nulls with exceptions
        if (inboxCursor != null && inboxCursor.getCount() > 0)
            inboxCursor.moveToFirst();
        else
            return null;

        Task task = new Task(inboxCursor.getString(inboxCursor.getColumnIndex(INBOX_KEY_NAME)));
        task.changeStatus(
                TaskStatus.valueOf(inboxCursor.getString(inboxCursor.getColumnIndex(INBOX_KEY_STATUS))));

        Cursor contextsCursor = db.query(
                CONTEXTS_TABLE_NAME,
                new String[] {CONTEXTS_KEY_NAME},
                CONTEXTS_KEY_TASK_ID + "=?",
                new String[] {Long.toString(id)},
                null, null, null, null);

        if (contextsCursor != null && contextsCursor.getCount() > 0) {
            contextsCursor.moveToFirst();
        } else {
            // If we've not got any tags, then this is an untagged task
            // We should move on
            inboxCursor.close();
            contextsCursor.close();
            db.close();
            return task;
        }

        do {
            TaskContext newContext = new TaskContext(contextsCursor.getString(contextsCursor.getColumnIndex(CONTEXTS_KEY_NAME)));
            task.addContext(newContext);
        } while (contextsCursor.moveToNext());


        Log.d("DatabaseHelper", String.format("Fetched task with name: %s", task.getName()));

        inboxCursor.close();
        db.close();

        return task;
    }
}
