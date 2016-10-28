package utkrishtdhankar.projectneptune;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Inbox extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        databaseHelper = new DatabaseHelper(getApplicationContext());
    }

    public void onAddButtonPress(View view) {
        EditText addTextInput = (EditText) findViewById(R.id.addTextInput);

        String newTaskName = addTextInput.getText().toString();
        Task newTask = new Task(newTaskName);

        databaseHelper.addTask(newTask);
    }
}
