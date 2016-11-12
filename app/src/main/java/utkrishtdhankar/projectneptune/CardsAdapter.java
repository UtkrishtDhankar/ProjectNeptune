package utkrishtdhankar.projectneptune;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shreyak Kumar on 28-10-2016.
 */
public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView statusView;
        public TextView contextView;

        public ViewHolder(View v) {
            super(v);
            //Linking the TextView present in R.layout.card_layout
            mTextView = (TextView) v.findViewById(R.id.info_text);
            statusView = (TextView) v.findViewById(R.id.status_text);
            contextView = (TextView) v.findViewById(R.id.context_text);
        }
    }
    //Constructor Accepting the data
    private ArrayList<Task> mDataset;
    public CardsAdapter(ArrayList<Task> myDataset) {
        this.mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view (TextView) with that element's info
        holder.mTextView.setText(mDataset.get(position).getName());
        holder.statusView.setText(mDataset.get(position).getStatus().name());

        ArrayList<TaskContext> contexts = mDataset.get(position).getAllContexts();
        if (!contexts.isEmpty()) {
            holder.contextView.setText(contexts.get(0).getName());
            holder.contextView.setTextColor(contexts.get(0).getColor());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mDataset != null)
            return mDataset.size();
        else
            return 0;
    }
}