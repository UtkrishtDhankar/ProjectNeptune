package utkrishtdhankar.projectneptune;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Inbox extends AppCompatActivity {

    public DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        databaseHelper = new DatabaseHelper(getApplicationContext());
    }




    public void onFABPress(View view) {

        FragmentManager fm = getSupportFragmentManager();
        InputDialog inputDialog = InputDialog.newInstance("Some Title",getApplicationContext());
        inputDialog.show(fm, "fragment_edit_name");
    }

    public void onSearchButtonPress(View view) {
        EditText searchTextInput = (EditText) findViewById(R.id.searchTextInput);

        long searchID = Long.parseLong(searchTextInput.getText().toString());
        databaseHelper.getTaskByID(searchID);

      //  databaseHelper.addTask(newTask);

    }
}
