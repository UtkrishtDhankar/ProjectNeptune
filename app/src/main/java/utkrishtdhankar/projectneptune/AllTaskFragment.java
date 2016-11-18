package utkrishtdhankar.projectneptune;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import utkrishtdhankar.projectneptune.TaskStatusPackage.Done;

/**
 * Created by Shreyak Kumar on 07-11-2016.
 */
public class AllTaskFragment extends Fragment {

    // The database that stores all of our tasks and contexts
    public DatabaseHelper databaseHelper;

    // Contains a list of all the tasks in the inbox
    private ArrayList<Task> tasksList = new ArrayList<Task>();

    // The view that contains the cards
    private RecyclerView allTasksRecyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager allTasksLayoutManager;

    /**
     * Inflates this layout and puts up all the tasks cards etc. on the screen
     * @param inflater the inflater to use to inflate this
     * @param container the container for this
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate the layout for this Inbox
        RelativeLayout baseLayoutView = (RelativeLayout) inflater
                .inflate(R.layout.status_fragment_layout,container,false);

        // Get a reference to the recycler view.
        // Also, set it's size to fixed to improve performance
        allTasksRecyclerView = (RecyclerView) baseLayoutView.findViewById(R.id.contexts_recycler_view);
        allTasksRecyclerView.setHasFixedSize(true);

        // Set a layout manager for our tasks list displaying recycler view
        allTasksLayoutManager = new LinearLayoutManager(getActivity());
        allTasksRecyclerView.setLayoutManager(allTasksLayoutManager);

        // Get the database
        databaseHelper = new DatabaseHelper(getActivity());

        //Making the filter
        Filter filter = new Filter();
        filter.setTaskStatusName("Done");

        // Fill the dataset from the database, and get the contexts list on the screen
        tasksList = databaseHelper.getTasksByFilter(filter);

        recyclerViewAdapter = new CardsAdapter(tasksList,AllTaskFragment.this);

        // Setting the adapter for the recycler view
        allTasksRecyclerView.setAdapter(recyclerViewAdapter);

        // Inflate the layout for this fragment
        return baseLayoutView ;
    }
}