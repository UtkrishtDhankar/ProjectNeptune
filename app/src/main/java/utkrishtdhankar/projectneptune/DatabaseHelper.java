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
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "projectNeptune";

    private static final String TASKS_TABLE_NAME = "tasks";
    private static final String TASKS_KEY_ID = "id";
    private static final String TASKS_KEY_NAME = "name";
    private static final String TASKS_KEY_STATUS = "status";

    private static final String CONTEXTS_TABLE_NAME = "contexts";
    private static final String CONTEXTS_KEY_ID = "id";
    private static final String CONTEXTS_KEY_NAME = "name";

    private static final String TASKS_CONTEXTS_JUNCTION_TABLE_NAME = "tasksContextsJunction";
    private static final String TASKS_CONTEXTS_JUNCTION_KEY_TASK_ID = "taskId";
    private static final String TASKS_CONTEXTS_JUNCTION_KEY_CONTEXT_ID = "contextId";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CONTEXTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + TASKS_CONTEXTS_JUNCTION_TABLE_NAME);
        onCreate(db);
    }

    void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues inboxValues = new ContentValues();
        inboxValues.put(TASKS_KEY_NAME, task.getName());
        inboxValues.put(TASKS_KEY_STATUS, task.getStatus().name());

        long newTaskId = db.insert(TASKS_TABLE_NAME, null, inboxValues);

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
