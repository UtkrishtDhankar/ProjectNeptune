package utkrishtdhankar.projectneptune;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by Shreyak Kumar on 07-11-2016.
 */
public class InboxFragment extends Fragment {

    public DatabaseHelper databaseHelper;

    //This is the dataset which will be used for inflation
    private ArrayList<Task> myDataset = new ArrayList<Task>();

    private RecyclerView inboxRecyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager inboxLayoutManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Making the Relative layout and inflating
        RelativeLayout baseLayoutView = (RelativeLayout) inflater.inflate(R.layout.inbox_fragment,container,false);
        databaseHelper = new DatabaseHelper(getActivity());
        inboxRecyclerView = (RecyclerView) baseLayoutView.findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        inboxRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        inboxLayoutManager = new LinearLayoutManager(getActivity());
        inboxRecyclerView.setLayoutManager(inboxLayoutManager);
        myDataset = databaseHelper.getAllTasks();
        // specifying the adapter (CardsAdapter class)
        recyclerViewAdapter = new CardsAdapter(myDataset);
        inboxRecyclerView.setAdapter(recyclerViewAdapter);

        // Inflate the layout for this fragment
        return baseLayoutView ;
    }

}
