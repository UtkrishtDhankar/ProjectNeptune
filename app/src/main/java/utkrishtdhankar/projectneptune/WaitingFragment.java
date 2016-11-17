package utkrishtdhankar.projectneptune;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import utkrishtdhankar.projectneptune.TaskStatusPackage.Next;

/**
 * Created by Shreyak Kumar on 12-11-2016.
 */
public class WaitingFragment extends Fragment {

    // The database that stores all of our tasks and contexts
    public DatabaseHelper databaseHelper;

    // Contains a list of all the Contexts in the inbox
    private ArrayList<Task> tasksList = new ArrayList<Task>();

    // The view that contains the cards
    private RecyclerView inboxRecyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager inboxLayoutManager;

    /**
     *
     * @param inflater inflater to use to inflate this
     * @param container the container for this
     * @param savedInstanceState
     * @return the view for Contexts fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the layout for this Context's fragment
        RelativeLayout baseLayoutView = (RelativeLayout) inflater
                .inflate(R.layout.status_fragments,container,false);

        // Get a reference to the recycler view.
        // Also, set it's size to fixed to improve performance
        inboxRecyclerView = (RecyclerView) baseLayoutView.findViewById(R.id.contexts_recycler_view);
        inboxRecyclerView.setHasFixedSize(true);

        // Set a layout manager for our tasks list displaying recycler view
        inboxLayoutManager = new LinearLayoutManager(getActivity());
        inboxRecyclerView.setLayoutManager(inboxLayoutManager);

        // Get the database
        databaseHelper = new DatabaseHelper(getActivity());

        //Making the filter
        Filter filter = new Filter();
        filter.setTaskStatusName("Waiting");

        // Fill the dataset from the database, and get the contexts list on the screen
        tasksList = databaseHelper.getTasksByFilter(filter);

        // Passing the dataset and fragment reference to the adapter
        recyclerViewAdapter = new CardsAdapter(tasksList,WaitingFragment.this);
        inboxRecyclerView.setAdapter(recyclerViewAdapter);

        // Inflate the layout for this fragment
        return baseLayoutView ;
    }
}
