package utkrishtdhankar.projectneptune;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.ArrayList;

import utkrishtdhankar.projectneptune.TaskStatusPackage.Done;

/**
 * Created by Shreyak Kumar on 12-11-2016.
 */
public class ContextsFragment extends Fragment {

    // The database that stores all of our tasks and contexts
    public DatabaseHelper databaseHelper;

    // Contains a list of all the Contexts in the inbox
    private ArrayList<TaskContext> tasksList = new ArrayList<TaskContext>();

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
                .inflate(R.layout.contexts_fragment,container,false);

        // Get a reference to the recycler view.
        // Also, set it's size to fixed to improve performance
        inboxRecyclerView = (RecyclerView) baseLayoutView.findViewById(R.id.contexts_recycler_view);
        inboxRecyclerView.setHasFixedSize(true);

        // Set a layout manager for our tasks list displaying recycler view
        inboxLayoutManager = new LinearLayoutManager(getActivity());
        inboxRecyclerView.setLayoutManager(inboxLayoutManager);

        // Get the database
        databaseHelper = new DatabaseHelper(getActivity());

        // Fill the dataset from the database, and get the contexts list on the screen
        tasksList = databaseHelper.getAllContexts();

        // Passing the dataset and fragment reference to the adapter
        recyclerViewAdapter = new ContextCardsAdapter(tasksList,ContextsFragment.this);
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
                //Remove swiped item from list and notify the RecyclerView
                TaskContext oldContext = tasksList.get(viewHolder.getAdapterPosition());
                oldContext.setId(databaseHelper.getContextId(oldContext));
                databaseHelper.deleteContext(oldContext);
                // Updating the contexts names array so that the spinner is updated
                updateContextFilterSpinner();
                getFragmentManager().beginTransaction().detach(ContextsFragment.this).attach(ContextsFragment.this).commit();
            }
        };

        //Attaching the swipe gesture to the recycler view
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(inboxRecyclerView);

        //Remove the context filtering in this fragment
        ((Spinner) getActivity().findViewById(R.id.toolbar_context_spinner)).setVisibility(View.GONE);

        // Inflate the layout for this fragment
        return baseLayoutView ;
    }

    private void updateContextFilterSpinner() {

        // Fetching all contexts from table
        final ArrayList<TaskContext> contextsArray = databaseHelper.getAllContexts();
        final String[] contextsNames= new String[contextsArray.size() + 1];
        contextsNames[0] = "All";
        for (int i = 1; i <= contextsArray.size(); i++) {
            contextsNames[i] = contextsArray.get(i - 1).getName();
        }

        // Populating the drop down menu
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, contextsNames); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        ((Spinner) getActivity().findViewById(R.id.toolbar_context_spinner)).setAdapter(spinnerArrayAdapter);

    }
}
