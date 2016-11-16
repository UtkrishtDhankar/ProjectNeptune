package utkrishtdhankar.projectneptune;

import android.support.v4.app.FragmentManager;
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
 * Cards Adapter adapts a list of cards to display a bunch of tasks
 */
public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.TaskCardViewHolder> {

    InboxFragment inboxFragment;

    /**
     * Class to hold a single Card instance.
     * Contains references to all the text views etc. inside the card
     * Is used to set these views from code
     */
    public static class TaskCardViewHolder extends RecyclerView.ViewHolder {
        // The name of the task
        public TextView nameTextView;

        // The status (Inbox, Waiting, etc. for this task)
        public TextView statusTextView;

        // The context for this task (Home, etc.)
        public TextView contextTextView;

        // The cardview for this task
        public CardView cardView;

        /**
         * Constructor for this task
         * Sets the different views to their values for the view that was passed in
         * @param view
         */
        public TaskCardViewHolder(View view) {
            super(view);

            cardView = (CardView) view.findViewById(R.id.card_view);
            nameTextView = (TextView) view.findViewById(R.id.info_text);
            statusTextView = (TextView) view.findViewById(R.id.status_text);
            contextTextView = (TextView) view.findViewById(R.id.context_text);
        }
    }

    // The dataset that this adapter will inflate
    // Contains all the tasks for this view
    private ArrayList<Task> dataset;

    /**
     * Constructs a new adapter, setting the dataset to the one supplied
     * @param newDataset The dataset to be inflated
     */
    public CardsAdapter(ArrayList<Task> newDataset) {
        this.dataset = newDataset;
    }

    public CardsAdapter(ArrayList<Task> newDataset,InboxFragment inbfrag) {
        this.inboxFragment = inbfrag;
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
    public TaskCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        TaskCardViewHolder viewHolder = new TaskCardViewHolder(view);
        return viewHolder;
    }

    /**
     * Replaces the contents of holder at position with the contents from the dataset
     * Called by inflater
     * @param holder the holder for the card
     * @param position the position for this data in the dataset
     */
    @Override
    public void onBindViewHolder(TaskCardViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view (TextView) with that element's info
        holder.nameTextView.setText(dataset.get(position).getName());
        holder.statusTextView.setText(dataset.get(position).getStatus().getName());
        StringBuilder stringbuilder = new StringBuilder();
        SpannableString spannableString = new SpannableString(stringbuilder.toString());

        // Setting the colors of contexts and displaying them
        int lastContextIndex = 0;
        stringbuilder = new StringBuilder();
        ArrayList<TaskContext> taskContexts ;
        taskContexts = dataset.get(position).getAllContexts();
        for(int j = 0; j < taskContexts.size() - 1; j++) {
            stringbuilder.append(taskContexts.get(j).getName());
            stringbuilder.append(" · ");
        }
        stringbuilder.append(taskContexts.get(taskContexts.size() - 1).getName());
        spannableString = new SpannableString(stringbuilder.toString());
        for(int j = 0; j < taskContexts.size(); j++) {
            Object colorSpan = new ForegroundColorSpan(taskContexts.get(j).getColor());
            spannableString.setSpan(colorSpan, lastContextIndex, lastContextIndex + taskContexts.get(j).getName().length(), 0);
            lastContextIndex = lastContextIndex + taskContexts.get(j).getName().length() + 3 ;
        }

        //Setting the color spannable string
        holder.contextTextView.setText(spannableString);

        // Setting the onClick listener
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                FragmentManager fragmentManager = inboxFragment.getFragmentManager();
                InputFragment inputFragment = InputFragment.newInstance("edit",dataset.get(position).getName(),dataset.get(position).getAllContexts().get(0).getName(),dataset.get(position).getStatus().toString());
                inputFragment.show(fragmentManager, "fragment_edit_name");
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
