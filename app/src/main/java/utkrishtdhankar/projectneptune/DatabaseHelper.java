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

    private static final String INBOX_TABLE_NAME = "inbox";
    private static final String INBOX_KEY_ID = "id";
    private static final String INBOX_KEY_NAME = "name";
    private static final String INBOX_KEY_STATUS = "status";

    private static final String CONTEXTS_TABLE_NAME = "contexts";
    private static final String CONTEXTS_KEY_ID = "id";
    private static final String CONTEXTS_KEY_NAME = "name";
    private static final String CONTEXTS_KEY_TASK_ID = "taskId";

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + INBOX_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CONTEXTS_TABLE_NAME);
        onCreate(db);
    }

    void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues inboxValues = new ContentValues();
        inboxValues.put(INBOX_KEY_NAME, task.getName());
        inboxValues.put(INBOX_KEY_STATUS, task.getStatus().name());

        long newTaskId = db.insert(INBOX_TABLE_NAME, null, inboxValues);

        ArrayList<String> contexts = task.getAllContexts();
        for (String context : contexts) {
            ContentValues contextsValues = new ContentValues();

            contextsValues.put(CONTEXTS_KEY_NAME, context);
            contextsValues.put(CONTEXTS_KEY_TASK_ID, newTaskId);
            db.insert(CONTEXTS_TABLE_NAME, null, contextsValues);
        }

        db.close();
    }

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
                continue;
            }

            do {
                task.addContext(contextsCursor.getString(contextsCursor.getColumnIndex(CONTEXTS_KEY_NAME)));
            } while (contextsCursor.moveToNext());

            contextsCursor.close();
            tasks.add(task);
        } while (inboxCursor.moveToNext());

        inboxCursor.close();
        return tasks;
    }

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
            task.addContext(contextsCursor.getString(contextsCursor.getColumnIndex(CONTEXTS_KEY_NAME)));
        } while (contextsCursor.moveToNext());


        Log.d("DatabaseHelper", String.format("Fetched task with name: %s", task.getName()));

        inboxCursor.close();
        db.close();

        return task;
    }
}
