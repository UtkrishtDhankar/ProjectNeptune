package utkrishtdhankar.projectneptune;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Debug;
import android.util.Log;

/**
 * Created by utkrishtdhankar on 21/10/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "neptuneData";

    private static final String INBOX_TABLE_NAME = "inbox";

    private static final String INBOX_COLUMN_ID = "id";
    private static final String INBOX_COLUMN_NAME = "name";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); // Setting the cursor to null
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_INBOX_TABLE =
                String.format("CREATE TABLE %s (%s INT AUTO_INCREMENT, %s TEXT, PRIMARY KEY (%s))",
                        INBOX_TABLE_NAME, INBOX_COLUMN_ID, INBOX_COLUMN_NAME, INBOX_COLUMN_ID);

        db.execSQL(CREATE_INBOX_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * Creates a task in the database and returns its id
     * @param task An object containing all related task data.
     *                This IGNORES the id variable.
     * @return The id of the task created
     */
    public long createTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(INBOX_COLUMN_NAME, task.getName());

        long newTaskID = db.insert(INBOX_TABLE_NAME, null, values);
        Log.d("DatabaseHelper", String.format("Created a new task with the id %s", Long.toString(newTaskID)));
        return newTaskID;
    }

    public Task getTaskByID(long taskID) {
        SQLiteDatabase db = this.getReadableDatabase();

        Log.d("DatabaseHelper", Long.toString(taskID));
        String selectQuery = String.format("SELECT * FROM %s WHERE %s = %s",
                INBOX_TABLE_NAME, INBOX_COLUMN_ID, Long.toString(taskID));

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            return null;
        }

        Task newTask = new Task(cursor.getString(cursor.getColumnIndex(INBOX_COLUMN_NAME)));
        Log.d("DatabaseHelper", String.format("Fetched task %s by id", newTask.getName()));

        return newTask;
    }
}
