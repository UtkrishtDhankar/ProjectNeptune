package utkrishtdhankar.projectneptune;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Calendar;

import utkrishtdhankar.projectneptune.TaskStatusPackage.Scheduled;
import utkrishtdhankar.projectneptune.TaskStatusPackage.TaskStatusHelper;

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
    public static final String TASKS_TABLE_NAME = "tasks";
    public static final String TASKS_KEY_ID = "id";
    public static final String TASKS_KEY_NAME = "name";
    public static final String TASKS_KEY_STATUS = "status";

    // Parameters related to the contexts table. The name and all of it's column names go here.
    public static final String CONTEXTS_TABLE_NAME = "contexts";
    public static final String CONTEXTS_KEY_ID = "id";
    public static final String CONTEXTS_KEY_COLOR = "color";
    public static final String CONTEXTS_KEY_NAME = "name";

    // Parameters related to the tasks-contexts junction table.
    // The name and all of it's column names go here.
    public static final String TASKS_CONTEXTS_JUNCTION_TABLE_NAME = "tasksContextsJunction";
    public static final String TASKS_CONTEXTS_JUNCTION_KEY_TASK_ID = "taskId";
    public static final String TASKS_CONTEXTS_JUNCTION_KEY_CONTEXT_ID = "contextId";

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
                        "%s TEXT UNIQUE, " +
                        "%s INTEGER);",
                CONTEXTS_TABLE_NAME, CONTEXTS_KEY_ID, CONTEXTS_KEY_NAME, CONTEXTS_KEY_COLOR);
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

        addDefaultContexts();
    }

    private void addDefaultContexts() {
        TaskContext homeContext = new TaskContext();
        homeContext.setName("Home");
        homeContext.setColor(Color.parseColor("#2ecc71")); // Emerland
        addContext(homeContext);

        TaskContext errandsContext = new TaskContext();
        errandsContext.setName("Errands");
        errandsContext.setColor(Color.parseColor("#e67e22")); // Carrot
        addContext(errandsContext);

        TaskContext workContext = new TaskContext();
        workContext.setName("Work");
        workContext.setColor(Color.parseColor("#34495e")); // Wet Asphalt
        addContext(workContext);
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
     * Updates all the tasks in the database. Right now, this moves up scheduled tasks to next action tasks
     */
    public void updateAll() {
        Filter filter = new Filter();
        filter.setTaskStatusName("Scheduled");

        ArrayList<Task> tasks = getTasksByFilter(filter);
        if (tasks == null) {
            return;
        }

        for (Task task : tasks) {
            if (((Scheduled) task.getStatus()).getScheduledForDate().compareTo(Calendar.getInstance()) < 0) {
                SQLiteDatabase db = getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                contentValues.put(TASKS_KEY_STATUS, "Next");

                db.update(TASKS_TABLE_NAME, contentValues, TASKS_KEY_ID + "=?",
                        new String[]{Long.toString(task.getId())});

                db.close();
            }
        }
    }

    /**
     * Adds the oldtask given to the database
     * Does _not_ check if such a oldtask already exists, so this might add duplicates
     * @param task the oldtask to add
     */
    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Put in the values for this oldtask into a contentvalues
        ContentValues taskValues = new ContentValues();
        taskValues.put(TASKS_KEY_NAME, task.getName());
        taskValues.put(TASKS_KEY_STATUS, task.getStatus().encode());

        // Insert the oldtask into the database and get it's id
        long newTaskId = db.insert(TASKS_TABLE_NAME, null, taskValues);

        // Add all of the oldtask's contexts into the database as well
        ArrayList<TaskContext> contexts = task.getAllContexts();
        for (TaskContext context : contexts) {
            // The following two paragraphs of code sees if the context we are on now
            // exists in the database. If it does not exist, then it adds it to the database
            Cursor contextsCursor = db.query(
                    CONTEXTS_TABLE_NAME,
                    new String[] {CONTEXTS_KEY_ID},
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
                contextValues.put(CONTEXTS_KEY_COLOR, context.getColor());

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
     * Updates the oldtask stored in the database with the given values.
     * @param oldTask The new values of the oldtask. _Must_ have the id set
     * @param newTask The id of the oldtask to update
     */
    public void updateTask(Task oldTask, Task newTask) {
        if (oldTask.isInvalid()) {
            throw new IllegalArgumentException("oldTask should have a valid id.");
        }

        newTask = setIdsForContexts(newTask);
        oldTask = setIdsForContexts(oldTask);

        SQLiteDatabase db = this.getWritableDatabase();

        // Put in the values for this oldtask into a contentvalues
        ContentValues taskValues = new ContentValues();
        taskValues.put(TASKS_KEY_NAME, newTask.getName());
        taskValues.put(TASKS_KEY_STATUS, newTask.getStatus().encode());

        // Update the values in the database
        db.update(TASKS_TABLE_NAME, taskValues, TASKS_KEY_ID + " = " + Long.toString(oldTask.getId()), null);

        ArrayList<TaskContext> oldTaskContexts = oldTask.getAllContexts();
        ArrayList<TaskContext> newTaskContexts = newTask.getAllContexts();

        // Delete all old context entries
        for(TaskContext context : oldTaskContexts) {
            db.delete(TASKS_CONTEXTS_JUNCTION_TABLE_NAME,
                            TASKS_CONTEXTS_JUNCTION_KEY_CONTEXT_ID + " = " + context.getId() + " AND " +
                            TASKS_CONTEXTS_JUNCTION_KEY_TASK_ID + " = " + oldTask.getId(), null);
        }

        // Add the new entries
        for (TaskContext context : newTaskContexts) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(TASKS_CONTEXTS_JUNCTION_KEY_TASK_ID, oldTask.getId());
            contentValues.put(TASKS_CONTEXTS_JUNCTION_KEY_CONTEXT_ID, context.getId());

            db.insert(TASKS_CONTEXTS_JUNCTION_TABLE_NAME, null, contentValues);
        }

        db.close();
    }

    /**
     * @param taskId The id to search for
     * @return The oldtask, if found. Otherwise, returns null
     */
    public Task getTaskById(long taskId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Get a cursor pointing to this oldtask
        Cursor tasksCursor = db.query(
                TASKS_TABLE_NAME,
                new String[]{TASKS_KEY_ID, TASKS_KEY_NAME, TASKS_KEY_STATUS},
                TASKS_KEY_ID + "=?",
                new String[]{Long.toString(taskId)},
                null, null, null, null);

        // TODO replace these return nulls with exceptions
        if (tasksCursor != null && tasksCursor.getCount() > 0) {
            tasksCursor.moveToFirst();
        } else {
            tasksCursor.close();
            db.close();
            return null;
        }

        // Store the oldtask name and status
        Task task = new Task(tasksCursor.getString(tasksCursor.getColumnIndex(TASKS_KEY_NAME)));
        task.changeStatus(
                TaskStatusHelper.decode(tasksCursor.getString(tasksCursor.getColumnIndex(TASKS_KEY_STATUS))));

        // Add all the contexts to the oldtask
        ArrayList<TaskContext> contexts = getAllContextsForTask(taskId);
        for (TaskContext context : contexts) {
            task.addContext(context);
        }

        db.close();

        return task;
    }

    public ArrayList<Task> getTasksByFilter(Filter filter) {
        SQLiteDatabase db = this.getReadableDatabase();

        // This gets _all_ the tasks
        Cursor tasksCursor = db.rawQuery(filter.getSelectAndWhereStatements(), null);

        // TODO replace these return nulls with exceptions
        if (tasksCursor != null && tasksCursor.getCount() > 0)
            tasksCursor.moveToFirst();
        else
            return null;

        ArrayList<Task> tasks = new ArrayList<Task> ();

        // Keep adding tasks to the list until we run out of tasks to add
        do {
            // Get the oldtask's id
            long taskId = tasksCursor.getLong(tasksCursor.getColumnIndex(TASKS_KEY_ID));

            // Store the oldtask name and status
            Task task = new Task(tasksCursor.getString(tasksCursor.getColumnIndex(TASKS_KEY_NAME)));
            task.changeStatus(
                    TaskStatusHelper.decode(tasksCursor.getString(tasksCursor.getColumnIndex(TASKS_KEY_STATUS))));

            // Set the id of the task
            task.setId(tasksCursor.getLong(tasksCursor.getColumnIndex(TASKS_KEY_ID)));

            // Add all the contexts to the oldtask
            ArrayList<TaskContext> contexts = getAllContextsForTask(taskId);
            for (TaskContext context : contexts) {
                task.addContext(context);
            }

            tasks.add(task);
        } while (tasksCursor.moveToNext());

        // Close the inbox cursor and we're done
        tasksCursor.close();
        db.close();
        return tasks;
    }

    /**
     * @return all the tasks in the database
     */
    public ArrayList<Task> getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();

        // This gets _all_ the tasks
        Cursor tasksCursor = db.query(
                TASKS_TABLE_NAME,
                new String[] {TASKS_KEY_ID, TASKS_KEY_NAME, TASKS_KEY_STATUS},
                null, null, null, null, null, null);

        // TODO replace these return nulls with exceptions
        if (tasksCursor != null && tasksCursor.getCount() > 0)
            tasksCursor.moveToFirst();
        else
            return null;

        ArrayList<Task> tasks = new ArrayList<Task> ();

        // Keep adding tasks to the list until we run out of tasks to add
        do {
            // Get the oldtask's id
            long taskId = tasksCursor.getLong(tasksCursor.getColumnIndex(TASKS_KEY_ID));

            // Store the oldtask name and status
            Task task = new Task(tasksCursor.getString(tasksCursor.getColumnIndex(TASKS_KEY_NAME)));
            task.changeStatus(
                    TaskStatusHelper.decode(tasksCursor.getString(tasksCursor.getColumnIndex(TASKS_KEY_STATUS))));

            // Set the id of the task
            task.setId(tasksCursor.getLong(tasksCursor.getColumnIndex(TASKS_KEY_ID)));

            // Add all the contexts to the oldtask
            ArrayList<TaskContext> contexts = getAllContextsForTask(taskId);
            for (TaskContext context : contexts) {
                task.addContext(context);
            }

            tasks.add(task);
        } while (tasksCursor.moveToNext());

        // Close the inbox cursor and we're done
        db.close();
        tasksCursor.close();
        return tasks;
    }

    /**
     * @return all the contexts in the contexts table
     */
    public ArrayList<TaskContext> getAllContexts() {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<TaskContext> contexts = new ArrayList<TaskContext>();

        // This gets _all_ the contexts
        Cursor contextsCursor = db.query(
                CONTEXTS_TABLE_NAME,
                new String[] {CONTEXTS_KEY_ID, CONTEXTS_KEY_NAME, CONTEXTS_KEY_COLOR},
                null, null, null, null, null, null);
        try {
            contextsCursor.moveToFirst();

            // Add all the contexts to the oldtask
            do {
                TaskContext newContext = new TaskContext(
                        contextsCursor.getString(contextsCursor.getColumnIndex(CONTEXTS_KEY_NAME)));
                newContext.setColor(contextsCursor.getInt(contextsCursor.getColumnIndex(CONTEXTS_KEY_COLOR)));
                newContext.setId(contextsCursor.getLong(contextsCursor.getColumnIndex(CONTEXTS_KEY_ID)));
                contexts.add(newContext);
            } while (contextsCursor.moveToNext());
        } catch(CursorIndexOutOfBoundsException exception) {
            // Do nothing. This is probably caused by the contextsCursor being empty.
            // TODO maybe make this a little more robust? Use a custom exception?
        } finally {
            contextsCursor.close();
        }

        db.close();

        return contexts;
    }

    /**
     * Given a context, returns the id associated with it. If the context does not exist in the
     * database, returns -1.
     * @param context
     * @return
     */
    public long getContextId(TaskContext context) {
        ArrayList<TaskContext> allContexts = getAllContexts();

        long id = Identifiable.INVALID_ID;

        for (int i = 0; i < allContexts.size(); i++) {
            if (context.getName().equals(allContexts.get(i).getName())) {
                id = allContexts.get(i).getId();
                break;
            }
        }

        return id;
    }

    /**
     * Adds a new context to the contexts table
     * @param context The context to be added
     */
    public void addContext(TaskContext context) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contextValues = new ContentValues();
        contextValues.put(CONTEXTS_KEY_NAME, context.getName());
        contextValues.put(CONTEXTS_KEY_COLOR, context.getColor());

        db.insert(CONTEXTS_TABLE_NAME, null, contextValues);

        db.close();
    }

    /**
     * Updates the oldContext to have the values of newContext.
     * @param oldContext _must_ be identifiable with a valid id.
     * @param newContext
     */
    public void updateContext(TaskContext oldContext, TaskContext newContext) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Put the values in the bundle
        ContentValues contextValues = new ContentValues();
        contextValues.put(CONTEXTS_KEY_NAME, newContext.getName());
        contextValues.put(CONTEXTS_KEY_COLOR, newContext.getColor());

        // Update the value
        db.update(CONTEXTS_TABLE_NAME, contextValues,
                CONTEXTS_KEY_ID + " = " + Long.toString(oldContext.getId()), null);

        db.close();
    }

    /**
     * Deletes the context given from the database. It _must_ be identifiable.
     * @param contextToDelete
     */
    public void deleteContext(TaskContext contextToDelete) {
        SQLiteDatabase db = getWritableDatabase();

        long id = contextToDelete.getId();

        db.delete(TASKS_CONTEXTS_JUNCTION_TABLE_NAME, TASKS_CONTEXTS_JUNCTION_KEY_CONTEXT_ID
                + "=" + Long.toString(id), null);
        db.delete(CONTEXTS_TABLE_NAME, CONTEXTS_KEY_ID + "=" + Long.toString(id), null);

        db.close();
    }

    private Task setIdsForContexts(Task task) {
        Task newTask = new Task(task.getName());
        newTask.changeStatus(task.getStatus());
        newTask.setId(task.getId());

        ArrayList<TaskContext> allContexts = getAllContexts();
        ArrayList<TaskContext> taskContexts = task.getAllContexts();

        for (int i = 0; i < taskContexts.size(); i++) {
            for (int j = 0; j < allContexts.size(); j++) {
                if (taskContexts.get(i).getName().equals(allContexts.get(j).getName())) {
                    newTask.addContext(allContexts.get(j));
                }
            }
        }

        return newTask;
    }

    /**
     * @param taskId The id of the oldtask for which the contexts are needed
     * @return A list of all the contexts of the oldtask
     */
    private ArrayList<TaskContext> getAllContextsForTask(long taskId) {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<TaskContext> contexts = new ArrayList<TaskContext>();

        // Defining the query to get the contexts from the oldtask
        String contextQuery = String.format(
                "SELECT %s, %s, %s " +
                        "FROM %s LEFT JOIN %s " +
                        "ON %s.%s = %s.%s " +
                        "WHERE %s.%s = %s",
                CONTEXTS_KEY_NAME, CONTEXTS_KEY_COLOR, CONTEXTS_KEY_ID,
                CONTEXTS_TABLE_NAME, TASKS_CONTEXTS_JUNCTION_TABLE_NAME,
                TASKS_CONTEXTS_JUNCTION_TABLE_NAME, TASKS_CONTEXTS_JUNCTION_KEY_CONTEXT_ID, CONTEXTS_TABLE_NAME, CONTEXTS_KEY_ID,
                TASKS_CONTEXTS_JUNCTION_TABLE_NAME, TASKS_CONTEXTS_JUNCTION_KEY_TASK_ID, Long.toString(taskId));

        // Run the query
        Cursor contextsCursor = db.rawQuery(contextQuery, null);
        try {
            contextsCursor.moveToFirst();

            // Add all the contexts to the oldtask
            do {
                TaskContext newContext = new TaskContext(
                        contextsCursor.getString(contextsCursor.getColumnIndex(CONTEXTS_KEY_NAME)));
                newContext.setColor(contextsCursor.getInt(contextsCursor.getColumnIndex(CONTEXTS_KEY_COLOR)));
                newContext.setId(contextsCursor.getLong(contextsCursor.getColumnIndex(CONTEXTS_KEY_ID)));
                contexts.add(newContext);
            } while (contextsCursor.moveToNext());
        } catch(CursorIndexOutOfBoundsException exception) {
            // Do nothing. This is probably caused by the contextsCursor being empty.
            // TODO maybe make this a little more robust? Use a custom exception?
        } finally {
            contextsCursor.close();
            db.close();
        }

        return contexts;
    }
}
