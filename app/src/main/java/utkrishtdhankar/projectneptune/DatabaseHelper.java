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

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT)",
                INBOX_TABLE_NAME, INBOX_KEY_ID, INBOX_KEY_NAME, INBOX_KEY_STATUS);
        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + INBOX_TABLE_NAME);
        onCreate(db);
    }

    void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(INBOX_KEY_NAME, task.getName());
        values.put(INBOX_KEY_STATUS, task.getStatus().name());

        db.insert(INBOX_TABLE_NAME, null, values);
        db.close();
    }

    ArrayList<Task> getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                INBOX_TABLE_NAME,
                new String[] {INBOX_KEY_NAME, INBOX_KEY_STATUS},
                null, null, null, null, null, null);

        ArrayList<Task> tasks = new ArrayList<Task> ();

        // TODO replace these return nulls with exceptions
        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;

        do {
            Task task = new Task(cursor.getString(cursor.getColumnIndex(INBOX_KEY_NAME)));
            task.changeStatus(
                    TaskStatus.valueOf(cursor.getString(cursor.getColumnIndex(INBOX_KEY_STATUS))));
            tasks.add(task);
        } while (cursor.moveToNext());

        return tasks;
    }

    Task getTaskByID(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                INBOX_TABLE_NAME,
                new String[] {INBOX_KEY_NAME},
                INBOX_KEY_ID + "=?",
                new String[] {Long.toString(id)},
                null, null, null, null);

        // TODO replace these return nulls with exceptions
        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;

        Task task = new Task(cursor.getString(cursor.getColumnIndex(INBOX_KEY_NAME)));

        Log.d("DatabaseHelper", String.format("Fetched task with name: %s", task.getName()));

        cursor.close();
        db.close();

        return task;
    }
}
