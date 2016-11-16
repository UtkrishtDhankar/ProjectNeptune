package utkrishtdhankar.projectneptune;

import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

    ContextsFragment contextsFragment;

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
         * Constructor for this oldtask
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
     * @param contextsFrag  The reference from calling fragment so that we can use it to replace fragments here
     */
    public ContextCardsAdapter(ArrayList<TaskContext> newDataset, ContextsFragment contextsFrag) {
        this.dataset = newDataset;
        this.contextsFragment = contextsFrag;
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
    public void onBindViewHolder(ContextCardViewHolder holder, final int position) {
        // Importing the required fonts
        Typeface robotoLight = Typeface.createFromAsset(contextsFragment.getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        Typeface robotoLightItalic = Typeface.createFromAsset(contextsFragment.getActivity().getAssets(), "fonts/Roboto-LightItalic.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(contextsFragment.getActivity().getAssets(), "fonts/Roboto-Regular.ttf");

        // Setting the fonts for all texts in the card
        holder.nameTextView.setTypeface(robotoRegular);

        // Getting elements from the dataset at this position
        // Setting the context's text and color
        holder.nameTextView.setText(dataset.get(position).getName());
        holder.cardView.setBackgroundColor(dataset.get(position).getColor());
        // TODO set context's text color according to the background color

        // Setting the onClick listener
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                FragmentManager fragmentManager = contextsFragment.getFragmentManager();
                ContextInputFragment contextInputFragment = ContextInputFragment.newInstance("edit",dataset.get(position).getName(),dataset.get(position).getColor(),dataset.get(position).getId());
                contextInputFragment.show(fragmentManager, "fragment_edit_name");
            }
        });
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
