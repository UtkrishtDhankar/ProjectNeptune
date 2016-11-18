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
import android.widget.Spinner;

import java.util.ArrayList;

import utkrishtdhankar.projectneptune.TaskStatusPackage.Done;

/**
 * Created by Shreyak Kumar on 12-11-2016.
 */
public class WaitingFragment extends Fragment {

    // The database that stores all of our tasks and contexts
    public DatabaseHelper databaseHelper;

    int filterSpecProvided = 0;

    // Contains a list of all the Contexts in the inbox
    private ArrayList<Task> tasksList = new ArrayList<Task>();

    // The view that contains the cards
    private RecyclerView inboxRecyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager inboxLayoutManager;

    public static WaitingFragment newInstance(String contextFilterSpec, long contextId) {
        WaitingFragment frag = new WaitingFragment();

        // Set the arguments for the fragment
        Bundle args = new Bundle();
        args.putString("contextFilter",contextFilterSpec);
        args.putLong("contextId",contextId);
        frag.setArguments(args);

        return frag;
    }

    public static WaitingFragment newInstance() {
        WaitingFragment frag = new WaitingFragment();
        return frag;
    }

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
                .inflate(R.layout.status_fragment_layout,container,false);

        if(getArguments() != null) {
            filterSpecProvided = 1;
        }else {
            filterSpecProvided = 0;
        }

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

        if(filterSpecProvided == 1){
            TaskContext taskContext = new TaskContext();
            taskContext.setName(getArguments().getString("contextFilter"));
            taskContext.setId(getArguments().getLong("contextId"));
            filter.setContext(taskContext);
        }else{
            // Setting selection = 0 for "All" option in context filtering
            ((Spinner) getActivity().findViewById(R.id.toolbar_context_spinner)).setSelection(0);
        }

        // Fill the dataset from the database, and get the contexts list on the screen
        tasksList = databaseHelper.getTasksByFilter(filter);

        // Passing the dataset and fragment reference to the adapter
        recyclerViewAdapter = new CardsAdapter(tasksList,WaitingFragment.this);
        inboxRecyclerView.setAdapter(recyclerViewAdapter);

        // The Swipe gesture
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            /**
             * If you don't support drag & drop, this method will never be called.
             */
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Updating the task's status
                Task oldTask = tasksList.get(viewHolder.getAdapterPosition());
                Task newTask = oldTask;
                newTask.changeStatus(new Done());
                databaseHelper.updateTask(oldTask,newTask);

                // Reloading the fragment so that the filtering happens again (swiped card leaves no vacant space)
                getFragmentManager().beginTransaction().detach(WaitingFragment.this).attach(WaitingFragment.this).commit();
            }
        };

        //Attaching the swipe gesture to the recycler view
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(inboxRecyclerView);

        // Inflate the layout for this fragment
        return baseLayoutView ;
    }
}
