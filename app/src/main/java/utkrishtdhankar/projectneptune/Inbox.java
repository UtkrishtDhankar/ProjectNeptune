package utkrishtdhankar.projectneptune;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;


public class Inbox extends AppCompatActivity {

    public DatabaseHelper databaseHelper;

    //This is the dataset which will be used for inflation
    private ArrayList<Task> myDataset = new ArrayList<Task>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        myDataset = databaseHelper.getAllTasks();

        // specifying the adapter (MyAdapter class)
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        Log.d("Inbox.java","WE REACHED THIS POINT");
    }



    public void onFABPress(View view) {
        FragmentManager fm = getSupportFragmentManager();
        InputDialog inputDialog = InputDialog.newInstance("Some Title",getApplicationContext());
        inputDialog.show(fm, "fragment_edit_name");
    }

}
