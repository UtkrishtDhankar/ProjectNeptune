package utkrishtdhankar.projectneptune;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shreyak Kumar on 28-10-2016.
 *
 * Cards Adapter adapts a list of cards to display a bunch of contexts
 */
public class ContextCardsAdapter extends RecyclerView.Adapter<ContextCardsAdapter.ContextCardViewHolder> {

    /**
     * Class to hold a single Card instance.
     * Contains references to all the text views etc. inside the card
     * Is used to set these views from code
     */
    public static class ContextCardViewHolder extends RecyclerView.ViewHolder {
        // The name of the context
        public TextView nameTextView;
        public CardView cardView;

        /**
         * Constructor for this task
         * Sets the different views to their values for the view that was passed in
         * @param view
         */
        public ContextCardViewHolder(View view) {
            super(view);

            nameTextView = (TextView) view.findViewById(R.id.context_text);
            cardView = (CardView) view.findViewById(R.id.context_card_view);
            }
    }

    // The dataset that this adapter will inflate
    // Contains all the contexts for this view
    private ArrayList<TaskContext> dataset;

    /**
     * Constructs a new adapter, setting the dataset to the one supplied
     * @param newDataset The dataset to be inflated
     */
    public ContextCardsAdapter(ArrayList<TaskContext> newDataset) {
        this.dataset = newDataset;
    }

    /**
     * Creates a new card and inflates it
     * Called by inflater
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ContextCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.context_card_layout, parent, false);

        ContextCardViewHolder viewHolder = new ContextCardViewHolder(view);
        return viewHolder;
    }

    /**
     * Replaces the contents of holder at position with the contents from the dataset
     * Called by inflater
     * @param holder the holder for the card
     * @param position the position for this data in the dataset
     */
    @Override
    public void onBindViewHolder(ContextCardViewHolder holder, int position) {
        // get element from your dataset at this position
        // replace the contents of the view (TextView) with that element's info
        holder.nameTextView.setText(dataset.get(position).getName());
        holder.nameTextView.setTextColor(dataset.get(position).getColor());
    }

    /**
     * Gets the number of items in the dataset
     * @return the number of items
     */
    @Override
    public int getItemCount() {
        if (dataset != null)
            return dataset.size();
        else
            return 0;
    }
}
